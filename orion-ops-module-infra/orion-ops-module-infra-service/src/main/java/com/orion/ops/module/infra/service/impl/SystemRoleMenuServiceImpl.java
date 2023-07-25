package com.orion.ops.module.infra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.ErrorCode;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.mybatis.core.query.Conditions;
import com.orion.ops.module.infra.convert.SystemMenuConvert;
import com.orion.ops.module.infra.dao.SystemMenuDAO;
import com.orion.ops.module.infra.dao.SystemRoleDAO;
import com.orion.ops.module.infra.dao.SystemRoleMenuDAO;
import com.orion.ops.module.infra.entity.domain.SystemMenuDO;
import com.orion.ops.module.infra.entity.domain.SystemRoleDO;
import com.orion.ops.module.infra.entity.domain.SystemRoleMenuDO;
import com.orion.ops.module.infra.entity.dto.SystemMenuCacheDTO;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuBindRequest;
import com.orion.ops.module.infra.service.PermissionService;
import com.orion.ops.module.infra.service.SystemRoleMenuService;
import lombok.extern.slf4j.Slf4j;
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
    private PermissionService permissionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer bindRoleMenu(SystemMenuBindRequest request) {
        Long roleId = request.getRoleId();
        List<Long> menuIdList = request.getIdList();
        // 检查角色是否存在
        SystemRoleDO role = Valid.notNull(systemRoleDAO.selectById(roleId), ErrorMessage.ROLE_ABSENT);
        // 查询菜单列表
        List<SystemMenuDO> menuList = systemMenuDAO.selectBatchIds(menuIdList);
        if (menuIdList.size() != menuList.size()) {
            throw ErrorCode.CONFLICT.exception();
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
        Map<String, List<SystemMenuCacheDTO>> cache = permissionService.getRoleMenuCache();
        List<SystemMenuCacheDTO> roleCache = cache.get(role.getCode());
        if (Lists.isEmpty(roleCache)) {
            roleCache = new ArrayList<>();
            cache.put(role.getCode(), roleCache);
        }
        roleCache.clear();
        roleCache.addAll(SystemMenuConvert.MAPPER.toCache(menuList));
        return effect;
    }

}
