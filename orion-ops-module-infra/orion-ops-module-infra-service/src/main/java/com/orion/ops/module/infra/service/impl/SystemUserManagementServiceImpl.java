package com.orion.ops.module.infra.service.impl;

import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Requests;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisStrings;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.infra.dao.SystemUserDAO;
import com.orion.ops.module.infra.define.cache.UserCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import com.orion.ops.module.infra.entity.dto.LoginTokenDTO;
import com.orion.ops.module.infra.entity.dto.LoginTokenIdentityDTO;
import com.orion.ops.module.infra.entity.request.user.UserSessionOfflineRequest;
import com.orion.ops.module.infra.entity.vo.UserSessionVO;
import com.orion.ops.module.infra.enums.LoginTokenStatusEnum;
import com.orion.ops.module.infra.service.SystemUserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户管理 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/22 18:17
 */
@Slf4j
@Service
public class SystemUserManagementServiceImpl implements SystemUserManagementService {

    @Resource
    private SystemUserDAO systemUserDAO;

    @Override
    public List<UserSessionVO> getUserSessionList(Long userId) {
        // 扫描缓存
        Set<String> keys = RedisStrings.scanKeys(UserCacheKeyDefine.LOGIN_TOKEN.format(userId, "*"));
        if (Lists.isEmpty(keys)) {
            return Lists.empty();
        }
        // 查询缓存
        List<LoginTokenDTO> tokens = RedisStrings.getJsonList(keys, UserCacheKeyDefine.LOGIN_TOKEN);
        if (Lists.isEmpty(tokens)) {
            return Lists.empty();
        }
        final boolean isCurrentUser = userId.equals(SecurityUtils.getLoginUserId());
        // 返回
        return tokens.stream()
                .filter(s -> LoginTokenStatusEnum.OK.getStatus().equals(s.getStatus()))
                .map(LoginTokenDTO::getOrigin)
                .map(s -> UserSessionVO.builder()
                        .current(isCurrentUser && s.getLoginTime().equals(SecurityUtils.getLoginTimestamp()))
                        .address(s.getAddress())
                        .location(s.getLocation())
                        .userAgent(s.getUserAgent())
                        .loginTime(new Date(s.getLoginTime()))
                        .build())
                .sorted(Comparator.comparing(UserSessionVO::getCurrent).reversed()
                        .thenComparing(Comparator.comparing(UserSessionVO::getLoginTime).reversed()))
                .collect(Collectors.toList());
    }

    @Override
    public void offlineUserSession(UserSessionOfflineRequest request) {
        Long userId = Valid.notNull(request.getUserId());
        Long timestamp = request.getTimestamp();
        log.info("SystemUserManagementService offlineUserSession userId: {}, timestamp: {}", userId, timestamp);
        // 查询用户
        SystemUserDO user = systemUserDAO.selectById(userId);
        Valid.notNull(user, ErrorMessage.USER_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.USERNAME, user.getUsername());
        // 删除刷新缓存
        RedisStrings.delete(UserCacheKeyDefine.LOGIN_REFRESH.format(userId, request.getTimestamp()));
        // 查询并且覆盖 token
        String tokenKey = UserCacheKeyDefine.LOGIN_TOKEN.format(userId, timestamp);
        LoginTokenDTO tokenInfo = RedisStrings.getJson(tokenKey, UserCacheKeyDefine.LOGIN_TOKEN);
        if (tokenInfo != null) {
            tokenInfo.setStatus(LoginTokenStatusEnum.SESSION_OFFLINE.getStatus());
            LoginTokenIdentityDTO override = new LoginTokenIdentityDTO();
            override.setLoginTime(System.currentTimeMillis());
            // 设置请求信息
            Requests.fillIdentity(override);
            tokenInfo.setOverride(override);
            // 更新 token
            RedisStrings.setJson(tokenKey, UserCacheKeyDefine.LOGIN_TOKEN, tokenInfo);
        }
    }

}
