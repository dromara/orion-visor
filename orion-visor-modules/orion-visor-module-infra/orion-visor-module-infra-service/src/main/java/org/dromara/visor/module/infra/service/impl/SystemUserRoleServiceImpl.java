/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.utils.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.security.LoginUser;
import org.dromara.visor.common.security.UserRole;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.module.infra.dao.SystemRoleDAO;
import org.dromara.visor.module.infra.dao.SystemUserDAO;
import org.dromara.visor.module.infra.dao.SystemUserRoleDAO;
import org.dromara.visor.module.infra.define.cache.UserCacheKeyDefine;
import org.dromara.visor.module.infra.entity.domain.SystemRoleDO;
import org.dromara.visor.module.infra.entity.domain.SystemUserDO;
import org.dromara.visor.module.infra.entity.domain.SystemUserRoleDO;
import org.dromara.visor.module.infra.entity.request.user.SystemUserUpdateRoleRequest;
import org.dromara.visor.module.infra.service.DataPermissionService;
import org.dromara.visor.module.infra.service.SystemUserRoleService;
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

    @Resource
    private DataPermissionService dataPermissionService;

    @Override
    public List<Long> getRoleIdListByUserId(Long userId) {
        return systemUserRoleDAO.selectRoleIdByUserId(userId);
    }

    @Override
    public List<Long> getUserIdListByRoleCode(String roleCode) {
        Long roleId = systemRoleDAO.of()
                .createWrapper()
                .eq(SystemRoleDO::getCode, roleCode)
                .then()
                .getOne(SystemRoleDO::getId);
        if (roleId == null) {
            return Lists.empty();
        }
        // 查询用户列表
        return systemUserRoleDAO.selectUserIdByRoleId(roleId);
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
        // 更新用户缓存中的角色
        RedisStrings.<LoginUser>processSetJson(UserCacheKeyDefine.USER_INFO, s -> {
            s.setRoles(Lists.empty());
        }, userId);
        // 清除数据权限缓存
        dataPermissionService.clearUserCache(userId);
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
            List<UserRole> roles = userRoles.stream()
                    .map(role -> new UserRole(role.getId(), role.getCode()))
                    .collect(Collectors.toList());
            s.setRoles(roles);
        }, userId);
        // 清除数据权限缓存
        dataPermissionService.clearUserCache(userId);
        return effect;
    }

    @Override
    @Async("asyncExecutor")
    public void deleteUserCacheRoleAsync(Long roleId, List<Long> userIdList) {
        for (Long userId : userIdList) {
            RedisStrings.<LoginUser>processSetJson(UserCacheKeyDefine.USER_INFO, s -> {
                List<UserRole> roles = s.getRoles();
                if (Lists.isEmpty(roles)) {
                    return;
                }
                // 移除角色
                roles.removeIf(role -> roleId.equals(role.getId()));
            }, userId);
        }
    }

}
