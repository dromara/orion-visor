package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.entity.request.*;
import com.orion.ops.module.infra.convert.*;
import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import com.orion.ops.module.infra.dao.SystemUserDAO;
import com.orion.ops.module.infra.service.SystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 22:46
 */
@Slf4j
@Service
public class SystemUserServiceImpl implements SystemUserService {

    @Resource
    private SystemUserDAO systemUserDAO;

    @Override
    public Long createSystemUser(SystemUserCreateRequest request) {
        // 转换
        SystemUserDO record = SystemUserConvert.MAPPER.to(request);
        record.setId(null);
        // 查询数据是否冲突
        this.checkSystemUserPresent(record);
        // 插入
        int effect = systemUserDAO.insert(record);
        log.info("SystemUserService-createSystemUser effect: {}, record: {}", effect, JSON.toJSONString(record));
        return record.getId();
    }

    @Override
    public Integer updateSystemUser(SystemUserUpdateRequest request) {
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        SystemUserDO record = systemUserDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        SystemUserDO updateRecord = SystemUserConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkSystemUserPresent(updateRecord);
        // 更新
        int effect = systemUserDAO.updateById(updateRecord);
        log.info("SystemUserService-updateSystemUser effect: {}, updateRecord: {}", effect, JSON.toJSONString(updateRecord));
        return effect;
    }

    @Override
    public SystemUserVO getSystemUser(Long id) {
        // 查询
        SystemUserDO record = systemUserDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return SystemUserConvert.MAPPER.to(record);
    }

    @Override
    public List<SystemUserVO> getSystemUserList(List<Long> idList) {
        // 查询
        List<SystemUserDO> records = systemUserDAO.selectBatchIds(idList);
        if (records.isEmpty()) {
            return Lists.empty();
        }
        // 转换
        return SystemUserConvert.MAPPER.to(records);
    }

    @Override
    public DataGrid<SystemUserVO> getSystemUserPage(SystemUserQueryRequest request) {
        // 构造条件
        LambdaQueryWrapper<SystemUserDO> wrapper = systemUserDAO.wrapper()
                .eq(SystemUserDO::getId, request.getId())
                .eq(SystemUserDO::getUsername, request.getUsername())
                .eq(SystemUserDO::getPassword, request.getPassword())
                .eq(SystemUserDO::getNickname, request.getNickname())
                .eq(SystemUserDO::getAvatar, request.getAvatar())
                .eq(SystemUserDO::getMobile, request.getMobile())
                .eq(SystemUserDO::getEmail, request.getEmail())
                .eq(SystemUserDO::getStatus, request.getStatus())
                .eq(SystemUserDO::getLastLoginTime, request.getLastLoginTime())
                .orderByDesc(SystemUserDO::getId);
        // 查询
        return systemUserDAO.of()
                .wrapper(wrapper)
                .page(request)
                .dataGrid(SystemUserConvert.MAPPER::to);
    }

    @Override
    public Integer deleteSystemUser(Long id) {
        int effect = systemUserDAO.deleteById(id);
        log.info("SystemUserService-deleteSystemUser id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer batchDeleteSystemUser(List<Long> idList) {
        int effect = systemUserDAO.deleteBatchIds(idList);
        log.info("SystemUserService-batchDeleteSystemUser idList: {}, effect: {}", JSON.toJSONString(idList), effect);
        return effect;
    }

    /**
     * 检测对象是否存在
     *
     * @param domain domain
     */
    private void checkSystemUserPresent(SystemUserDO domain) {
        // 构造条件
        LambdaQueryWrapper<SystemUserDO> wrapper = systemUserDAO.wrapper()
                // 更新时忽略当前记录
                .ne(SystemUserDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(SystemUserDO::getUsername, domain.getUsername())
                .eq(SystemUserDO::getPassword, domain.getPassword())
                .eq(SystemUserDO::getNickname, domain.getNickname())
                .eq(SystemUserDO::getAvatar, domain.getAvatar())
                .eq(SystemUserDO::getMobile, domain.getMobile())
                .eq(SystemUserDO::getEmail, domain.getEmail())
                .eq(SystemUserDO::getStatus, domain.getStatus())
                .eq(SystemUserDO::getLastLoginTime, domain.getLastLoginTime());
        // 检查是否存在
        boolean present = systemUserDAO.of().wrapper(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

}
