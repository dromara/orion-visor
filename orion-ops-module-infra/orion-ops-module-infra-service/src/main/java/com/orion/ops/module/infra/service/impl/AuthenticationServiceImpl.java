package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.constant.StandardHttpHeader;
import com.orion.lang.define.wrapper.Pair;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.crypto.Signatures;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.crypto.ValueCrypto;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.common.utils.CryptoUtils;
import com.orion.ops.framework.common.utils.IpUtils;
import com.orion.ops.framework.common.utils.Kits;
import com.orion.ops.framework.redis.core.utils.RedisUtils;
import com.orion.ops.module.infra.convert.SystemUserConvert;
import com.orion.ops.module.infra.dao.SystemUserDAO;
import com.orion.ops.module.infra.define.UserCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import com.orion.ops.module.infra.entity.dto.LoginTokenDTO;
import com.orion.ops.module.infra.entity.request.UserLoginRequest;
import com.orion.ops.module.infra.enums.LoginTokenStatusEnum;
import com.orion.ops.module.infra.enums.UserStatusEnum;
import com.orion.ops.module.infra.service.AuthenticationService;
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

    // TODO 想想看 如何配置化
    // 允许多端登陆
    private final boolean allowMultiDevice = true;
    // 允许凭证续签
    private final boolean allowRefresh = true;
    // 凭证续签最大次数
    private final int maxRefreshCount = 5;
    // 失败锁定次数
    private final int maxFailedLoginCount = 5;

    @Resource
    private ValueCrypto valueCrypto;

    @Resource
    private SystemUserDAO systemUserDAO;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String login(UserLoginRequest request, HttpServletRequest servletRequest) {
        // 登陆前检查
        this.preCheckLogin(request);
        // 获取登陆用户
        LambdaQueryWrapper<SystemUserDO> wrapper = systemUserDAO.wrapper()
                .eq(SystemUserDO::getUsername, request.getUsername());
        SystemUserDO user = systemUserDAO.of(wrapper).only().get();
        // 检查密码
        boolean passwordCorrect = this.checkPassword(request, user);
        Valid.isTrue(passwordCorrect, ErrorMessage.USERNAME_PASSWORD_ERROR);
        // 检查用户状态
        this.checkUserStatus(user.getStatus());
        // 设置上次登录时间
        this.setLastLoginTime(user.getId());
        // 设置缓存
        this.setUserCache(user);
        // 删除登陆失败次数缓存
        redisTemplate.delete(UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(request.getUsername()));
        // 获取登陆 ip
        String remoteAddr = Servlets.getRemoteAddr(servletRequest);
        String location = IpUtils.getLocation(remoteAddr);
        long current = System.currentTimeMillis();
        // 不允许多端登陆
        if (!false) {
            // 无效化其他缓存
            this.invalidOtherDeviceToken(user.getId(), current, remoteAddr, location);
        }
        // 生成 loginToken
        return this.generatorLoginToken(user, current, remoteAddr, location);
    }

    @Override
    public void logout(HttpServletRequest request) {
        // 获取登陆 token
        String loginToken = Kits.getAuthorization(request.getHeader(StandardHttpHeader.AUTHORIZATION));
        if (loginToken == null) {
            return;
        }
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
    public Pair<Long, Long> getLoginTokenPair(String loginToken) {
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
     * 登陆预检查
     *
     * @param request request
     */
    private void preCheckLogin(UserLoginRequest request) {
        // 检查密码长度是否正确 MD5 长度为 32
        if (request.getPassword().length() != Const.MD5_LEN) {
            throw Exceptions.argument(ErrorMessage.USERNAME_PASSWORD_ERROR);
        }
        // 检查登陆失败次数
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
    private boolean checkPassword(UserLoginRequest request, SystemUserDO user) {
        // 密码正确
        if (user != null && user.getPassword().equals(Signatures.md5(request.getPassword()))) {
            return true;
        }
        // 刷新登陆失败缓存
        String failedCountKey = UserCacheKeyDefine.LOGIN_FAILED_COUNT.format(user.getUsername());
        Long failedLoginCount = redisTemplate.opsForValue().increment(failedCountKey);
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
            redisTemplate.opsForValue().set(userInfoKey, JSON.toJSONString(loginUser),
                    UserCacheKeyDefine.USER_INFO.getTimeout(),
                    UserCacheKeyDefine.USER_INFO.getUnit());
        }
        return false;
    }

    /**
     * 检查用户状态
     *
     * @param status status
     */
    private void checkUserStatus(Integer status) {
        if (UserStatusEnum.DISABLED.getStatus().equals(status)) {
            // 禁用状态
            throw Exceptions.argument(ErrorMessage.USER_DISABLED);
        } else if (UserStatusEnum.LOCKED.getStatus().equals(status)) {
            // 锁定状态
            throw Exceptions.argument(ErrorMessage.USER_LOCKED);
        }
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
     * 设置用户缓存
     *
     * @param user user
     */
    private void setUserCache(SystemUserDO user) {
        String userInfoKey = UserCacheKeyDefine.USER_INFO.format(user.getId());
        String userInfoCache = redisTemplate.opsForValue().get(userInfoKey);
        // 缓存存在
        if (userInfoCache != null) {
            return;
        }
        // 设置缓存
        LoginUser loginUser = SystemUserConvert.MAPPER.toLoginUser(user);
        // TODO 查询角色
        redisTemplate.opsForValue().set(userInfoKey, JSON.toJSONString(loginUser),
                UserCacheKeyDefine.USER_INFO.getTimeout(),
                UserCacheKeyDefine.USER_INFO.getUnit());
    }

    /**
     * 无效化其他登陆信息
     *
     * @param id         id
     * @param loginTime  loginTime
     * @param remoteAddr remoteAddr
     * @param location   location
     */
    private void invalidOtherDeviceToken(Long id, long loginTime, String remoteAddr, String location) {
        String loginKey = UserCacheKeyDefine.LOGIN_TOKEN.format(id, "*");
        // 获取登陆信息
        Set<String> loginKeyList = RedisUtils.scanKeys(redisTemplate, loginKey, 100);
        if (!loginKeyList.isEmpty()) {
            // 获取有效登陆信息
            List<LoginTokenDTO> loginTokenInfoList = redisTemplate.opsForValue()
                    .multiGet(loginKeyList)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(s -> JSON.parseObject(s, LoginTokenDTO.class))
                    .filter(s -> LoginTokenStatusEnum.OK.getStatus().equals(s.getStatus()))
                    .collect(Collectors.toList());
            // 修改登陆信息
            for (LoginTokenDTO loginTokenInfo : loginTokenInfoList) {
                String deviceLoginKey = UserCacheKeyDefine.LOGIN_TOKEN.format(id, loginTokenInfo.getLoginTime());
                loginTokenInfo.setStatus(LoginTokenStatusEnum.OTHER_DEVICE.getStatus());
                loginTokenInfo.setLoginTime(loginTime);
                loginTokenInfo.setIp(remoteAddr);
                loginTokenInfo.setLocation(location);
                redisTemplate.opsForValue().set(deviceLoginKey, JSON.toJSONString(loginTokenInfo),
                        UserCacheKeyDefine.LOGIN_TOKEN.getTimeout(),
                        UserCacheKeyDefine.LOGIN_TOKEN.getUnit());
            }
        }
        // 删除续签信息
        if (allowRefresh) {
            String refreshKey = UserCacheKeyDefine.LOGIN_REFRESH.format(id, "*");
            Set<String> refreshKeyList = RedisUtils.scanKeys(redisTemplate, refreshKey, 100);
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
     * @return loginToken
     */
    private String generatorLoginToken(SystemUserDO user, long loginTime,
                                       String remoteAddr, String location) {
        Long id = user.getId();
        // 生成 loginToken
        String loginKey = UserCacheKeyDefine.LOGIN_TOKEN.format(id, loginTime);
        LoginTokenDTO loginValue = LoginTokenDTO.builder()
                .status(LoginTokenStatusEnum.OK.getStatus())
                .ip(remoteAddr)
                .loginTime(loginTime)
                .location(location)
                .build();
        redisTemplate.opsForValue().set(loginKey, JSON.toJSONString(loginValue),
                UserCacheKeyDefine.LOGIN_TOKEN.getTimeout(),
                UserCacheKeyDefine.LOGIN_TOKEN.getUnit());
        // 生成 refreshToken
        if (allowRefresh) {
            String refreshKey = UserCacheKeyDefine.LOGIN_REFRESH.format(id, loginTime);
            redisTemplate.opsForValue().set(refreshKey, "1",
                    UserCacheKeyDefine.LOGIN_REFRESH.getTimeout(),
                    UserCacheKeyDefine.LOGIN_REFRESH.getUnit());
        }
        // 返回token
        return CryptoUtils.encryptBase62(id + ":" + loginTime);
    }

}
