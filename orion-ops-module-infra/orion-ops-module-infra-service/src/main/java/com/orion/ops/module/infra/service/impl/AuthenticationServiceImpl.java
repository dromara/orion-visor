package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.Pair;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.crypto.Signatures;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogs;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorCode;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.common.security.UserRole;
import com.orion.ops.framework.common.utils.CryptoUtils;
import com.orion.ops.framework.common.utils.IpUtils;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisStrings;
import com.orion.ops.framework.redis.core.utils.RedisUtils;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.infra.convert.SystemUserConvert;
import com.orion.ops.module.infra.dao.SystemUserDAO;
import com.orion.ops.module.infra.dao.SystemUserRoleDAO;
import com.orion.ops.module.infra.define.cache.UserCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import com.orion.ops.module.infra.entity.dto.LoginTokenDTO;
import com.orion.ops.module.infra.entity.dto.LoginTokenIdentityDTO;
import com.orion.ops.module.infra.entity.request.user.UserLoginRequest;
import com.orion.ops.module.infra.entity.vo.UserLoginVO;
import com.orion.ops.module.infra.enums.LoginTokenStatusEnum;
import com.orion.ops.module.infra.enums.UserStatusEnum;
import com.orion.ops.module.infra.service.AuthenticationService;
import com.orion.ops.module.infra.service.PermissionService;
import com.orion.web.servlet.web.Servlets;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    private SystemUserDAO systemUserDAO;

    @Resource
    private SystemUserRoleDAO systemUserRoleDAO;

    @Resource
    private PermissionService permissionService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public UserLoginVO login(UserLoginRequest request, HttpServletRequest servletRequest) {
        // 登录前检查
        this.preCheckLogin(request);
        // 获取登录用户
        LambdaQueryWrapper<SystemUserDO> wrapper = systemUserDAO.wrapper()
                .eq(SystemUserDO::getUsername, request.getUsername());
        SystemUserDO user = systemUserDAO.of(wrapper).getOne();
        // 设置日志上下文
        OperatorLogs.setUser(SystemUserConvert.MAPPER.toLoginUser(user));
        // 检查密码
        boolean passwordCorrect = this.checkPassword(request, user);
        Valid.isTrue(passwordCorrect, ErrorMessage.USERNAME_PASSWORD_ERROR);
        // 检查用户状态
        UserStatusEnum.checkUserStatus(user.getStatus());
        // 设置上次登录时间
        this.setLastLoginTime(user.getId());
        // 删除用户缓存
        this.deleteUserCache(user);
        // 重设用户缓存
        this.setUserCache(user);
        // 获取登录信息
        String remoteAddr = Servlets.getRemoteAddr(servletRequest);
        String location = IpUtils.getLocation(remoteAddr);
        String userAgent = Servlets.getUserAgent(servletRequest);
        long current = System.currentTimeMillis();
        // 不允许多端登录
        if (!allowMultiDevice) {
            // 无效化其他缓存
            this.invalidOtherDeviceToken(user.getId(), current, remoteAddr, location, userAgent);
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
            throw Exceptions.httpWrapper(ErrorCode.UNAUTHORIZED);
        }
        // 设置用户缓存
        return this.setUserCache(user);
    }

    @Override
    public LoginTokenDTO getLoginTokenInfo(String loginToken, boolean checkRefresh) {
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
        if (!checkRefresh || !allowRefresh) {
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
        if (refreshCount < maxRefreshCount) {
            // 小于续签最大次数 则再次设置 refreshToken
            RedisStrings.setJson(refreshKey, UserCacheKeyDefine.LOGIN_REFRESH, refresh);
        } else {
            // 大于等于续签最大次数 则删除
            redisTemplate.delete(refreshKey);
        }
        return refresh;
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
     * 登录预检查
     *
     * @param request request
     */
    private void preCheckLogin(UserLoginRequest request) {
        // 检查密码长度是否正确 MD5 长度为 32
        if (request.getPassword().length() != Const.MD5_LEN) {
            throw Exceptions.argument(ErrorMessage.USERNAME_PASSWORD_ERROR);
        }
        // 检查登录失败次数
        String failedCountKey = UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(request.getUsername());
        String failedCount = redisTemplate.opsForValue().get(failedCountKey);
        if (failedCount != null && Integer.parseInt(failedCount) >= maxFailedLoginCount) {
            throw Exceptions.argument(ErrorMessage.MAX_LOGIN_FAILED);
        }
    }

    /**
     * 检查密码
     *
     * @param request request
     * @param user    user
     * @return 是否正确
     */
    @SuppressWarnings("ALL")
    private boolean checkPassword(UserLoginRequest request, SystemUserDO user) {
        // 密码正确
        if (user != null && user.getPassword().equals(Signatures.md5(request.getPassword()))) {
            return true;
        }
        // 刷新登录失败缓存
        String failedCountKey = UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(request.getUsername());
        Long failedLoginCount = redisTemplate.opsForValue().increment(failedCountKey);
        RedisUtils.setExpire(failedCountKey, UserCacheKeyDefine.LOGIN_FAILED_COUNT);
        // 用户不存在
        if (user == null) {
            return false;
        }
        // 锁定用户
        if (failedLoginCount >= maxFailedLoginCount) {
            // 更新用户表
            SystemUserDO updateUser = new SystemUserDO();
            updateUser.setId(user.getId());
            updateUser.setStatus(UserStatusEnum.LOCKED.getStatus());
            systemUserDAO.updateById(updateUser);
            // 更新缓存
            String userInfoKey = UserCacheKeyDefine.USER_INFO.format(user.getId());
            String userInfoCache = redisTemplate.opsForValue().get(userInfoKey);
            // 缓存不存在
            if (userInfoCache == null) {
                return false;
            }
            // 修改缓存状态
            LoginUser loginUser = JSON.parseObject(userInfoCache, LoginUser.class);
            loginUser.setStatus(UserStatusEnum.LOCKED.getStatus());
            RedisStrings.setJson(userInfoKey, UserCacheKeyDefine.USER_INFO, loginUser);
        }
        return false;
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
     * 设置用户缓存
     *
     * @param user user
     * @return loginUser
     */
    private LoginUser setUserCache(SystemUserDO user) {
        Long id = user.getId();
        // 查询用户角色
        List<Long> roleIds = systemUserRoleDAO.selectRoleIdByUserId(id);
        List<UserRole> roleList = permissionService.getRoleCache()
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
        String loginKey = UserCacheKeyDefine.LOGIN_TOKEN.format(id, "*");
        // 获取登录信息
        Set<String> loginKeyList = RedisUtils.scanKeys(loginKey);
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
        if (allowRefresh) {
            String refreshKey = UserCacheKeyDefine.LOGIN_REFRESH.format(id, "*");
            Set<String> refreshKeyList = RedisUtils.scanKeys(refreshKey);
            if (!refreshKeyList.isEmpty()) {
                redisTemplate.delete(refreshKeyList);
            }
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
        if (allowRefresh) {
            String refreshKey = UserCacheKeyDefine.LOGIN_REFRESH.format(id, loginTime);
            RedisStrings.setJson(refreshKey, UserCacheKeyDefine.LOGIN_REFRESH, loginValue);
        }
        // 返回token
        return CryptoUtils.encryptBase62(id + ":" + loginTime);
    }

}
