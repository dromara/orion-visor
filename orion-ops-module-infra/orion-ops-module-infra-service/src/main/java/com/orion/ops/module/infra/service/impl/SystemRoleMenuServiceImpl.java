package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.module.infra.convert.SystemRoleMenuConvert;
import com.orion.ops.module.infra.dao.SystemRoleMenuDAO;
import com.orion.ops.module.infra.entity.domain.SystemRoleMenuDO;
import com.orion.ops.module.infra.entity.request.menu.SystemRoleMenuCreateRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemRoleMenuQueryRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemRoleMenuUpdateRequest;
import com.orion.ops.module.infra.entity.vo.SystemRoleMenuVO;
import com.orion.ops.module.infra.service.SystemRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    private SystemRoleMenuDAO systemRoleMenuDAO;

    @Override
    public Long createSystemRoleMenu(SystemRoleMenuCreateRequest request) {
        // 转换
        SystemRoleMenuDO record = SystemRoleMenuConvert.MAPPER.to(request);
        record.setId(null);
        // 查询数据是否冲突
        this.checkSystemRoleMenuPresent(record);
        // 插入
        int effect = systemRoleMenuDAO.insert(record);
        log.info("SystemRoleMenuService-createSystemRoleMenu effect: {}, record: {}", effect, JSON.toJSONString(record));
        return record.getId();
    }

    @Override
    public Integer updateSystemRoleMenu(SystemRoleMenuUpdateRequest request) {
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        SystemRoleMenuDO record = systemRoleMenuDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        SystemRoleMenuDO updateRecord = SystemRoleMenuConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkSystemRoleMenuPresent(updateRecord);
        // 更新
        int effect = systemRoleMenuDAO.updateById(updateRecord);
        log.info("SystemRoleMenuService-updateSystemRoleMenu effect: {}, updateRecord: {}", effect, JSON.toJSONString(updateRecord));
        return effect;
    }

    @Override
    public SystemRoleMenuVO getSystemRoleMenu(Long id) {
        // 查询
        SystemRoleMenuDO record = systemRoleMenuDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return SystemRoleMenuConvert.MAPPER.to(record);
    }

    @Override
    public List<SystemRoleMenuVO> getSystemRoleMenuList(List<Long> idList) {
        // 查询
        List<SystemRoleMenuDO> records = systemRoleMenuDAO.selectBatchIds(idList);
        if (records.isEmpty()) {
            return Lists.empty();
        }
        // 转换
        return SystemRoleMenuConvert.MAPPER.to(records);
    }

    @Override
    public DataGrid<SystemRoleMenuVO> getSystemRoleMenuPage(SystemRoleMenuQueryRequest request) {
        // 构造条件
        LambdaQueryWrapper<SystemRoleMenuDO> wrapper = systemRoleMenuDAO.wrapper()
                .eq(SystemRoleMenuDO::getId, request.getId())
                .eq(SystemRoleMenuDO::getRoleId, request.getRoleId())
                .eq(SystemRoleMenuDO::getMenuId, request.getMenuId())
                .orderByDesc(SystemRoleMenuDO::getId);
        // 查询
        return systemRoleMenuDAO.of()
                .wrapper(wrapper)
                .page(request)
                .dataGrid(SystemRoleMenuConvert.MAPPER::to);
    }

    @Override
    public Integer deleteSystemRoleMenu(Long id) {
        int effect = systemRoleMenuDAO.deleteById(id);
        log.info("SystemRoleMenuService-deleteSystemRoleMenu id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer batchDeleteSystemRoleMenu(List<Long> idList) {
        int effect = systemRoleMenuDAO.deleteBatchIds(idList);
        log.info("SystemRoleMenuService-batchDeleteSystemRoleMenu idList: {}, effect: {}", JSON.toJSONString(idList), effect);
        return effect;
    }

    /**
     * 检测对象是否存在
     *
     * @param domain domain
     */
    private void checkSystemRoleMenuPresent(SystemRoleMenuDO domain) {
        // 构造条件
        LambdaQueryWrapper<SystemRoleMenuDO> wrapper = systemRoleMenuDAO.wrapper()
                // 更新时忽略当前记录
                .ne(SystemRoleMenuDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(SystemRoleMenuDO::getRoleId, domain.getRoleId())
                .eq(SystemRoleMenuDO::getMenuId, domain.getMenuId());
        // 检查是否存在
        boolean present = systemRoleMenuDAO.of().wrapper(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

}
