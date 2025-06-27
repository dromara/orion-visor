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

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.infra.convert.SystemMenuConvert;
import org.dromara.visor.module.infra.convert.SystemUserConvert;
import org.dromara.visor.module.infra.dao.SystemUserDAO;
import org.dromara.visor.module.infra.entity.domain.SystemUserDO;
import org.dromara.visor.module.infra.entity.dto.SystemMenuCacheDTO;
import org.dromara.visor.module.infra.entity.vo.SystemMenuVO;
import org.dromara.visor.module.infra.entity.vo.UserAggregateVO;
import org.dromara.visor.module.infra.entity.vo.UserUpdatePasswordVO;
import org.dromara.visor.module.infra.enums.MenuTypeEnum;
import org.dromara.visor.module.infra.enums.PreferenceTypeEnum;
import org.dromara.visor.module.infra.enums.UpdatePasswordStatusEnum;
import org.dromara.visor.module.infra.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 用户聚合服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/16 14:43
 */
@Slf4j
@Service
public class UserAggregateServiceImpl implements UserAggregateService {

    @Resource
    private SystemUserDAO systemUserDAO;

    @Resource
    private SystemMenuService systemMenuService;

    @Resource
    private PreferenceService preferenceService;

    @Resource
    private TipsService tipsService;

    @Resource
    private UserPermissionService userPermissionService;

    @Override
    public List<SystemMenuVO> getUserMenuList() {
        // 获取菜单
        List<SystemMenuVO> menus = userPermissionService.getUserEnabledMenus()
                .stream()
                .filter(s -> !MenuTypeEnum.FUNCTION.getType().equals(s.getType()))
                .map(SystemMenuConvert.MAPPER::to)
                .collect(Collectors.toList());
        // 构建菜单树
        return systemMenuService.buildSystemMenuTree(menus);
    }

    @SneakyThrows
    @Override
    public UserAggregateVO getUserAggregateInfo() {
        // 获取用户信息
        Long userId = SecurityUtils.getLoginUserId();
        // 获取用户系统偏好
        Future<Object> systemPreference = preferenceService.getPreferenceAsync(userId, PreferenceTypeEnum.SYSTEM);
        // 查询用户信息
        SystemUserDO user = systemUserDAO.selectById(userId);
        // 修改密码信息
        UserUpdatePasswordVO updatePassword = null;
        if (UpdatePasswordStatusEnum.REQUIRED.getStatus().equals(user.getUpdatePasswordStatus())) {
            updatePassword = UserUpdatePasswordVO.builder()
                    .updatePasswordStatus(user.getUpdatePasswordStatus())
                    .updatePasswordReason(user.getUpdatePasswordReason())
                    .build();
        }
        // 获取用户角色
        Map<Long, String> roles = userPermissionService.getUserEnabledRoles();
        // 获取角色权限
        List<SystemMenuCacheDTO> menus = userPermissionService.getRolesEnabledMenus(roles);
        List<String> permissions = userPermissionService.getMenuPermissions(menus);
        // 提示信息
        List<String> tippedKeys = tipsService.getTippedKeys();
        // 组装数据
        return UserAggregateVO.builder()
                .user(SystemUserConvert.MAPPER.toBase(user))
                .roles(roles.values())
                .permissions(permissions)
                .updatePassword(updatePassword)
                .systemPreference(systemPreference.get())
                .tippedKeys(tippedKeys)
                .build();
    }

}
