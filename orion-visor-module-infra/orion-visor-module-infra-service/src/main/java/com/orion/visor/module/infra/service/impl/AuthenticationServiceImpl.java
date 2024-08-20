package com.orion.visor.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.orion.lang.annotation.Keep;
import com.orion.lang.define.wrapper.Pair;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.crypto.Signatures;
import com.orion.lang.utils.time.Dates;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.constant.ExtraFieldConst;
import com.orion.visor.framework.common.security.LoginUser;
import com.orion.visor.framework.common.security.UserRole;
import com.orion.visor.framework.common.utils.CryptoUtils;
import com.orion.visor.framework.common.utils.IpUtils;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.redis.core.utils.RedisStrings;
import com.orion.visor.framework.redis.core.utils.RedisUtils;
import com.orion.visor.framework.security.core.utils.SecurityUtils;
import com.orion.visor.module.infra.api.SystemMessageApi;
import com.orion.visor.module.infra.convert.SystemUserConvert;
import com.orion.visor.module.infra.dao.SystemUserDAO;
import com.orion.visor.module.infra.dao.SystemUserRoleDAO;
import com.orion.visor.module.infra.define.cache.UserCacheKeyDefine;
import com.orion.visor.module.infra.define.config.AppAuthenticationConfig;
import com.orion.visor.module.infra.define.message.SystemUserMessageDefine;
import com.orion.visor.module.infra.entity.domain.SystemUserDO;
import com.orion.visor.module.infra.entity.dto.LoginTokenDTO;
import com.orion.visor.module.infra.entity.dto.LoginTokenIdentityDTO;
import com.orion.visor.module.infra.entity.dto.message.SystemMessageDTO;
import com.orion.visor.module.infra.entity.request.user.UserLoginRequest;
import com.orion.visor.module.infra.entity.vo.UserLoginVO;
import com.orion.visor.module.infra.enums.LoginTokenStatusEnum;
import com.orion.visor.module.infra.enums.UserStatusEnum;
import com.orion.visor.module.infra.service.AuthenticationService;
import com.orion.visor.module.infra.service.UserPermissionService;
import com.orion.web.servlet.web.Servlets;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 认证服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/13 22:15
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Resource
    private AppAuthenticationConfig appAuthenticationConfig;

    @Resource
    private SystemUserDAO systemUserDAO;

    @Resource
    private SystemUserRoleDAO systemUserRoleDAO;

    @Resource
    private UserPermissionService userPermissionService;

    @Resource
    private SystemMessageApi systemMessageApi;

    @Keep
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public UserLoginVO login(UserLoginRequest request, HttpServletRequest servletRequest) {
        // 获取登录信息
        String remoteAddr = IpUtils.getRemoteAddr(servletRequest);
        String location = IpUtils.getLocation(remoteAddr);
        String userAgent = Servlets.getUserAgent(servletRequest);
        // 设置日志上下文的用户 否则登录失败不会记录日志
        OperatorLogs.setUser(SystemUserConvert.MAPPER.toLoginUser(request));
        // 登录前检查
        SystemUserDO user = this.preCheckLogin(request.getUsername(), request.getPassword());
        // 重新设置日志上下文
        OperatorLogs.setUser(SystemUserConvert.MAPPER.toLoginUser(user));
        // 用户密码校验
        boolean passRight = this.checkUserPassword(user, request.getPassword(), true);
        // 发送站内信
        this.sendLoginFailedErrorMessage(passRight, user, remoteAddr, location);
        Valid.isTrue(passRight, ErrorMessage.USERNAME_PASSWORD_ERROR);
        // 用户状态校验
        this.checkUserStatus(user);
        Long id = user.getId();
        // 设置上次登录时间
        this.setLastLoginTime(id);
        // 删除用户缓存
        this.deleteUserCache(user);
        // 重设用户缓存
        this.setUserCache(user);
        long current = System.currentTimeMillis();
        // 不允许多端登录
        if (!appAuthenticationConfig.getAllowMultiDevice()) {
            // 无效化其他缓存
            this.invalidOtherDeviceToken(id, current, remoteAddr, location, userAgent);
        }
        // 生成 loginToken
        String token = this.generatorLoginToken(user, current, remoteAddr, location, userAgent);
        return UserLoginVO.builder()
                .token(token)
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        // 获取登录 token
        String loginToken = SecurityUtils.obtainAuthorization(request);
        if (loginToken == null) {
            return;
        }
        // 获取 token 信息
        Pair<Long, Long> pair = this.getLoginTokenPair(loginToken);
        if (pair == null) {
            return;
        }
        Long id = pair.getKey();
        Long current = pair.getValue();
        // 删除 loginToken & refreshToken
        String loginKey = UserCacheKeyDefine.LOGIN_TOKEN.format(id, current);
        String refreshKey = UserCacheKeyDefine.LOGIN_REFRESH.format(id, current);
        redisTemplate.delete(Lists.of(loginKey, refreshKey));
    }

    @Override
    public LoginUser getLoginUser(Long id) {
        String userInfoKey = UserCacheKeyDefine.USER_INFO.format(id);
        String userInfoCache = redisTemplate.opsForValue().get(userInfoKey);
        // 缓存存在
        if (userInfoCache != null) {
            return JSON.parseObject(userInfoCache, LoginUser.class);
        }
        // 查询用户信息
        SystemUserDO user = systemUserDAO.selectById(id);
        if (user == null) {
            return null;
        }
        // 设置用户缓存
        return this.setUserCache(user);
    }

    @Override
    public LoginTokenDTO getLoginTokenInfo(String loginToken) {
        // 获取登录 key pair
        Pair<Long, Long> pair = this.getLoginTokenPair(loginToken);
        if (pair == null) {
            return null;
        }
        // 获取登录 key value
        String loginKey = UserCacheKeyDefine.LOGIN_TOKEN.format(pair.getKey(), pair.getValue());
        String loginCache = redisTemplate.opsForValue().get(loginKey);
        if (loginCache != null) {
            return JSON.parseObject(loginCache, LoginTokenDTO.class);
        }
        // loginToken 不存在 需要查询 refreshToken
        if (!appAuthenticationConfig.getAllowRefresh()) {
            return null;
        }
        String refreshKey = UserCacheKeyDefine.LOGIN_REFRESH.format(pair.getKey(), pair.getValue());
        String refreshCache = redisTemplate.opsForValue().get(refreshKey);
        // 未查询到刷新key直接返回
        if (refreshCache == null) {
            return null;
        }
        // 执行续签操作
        LoginTokenDTO refresh = JSON.parseObject(refreshCache, LoginTokenDTO.class);
        int refreshCount = refresh.getRefreshCount() + 1;
        refresh.setRefreshCount(refreshCount);
        // 设置登录缓存
        RedisStrings.setJson(loginKey, UserCacheKeyDefine.LOGIN_TOKEN, refresh);
        if (refreshCount < appAuthenticationConfig.getMaxRefreshCount()) {
            // 小于续签最大次数 则再次设置 refreshToken
            RedisStrings.setJson(refreshKey, UserCacheKeyDefine.LOGIN_REFRESH, refresh);
        } else {
            // 大于等于续签最大次数 则删除
            redisTemplate.delete(refreshKey);
        }
        return refresh;
    }

    @Override
    public SystemUserDO preCheckLogin(String username, String password) {
        // 检查密码长度是否正确 MD5 长度为 32
        if (password.length() != Const.MD5_LEN) {
            throw Exceptions.argument(ErrorMessage.USERNAME_PASSWORD_ERROR);
        }
        // 检查登录失败次数
        String failedCountKey = UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(username);
        String failedCount = redisTemplate.opsForValue().get(failedCountKey);
        if (failedCount != null
                && Integer.parseInt(failedCount) >= appAuthenticationConfig.getLoginFailedLockCount()) {
            throw Exceptions.argument(ErrorMessage.MAX_LOGIN_FAILED);
        }
        // 获取登录用户
        SystemUserDO user = systemUserDAO.of()
                .createWrapper()
                .eq(SystemUserDO::getUsername, username)
                .then()
                .getOne();
        Valid.notNull(user, ErrorMessage.USERNAME_PASSWORD_ERROR);
        return user;
    }

    @Override
    public boolean checkUserPassword(SystemUserDO user, String password, boolean addFailedCount) {
        // 检查密码
        boolean passRight = user.getPassword().equals(Signatures.md5(password));
        if (!passRight && addFailedCount) {
            // 刷新登录失败缓存
            String failedCountKey = UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(user.getUsername());
            redisTemplate.opsForValue().increment(failedCountKey);
            RedisUtils.setExpire(failedCountKey, appAuthenticationConfig.getLoginFailedLockTime(), TimeUnit.MINUTES);
        }
        return passRight;
    }

    @Override
    public void checkUserStatus(SystemUserDO user) {
        // 检查用户状态
        UserStatusEnum.checkUserStatus(user.getStatus());
    }

    /**
     * 发送登录失败错误消息
     *
     * @param passRight  passRight
     * @param user       user
     * @param remoteAddr remoteAddr
     * @param location   location
     */
    private void sendLoginFailedErrorMessage(boolean passRight, SystemUserDO user,
                                             String remoteAddr, String location) {
        if (passRight) {
            return;
        }
        String failedCountKey = UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(user.getUsername());
        String failedCountStr = redisTemplate.opsForValue().get(failedCountKey);
        if (failedCountStr == null || !Strings.isInteger(failedCountStr)) {
            return;
        }
        // 直接用相等 因为只触发一次
        if (!appAuthenticationConfig.getLoginFailedSendThreshold().equals(Integer.valueOf(failedCountStr))) {
            return;
        }
        // 发送站内信
        Map<String, Object> params = new HashMap<>();
        params.put(ExtraFieldConst.ADDRESS, remoteAddr);
        params.put(ExtraFieldConst.LOCATION, location);
        params.put(ExtraFieldConst.TIME, Dates.current());
        SystemMessageDTO message = SystemMessageDTO.builder()
                .receiverId(user.getId())
                .receiverUsername(user.getUsername())
                .relKey(user.getUsername())
                .params(params)
                .build();
        // 发送
        systemMessageApi.create(SystemUserMessageDefine.LOGIN_FAILED, message);
    }

    /**
     * 设置最后登录时间
     *
     * @param id id
     */
    private void setLastLoginTime(Long id) {
        SystemUserDO update = new SystemUserDO();
        update.setId(id);
        update.setLastLoginTime(new Date());
        systemUserDAO.updateById(update);
    }

    /**
     * 删除用户缓存
     *
     * @param user user
     */
    private void deleteUserCache(SystemUserDO user) {
        // 用户信息缓存
        String userInfoKey = UserCacheKeyDefine.USER_INFO.format(user.getId());
        // 登录失败次数缓存
        String loginFailedCountKey = UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(user.getUsername());
        // 删除缓存
        redisTemplate.delete(Lists.of(userInfoKey, loginFailedCountKey));
    }

    /**
     * 获取 token pair
     *
     * @param loginToken loginToken
     * @return pair
     */
    private Pair<Long, Long> getLoginTokenPair(String loginToken) {
        if (loginToken == null) {
            return null;
        }
        try {
            String value = CryptoUtils.decryptBase62(loginToken);
            String[] pair = value.split(":");
            return Pair.of(Long.valueOf(pair[0]), Long.valueOf(pair[1]));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置用户缓存
     *
     * @param user user
     * @return loginUser
     */
    private LoginUser setUserCache(SystemUserDO user) {
        Long id = user.getId();
        // 查询用户角色
        List<Long> roleIds = systemUserRoleDAO.selectRoleIdByUserId(id);
        List<UserRole> roleList = userPermissionService.getRoleCache()
                .values()
                .stream()
                .filter(s -> roleIds.contains(s.getId()))
                .map(r -> new UserRole(r.getId(), r.getCode()))
                .collect(Collectors.toList());
        // 设置用户缓存
        String userInfoKey = UserCacheKeyDefine.USER_INFO.format(id);
        LoginUser loginUser = SystemUserConvert.MAPPER.toLoginUser(user);
        loginUser.setRoles(roleList);
        RedisStrings.setJson(userInfoKey, UserCacheKeyDefine.USER_INFO, loginUser);
        return loginUser;
    }

    /**
     * 无效化其他登录信息
     *
     * @param id         id
     * @param loginTime  loginTime
     * @param remoteAddr remoteAddr
     * @param location   location
     * @param userAgent  userAgent
     */
    @SuppressWarnings("ALL")
    private void invalidOtherDeviceToken(Long id, long loginTime,
                                         String remoteAddr, String location, String userAgent) {
        // 获取登录信息
        Set<String> loginKeyList = RedisUtils.scanKeys(UserCacheKeyDefine.LOGIN_TOKEN.format(id, "*"));
        if (!loginKeyList.isEmpty()) {
            // 获取有效登录信息
            List<LoginTokenDTO> loginTokenInfoList = redisTemplate.opsForValue()
                    .multiGet(loginKeyList)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(s -> JSON.parseObject(s, LoginTokenDTO.class))
                    .filter(s -> LoginTokenStatusEnum.OK.getStatus().equals(s.getStatus()))
                    .collect(Collectors.toList());
            // 修改登录信息
            for (LoginTokenDTO loginTokenInfo : loginTokenInfoList) {
                String deviceLoginKey = UserCacheKeyDefine.LOGIN_TOKEN.format(id, loginTokenInfo.getOrigin().getLoginTime());
                loginTokenInfo.setStatus(LoginTokenStatusEnum.OTHER_DEVICE.getStatus());
                loginTokenInfo.setOverride(new LoginTokenIdentityDTO(loginTime, remoteAddr, location, userAgent));
                RedisStrings.setJson(deviceLoginKey, UserCacheKeyDefine.LOGIN_TOKEN, loginTokenInfo);
            }
        }
        // 删除续签信息
        if (appAuthenticationConfig.getAllowRefresh()) {
            RedisUtils.scanKeysDelete(UserCacheKeyDefine.LOGIN_REFRESH.format(id, "*"));
        }
    }

    /**
     * 生成 loginToken
     *
     * @param user       user
     * @param loginTime  loginTime
     * @param remoteAddr remoteAddr
     * @param location   location
     * @param userAgent  userAgent
     * @return loginToken
     */
    private String generatorLoginToken(SystemUserDO user, long loginTime,
                                       String remoteAddr, String location, String userAgent) {
        Long id = user.getId();
        // 生成 loginToken
        String loginKey = UserCacheKeyDefine.LOGIN_TOKEN.format(id, loginTime);
        LoginTokenDTO loginValue = LoginTokenDTO.builder()
                .id(id)
                .status(LoginTokenStatusEnum.OK.getStatus())
                .refreshCount(0)
                .origin(new LoginTokenIdentityDTO(loginTime, remoteAddr, location, userAgent))
                .build();
        RedisStrings.setJson(loginKey, UserCacheKeyDefine.LOGIN_TOKEN, loginValue);
        // 生成 refreshToken
        if (appAuthenticationConfig.getAllowRefresh()) {
            String refreshKey = UserCacheKeyDefine.LOGIN_REFRESH.format(id, loginTime);
            RedisStrings.setJson(refreshKey, UserCacheKeyDefine.LOGIN_REFRESH, loginValue);
        }
        // 返回token
        return CryptoUtils.encryptBase62(id + ":" + loginTime);
    }

}
