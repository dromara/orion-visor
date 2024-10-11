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
package com.orion.visor.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.collect.Lists;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.module.infra.convert.SystemRoleConvert;
import com.orion.visor.module.infra.dao.SystemRoleDAO;
import com.orion.visor.module.infra.dao.SystemRoleMenuDAO;
import com.orion.visor.module.infra.dao.SystemUserRoleDAO;
import com.orion.visor.module.infra.define.RoleDefine;
import com.orion.visor.module.infra.entity.domain.SystemRoleDO;
import com.orion.visor.module.infra.entity.request.role.SystemRoleCreateRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleQueryRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleStatusRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleUpdateRequest;
import com.orion.visor.module.infra.entity.vo.SystemRoleVO;
import com.orion.visor.module.infra.enums.RoleStatusEnum;
import com.orion.visor.module.infra.service.DataPermissionService;
import com.orion.visor.module.infra.service.UserPermissionService;
import com.orion.visor.module.infra.service.SystemRoleService;
import com.orion.visor.module.infra.service.SystemUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Slf4j
@Service
public class SystemRoleServiceImpl implements SystemRoleService {

    @Resource
    private SystemRoleDAO systemRoleDAO;

    @Resource
    private SystemUserRoleDAO systemUserRoleDAO;

    @Resource
    private SystemRoleMenuDAO systemRoleMenuDAO;

    @Resource
    private UserPermissionService userPermissionService;

    @Resource
    private SystemUserRoleService systemUserRoleService;

    @Resource
    private DataPermissionService dataPermissionService;

    @Override
    public Long createSystemRole(SystemRoleCreateRequest request) {
        // 转换
        SystemRoleDO record = SystemRoleConvert.MAPPER.to(request);
        // 查询名称是否存在
        this.checkNamePresent(record);
        // 查询编码是否存在
        this.checkCodePresent(record);
        // 插入
        record.setStatus(RoleStatusEnum.ENABLED.getStatus());
        int effect = systemRoleDAO.insert(record);
        log.info("SystemRoleService-createSystemRole effect: {}, domain: {}", effect, JSON.toJSONString(record));
        // 设置到缓存
        userPermissionService.getRoleCache().put(record.getId(), record);
        return record.getId();
    }

