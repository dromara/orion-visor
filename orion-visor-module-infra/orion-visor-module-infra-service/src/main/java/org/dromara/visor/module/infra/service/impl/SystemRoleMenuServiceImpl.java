/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.common.constant.ErrorCode;
import org.dromara.visor.framework.common.constant.ErrorMessage;
import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.module.infra.convert.SystemMenuConvert;
import org.dromara.visor.module.infra.dao.SystemMenuDAO;
import org.dromara.visor.module.infra.dao.SystemRoleDAO;
import org.dromara.visor.module.infra.dao.SystemRoleMenuDAO;
import org.dromara.visor.module.infra.entity.domain.SystemMenuDO;
import org.dromara.visor.module.infra.entity.domain.SystemRoleDO;
import org.dromara.visor.module.infra.entity.domain.SystemRoleMenuDO;
import org.dromara.visor.module.infra.entity.dto.SystemMenuCacheDTO;
import org.dromara.visor.module.infra.entity.request.menu.SystemRoleGrantMenuRequest;
import org.dromara.visor.module.infra.service.SystemRoleMenuService;
import org.dromara.visor.module.infra.service.UserPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色菜单关联 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 22:46
 */
@Slf4j
@Service
public class SystemRoleMenuServiceImpl implements SystemRoleMenuService {

    @Resource
    private SystemRoleDAO systemRoleDAO;

    @Resource
    private SystemMenuDAO systemMenuDAO;

    @Resource
    private SystemRoleMenuDAO systemRoleMenuDAO;

    @Resource
    private UserPermissionService userPermissionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer grantRoleMenu(SystemRoleGrantMenuRequest request) {
        Long roleId = request.getRoleId();
        List<Long> menuIdList = request.getMenuIdList();
        // 检查角色是否存在
        SystemRoleDO role = Valid.notNull(systemRoleDAO.selectById(roleId), ErrorMessage.ROLE_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.NAME, role.getName());
        OperatorLogs.add(OperatorLogs.CODE, role.getCode());
        // 查询菜单列表
        List<SystemMenuDO> menuList;
        if (menuIdList.isEmpty()) {
            menuList = Lists.empty();
        } else {
            menuList = systemMenuDAO.selectBatchIds(menuIdList);
            if (menuIdList.size() != menuList.size()) {
                throw ErrorCode.CONFLICT.exception();
            }
        }
        // 查询角色菜单
        List<Long> beforeMenuIdList = systemRoleMenuDAO.of()
                .wrapper(Conditions.eq(SystemRoleMenuDO::getRoleId, roleId))
                .list(SystemRoleMenuDO::getMenuId);
        // 计算需要删除的
        List<Long> deleteMenuIdList = beforeMenuIdList.stream()
                .filter(s -> !menuIdList.contains(s))
                .collect(Collectors.toList());
        // 计算需要新增的
        List<Long> insertMenuIdList = menuIdList.stream()
                .filter(s -> !beforeMenuIdList.contains(s))
                .collect(Collectors.toList());
        int effect = 0;
        // 删除
        if (!deleteMenuIdList.isEmpty()) {
            LambdaQueryWrapper<SystemRoleMenuDO> deleteWrapper = systemRoleMenuDAO.wrapper()
                    .eq(SystemRoleMenuDO::getRoleId, roleId)
                    .in(SystemRoleMenuDO::getMenuId, deleteMenuIdList);
            effect += systemRoleMenuDAO.delete(deleteWrapper);
        }
        // 插入
        if (!insertMenuIdList.isEmpty()) {
            List<SystemRoleMenuDO> insertRecords = insertMenuIdList.stream()
                    .map(s -> {
                        SystemRoleMenuDO record = new SystemRoleMenuDO();
                        record.setRoleId(roleId);
                        record.setMenuId(s);
                        return record;
                    }).collect(Collectors.toList());
            systemRoleMenuDAO.insertBatch(insertRecords);
            effect += insertMenuIdList.size();
        }
        // 更新缓存
        Map<Long, List<SystemMenuCacheDTO>> cache = userPermissionService.getRoleMenuCache();
        List<SystemMenuCacheDTO> roleCache = cache.computeIfAbsent(roleId, s -> new ArrayList<>());
        roleCache.clear();
        roleCache.addAll(SystemMenuConvert.MAPPER.toCache(menuList));
        return effect;
    }

    @Override
    public List<Long> getRoleMenuIdList(Long roleId) {
        return systemRoleMenuDAO.selectList(Conditions.eq(SystemRoleMenuDO::getRoleId, roleId))
                .stream()
                .map(SystemRoleMenuDO::getMenuId)
                .collect(Collectors.toList());
    }

}
