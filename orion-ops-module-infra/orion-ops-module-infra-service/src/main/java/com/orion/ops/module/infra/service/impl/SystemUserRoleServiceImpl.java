package com.orion.ops.module.infra.service.impl;

import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogs;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisStrings;
import com.orion.ops.module.infra.dao.SystemRoleDAO;
import com.orion.ops.module.infra.dao.SystemUserDAO;
import com.orion.ops.module.infra.dao.SystemUserRoleDAO;
import com.orion.ops.module.infra.define.cache.UserCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.SystemRoleDO;
import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import com.orion.ops.module.infra.entity.domain.SystemUserRoleDO;
import com.orion.ops.module.infra.entity.request.user.SystemUserUpdateRoleRequest;
import com.orion.ops.module.infra.service.SystemUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色关联 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 22:46
 */
@Slf4j
@Service
public class SystemUserRoleServiceImpl implements SystemUserRoleService {

    @Resource
    private SystemUserDAO systemUserDAO;

    @Resource
    private SystemRoleDAO systemRoleDAO;

    @Resource
    private SystemUserRoleDAO systemUserRoleDAO;

    @Override
    public List<Long> getUserRoleIdList(Long userId) {
        return systemUserRoleDAO.selectRoleIdByUserId(userId);
    }

    @Override
    public Integer deleteUserRoles(SystemUserUpdateRoleRequest request) {
        Long userId = request.getId();
        // 查询用户
        SystemUserDO user = systemUserDAO.selectById(userId);
        Valid.notNull(user, ErrorMessage.USER_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.USERNAME, user.getUsername());
        // 删除用户关联
        int effect = systemUserRoleDAO.deleteByUserId(userId);
        // 更新缓存中的角色
        RedisStrings.<LoginUser>processSetJson(UserCacheKeyDefine.USER_INFO, s -> {
            s.setRoles(Lists.empty());
        }, userId);
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUserRoles(SystemUserUpdateRoleRequest request) {
        Long userId = request.getId();
        List<Long> roleIdList = request.getRoleIdList();
        // 查询用户
        SystemUserDO user = systemUserDAO.selectById(userId);
        Valid.notNull(user, ErrorMessage.USER_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.USERNAME, user.getUsername());
        // 查询角色
        List<SystemRoleDO> userRoles = systemRoleDAO.selectBatchIds(roleIdList);
        // 检查角色是否存在
        if (roleIdList.size() != userRoles.size()) {
            // 有不存在的角色
            List<Long> userRoleIdLists = userRoles.stream()
                    .map(SystemRoleDO::getId)
                    .collect(Collectors.toList());
            for (Long roleId : roleIdList) {
                Valid.in(roleId, userRoleIdLists, ErrorMessage.ROLE_CODE_ABSENT, roleId);
            }
        }
        // 删除用户角色关联
        int effect = systemUserRoleDAO.deleteByUserId(userId);
        // 重新添加用户角色关联
        List<SystemUserRoleDO> addUserRoles = userRoles.stream().map(s -> {
            SystemUserRoleDO ur = new SystemUserRoleDO();
            ur.setUserId(userId);
            ur.setRoleId(s.getId());
            return ur;
        }).collect(Collectors.toList());
        systemUserRoleDAO.insertBatch(addUserRoles);
        effect += addUserRoles.size();
        // 更新缓存中的角色
        RedisStrings.<LoginUser>processSetJson(UserCacheKeyDefine.USER_INFO, s -> {
            List<String> roleCodeList = userRoles.stream()
                    .map(SystemRoleDO::getCode)
                    .collect(Collectors.toList());
            s.setRoles(roleCodeList);
        }, userId);
        return effect;
    }

    @Override
    @Async("asyncExecutor")
    public void asyncDeleteUserCacheRole(String roleCode, List<Long> userIdList) {
        for (Long userId : userIdList) {
            RedisStrings.<LoginUser>processSetJson(UserCacheKeyDefine.USER_INFO, s -> {
                List<String> roles = s.getRoles();
                if (Lists.isEmpty(roles)) {
                    return;
                }
                // 移除角色
                roles.remove(roleCode);
            }, userId);
        }
    }

}
