package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.module.infra.convert.SystemUserRoleConvert;
import com.orion.ops.module.infra.dao.SystemUserRoleDAO;
import com.orion.ops.module.infra.entity.domain.SystemUserRoleDO;
import com.orion.ops.module.infra.entity.request.role.SystemUserRoleCreateRequest;
import com.orion.ops.module.infra.entity.request.role.SystemUserRoleQueryRequest;
import com.orion.ops.module.infra.entity.request.role.SystemUserRoleUpdateRequest;
import com.orion.ops.module.infra.entity.vo.SystemUserRoleVO;
import com.orion.ops.module.infra.service.SystemUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    private SystemUserRoleDAO systemUserRoleDAO;

    @Override
    public Long createSystemUserRole(SystemUserRoleCreateRequest request) {
        // 转换
        SystemUserRoleDO record = SystemUserRoleConvert.MAPPER.to(request);
        record.setId(null);
        // 查询数据是否冲突
        this.checkSystemUserRolePresent(record);
        // 插入
        int effect = systemUserRoleDAO.insert(record);
        log.info("SystemUserRoleService-createSystemUserRole effect: {}, record: {}", effect, JSON.toJSONString(record));
        return record.getId();
    }

    @Override
    public Integer updateSystemUserRole(SystemUserRoleUpdateRequest request) {
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        SystemUserRoleDO record = systemUserRoleDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        SystemUserRoleDO updateRecord = SystemUserRoleConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkSystemUserRolePresent(updateRecord);
        // 更新
        int effect = systemUserRoleDAO.updateById(updateRecord);
        log.info("SystemUserRoleService-updateSystemUserRole effect: {}, updateRecord: {}", effect, JSON.toJSONString(updateRecord));
        return effect;
    }

    @Override
    public SystemUserRoleVO getSystemUserRole(Long id) {
        // 查询
        SystemUserRoleDO record = systemUserRoleDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return SystemUserRoleConvert.MAPPER.to(record);
    }

    @Override
    public List<SystemUserRoleVO> getSystemUserRoleList(List<Long> idList) {
        // 查询
        List<SystemUserRoleDO> records = systemUserRoleDAO.selectBatchIds(idList);
        if (records.isEmpty()) {
            return Lists.empty();
        }
        // 转换
        return SystemUserRoleConvert.MAPPER.to(records);
    }

    @Override
    public DataGrid<SystemUserRoleVO> getSystemUserRolePage(SystemUserRoleQueryRequest request) {
        // 构造条件
        LambdaQueryWrapper<SystemUserRoleDO> wrapper = systemUserRoleDAO.wrapper()
                .eq(SystemUserRoleDO::getId, request.getId())
                .eq(SystemUserRoleDO::getUserId, request.getUserId())
                .eq(SystemUserRoleDO::getRoleId, request.getRoleId())
                .orderByDesc(SystemUserRoleDO::getId);
        // 查询
        return systemUserRoleDAO.of()
                .wrapper(wrapper)
                .page(request)
                .dataGrid(SystemUserRoleConvert.MAPPER::to);
    }

    @Override
    public Integer deleteSystemUserRole(Long id) {
        int effect = systemUserRoleDAO.deleteById(id);
        log.info("SystemUserRoleService-deleteSystemUserRole id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer batchDeleteSystemUserRole(List<Long> idList) {
        int effect = systemUserRoleDAO.deleteBatchIds(idList);
        log.info("SystemUserRoleService-batchDeleteSystemUserRole idList: {}, effect: {}", JSON.toJSONString(idList), effect);
        return effect;
    }

    /**
     * 检测对象是否存在
     *
     * @param domain domain
     */
    private void checkSystemUserRolePresent(SystemUserRoleDO domain) {
        // 构造条件
        LambdaQueryWrapper<SystemUserRoleDO> wrapper = systemUserRoleDAO.wrapper()
                // 更新时忽略当前记录
                .ne(SystemUserRoleDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(SystemUserRoleDO::getUserId, domain.getUserId())
                .eq(SystemUserRoleDO::getRoleId, domain.getRoleId());
        // 检查是否存在
        boolean present = systemUserRoleDAO.of().wrapper(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

}