    @Override
    public Integer updateSystemRoleById(SystemRoleUpdateRequest request) {
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        SystemRoleDO record = systemRoleDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.ROLE_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.CODE, record.getCode());
        // 转换
        SystemRoleDO updateRecord = SystemRoleConvert.MAPPER.to(request);
        // 查询名称是否存在
        this.checkNamePresent(updateRecord);
        // 更新
        int effect = systemRoleDAO.updateById(updateRecord);
        log.info("SystemRoleService-updateSystemRoleById effect: {}, updateRecord: {}", effect, JSON.toJSONString(updateRecord));
        // 设置到缓存
        SystemRoleDO roleCache = userPermissionService.getRoleCache().get(id);
        roleCache.setName(updateRecord.getName());
        return effect;
    }

    @Override
    public Integer updateRoleStatus(SystemRoleStatusRequest request) {
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        SystemRoleDO record = systemRoleDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 检查是否为管理员角色
        Valid.isTrue(!RoleDefine.isAdmin(record.getCode()), ErrorMessage.UNABLE_OPERATE_ADMIN_ROLE);
        // 转换
        SystemRoleDO updateRecord = SystemRoleConvert.MAPPER.to(request);
        Integer status = updateRecord.getStatus();
        RoleStatusEnum statusEnum = Valid.valid(RoleStatusEnum::of, status);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.CODE, record.getCode());
        OperatorLogs.add(OperatorLogs.NAME, record.getName());
        OperatorLogs.add(OperatorLogs.STATUS_NAME, statusEnum.name());
        // 更新
        int effect = systemRoleDAO.updateById(updateRecord);
        log.info("SystemRoleService-updateRoleStatus effect: {}, updateRecord: {}", effect, JSON.toJSONString(updateRecord));
        // 修改本地缓存状态
        SystemRoleDO roleCache = userPermissionService.getRoleCache().get(id);
        roleCache.setStatus(status);
        // 删除数据权限缓存
        dataPermissionService.clearRoleCache(id);
        return effect;
    }

    @Override
    public SystemRoleVO getSystemRoleById(Long id) {
        // 查询角色
        SystemRoleDO record = systemRoleDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.ROLE_ABSENT);
        // 转换
        return SystemRoleConvert.MAPPER.to(record);
    }

    @Override
    public List<SystemRoleVO> getSystemRoleByIdList() {
        // 查询
        List<SystemRoleDO> records = systemRoleDAO.selectList(null);
        if (records.isEmpty()) {
            return Lists.empty();
        }
        // 转换
        return SystemRoleConvert.MAPPER.to(records);
    }

    @Override
    public DataGrid<SystemRoleVO> getSystemRolePage(SystemRoleQueryRequest request) {
        // 构造条件
        LambdaQueryWrapper<SystemRoleDO> wrapper = systemRoleDAO.wrapper()
                .eq(SystemRoleDO::getId, request.getId())
                .like(SystemRoleDO::getName, request.getName())
                .eq(SystemRoleDO::getCode, request.getCode())
                .eq(SystemRoleDO::getStatus, request.getStatus());
        // 查询
        return systemRoleDAO.of(wrapper)
                .page(request)
                .dataGrid(SystemRoleConvert.MAPPER::to);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteSystemRoleById(Long id) {
        // 查询角色
        SystemRoleDO record = systemRoleDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        String code = record.getCode();
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.CODE, code);
        OperatorLogs.add(OperatorLogs.NAME, record.getName());
        // 检查是否为管理员角色
        Valid.isTrue(!RoleDefine.isAdmin(code), ErrorMessage.UNABLE_OPERATE_ADMIN_ROLE);
        // 删除角色
        int effect = systemRoleDAO.deleteById(id);
        log.info("SystemRoleService-deleteSystemRole id: {}, effect: {}", id, effect);
        // 查询用户角色关联
        List<Long> userIdList = systemUserRoleDAO.selectUserIdByRoleId(id);
        // 删除用户角色关联
        effect += systemUserRoleDAO.deleteByRoleId(id);
        // 删除角色菜单关联
        effect += systemRoleMenuDAO.deleteByRoleId(id);
        // 删除角色缓存
        userPermissionService.getRoleCache().remove(id);
        // 删除菜单缓存
        userPermissionService.getRoleMenuCache().remove(id);
        // 删除用户缓存中的角色
        systemUserRoleService.deleteUserCacheRoleAsync(id, userIdList);
        // 删除数据权限缓存
        dataPermissionService.deleteByRoleId(id);
        return effect;
    }

    /**
     * 检查名称是否存在
     *
     * @param domain domain
     */
    private void checkNamePresent(SystemRoleDO domain) {
        // 构造条件
        LambdaQueryWrapper<SystemRoleDO> wrapper = systemRoleDAO.wrapper()
                // 更新时忽略当前记录
                .ne(SystemRoleDO::getId, domain.getId())
                .eq(SystemRoleDO::getName, domain.getName());
        // 检查是否存在
        boolean present = systemRoleDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.NAME_PRESENT);
    }

    /**
     * 检查编码是否存在
     *
     * @param domain domain
     */
    private void checkCodePresent(SystemRoleDO domain) {
        // 构造条件
        LambdaQueryWrapper<SystemRoleDO> wrapper = systemRoleDAO.wrapper()
                // 更新时忽略当前记录
                .ne(SystemRoleDO::getId, domain.getId())
                .eq(SystemRoleDO::getCode, domain.getCode());
        // 检查是否存在
        boolean present = systemRoleDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.CODE_PRESENT);
    }

}
