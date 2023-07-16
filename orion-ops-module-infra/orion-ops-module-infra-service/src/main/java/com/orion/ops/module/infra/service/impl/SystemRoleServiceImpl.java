package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.module.infra.convert.SystemRoleConvert;
import com.orion.ops.module.infra.dao.SystemRoleDAO;
import com.orion.ops.module.infra.entity.domain.SystemRoleDO;
import com.orion.ops.module.infra.entity.request.SystemRoleCreateRequest;
import com.orion.ops.module.infra.entity.request.SystemRoleQueryRequest;
import com.orion.ops.module.infra.entity.request.SystemRoleUpdateRequest;
import com.orion.ops.module.infra.entity.vo.SystemRoleVO;
import com.orion.ops.module.infra.service.PermissionService;
import com.orion.ops.module.infra.service.SystemRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private PermissionService permissionService;

    @Override
    public Long createSystemRole(SystemRoleCreateRequest request) {
        // 转换
        SystemRoleDO record = SystemRoleConvert.MAPPER.to(request);
        record.setId(null);
        // 查询名称是否存在
        this.checkNamePresent(record);
        // 查询编码是否存在
        this.checkCodePresent(record);
        // 插入
        int effect = systemRoleDAO.insert(record);
        log.info("SystemRoleService-createSystemRole effect: {}, domain: {}", effect, JSON.toJSONString(record));
        // 设置到缓存
        permissionService.getRoleCache().put(record.getCode(), record);
        return record.getId();
    }

    @Override
    public Integer updateSystemRole(SystemRoleUpdateRequest request) {
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        SystemRoleDO record = systemRoleDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        SystemRoleDO updateRecord = SystemRoleConvert.MAPPER.to(request);
        // 查询数据是否冲突
        // 查询名称是否存在
        this.checkNamePresent(updateRecord);
        // 查询编码是否存在
        this.checkCodePresent(updateRecord);
        // 更新
        int effect = systemRoleDAO.updateById(updateRecord);
        log.info("SystemRoleService-updateSystemRole effect: {}, updateRecord: {}", effect, JSON.toJSONString(updateRecord));
        // 设置到缓存
        SystemRoleDO roleCache = permissionService.getRoleCache().get(record.getCode());
        roleCache.setName(updateRecord.getName());
        return effect;
    }

    @Override
    public SystemRoleVO getSystemRole(Long id) {
        // 查询
        SystemRoleDO record = systemRoleDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return SystemRoleConvert.MAPPER.to(record);
    }

    @Override
    public List<SystemRoleVO> getSystemRoleList(List<Long> idList) {
        // 查询
        List<SystemRoleDO> records = systemRoleDAO.selectBatchIds(idList);
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
                .eq(SystemRoleDO::getName, request.getName())
                .eq(SystemRoleDO::getCode, request.getCode())
                .eq(SystemRoleDO::getStatus, request.getStatus())
                .orderByDesc(SystemRoleDO::getId);
        // 查询
        return systemRoleDAO.of()
                .wrapper(wrapper)
                .page(request)
                .dataGrid(SystemRoleConvert.MAPPER::to);
    }

    @Override
    public Integer deleteSystemRole(Long id) {
        int effect = systemRoleDAO.deleteById(id);
        log.info("SystemRoleService-deleteSystemRole id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer batchDeleteSystemRole(List<Long> idList) {
        int effect = systemRoleDAO.deleteBatchIds(idList);
        log.info("SystemRoleService-batchDeleteSystemRole idList: {}, effect: {}", JSON.toJSONString(idList), effect);
        return effect;
    }

    /**
     * 检测名称是否存在
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
        boolean present = systemRoleDAO.of().wrapper(wrapper).present();
        Valid.isFalse(present, ErrorMessage.NAME_PRESENT);
    }

    /**
     * 检测编码是否存在
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
        boolean present = systemRoleDAO.of().wrapper(wrapper).present();
        Valid.isFalse(present, ErrorMessage.CODE_PRESENT);
    }

}
