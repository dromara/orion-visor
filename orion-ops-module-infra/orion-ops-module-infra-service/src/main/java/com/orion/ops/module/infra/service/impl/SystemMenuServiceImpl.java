package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.module.infra.convert.SystemMenuConvert;
import com.orion.ops.module.infra.dao.SystemMenuDAO;
import com.orion.ops.module.infra.entity.domain.SystemMenuDO;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuCreateRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuQueryRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuUpdateRequest;
import com.orion.ops.module.infra.entity.vo.SystemMenuVO;
import com.orion.ops.module.infra.service.SystemMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-17 11:39
 */
@Slf4j
@Service
public class SystemMenuServiceImpl implements SystemMenuService {

    @Resource
    private SystemMenuDAO systemMenuDAO;

    @Override
    public Long createSystemMenu(SystemMenuCreateRequest request) {
        // 转换
        SystemMenuDO record = SystemMenuConvert.MAPPER.to(request);
        record.setId(null);
        // 查询数据是否冲突
        this.checkSystemMenuPresent(record);
        // 插入
        int effect = systemMenuDAO.insert(record);
        log.info("SystemMenuService-createSystemMenu effect: {}, record: {}", effect, JSON.toJSONString(record));
        return record.getId();
    }

    @Override
    public Integer updateSystemMenu(SystemMenuUpdateRequest request) {
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        SystemMenuDO record = systemMenuDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        SystemMenuDO updateRecord = SystemMenuConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkSystemMenuPresent(updateRecord);
        // 更新
        int effect = systemMenuDAO.updateById(updateRecord);
        log.info("SystemMenuService-updateSystemMenu effect: {}, updateRecord: {}", effect, JSON.toJSONString(updateRecord));
        return effect;
    }

    @Override
    public SystemMenuVO getSystemMenu(Long id) {
        // 查询
        SystemMenuDO record = systemMenuDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return SystemMenuConvert.MAPPER.to(record);
    }

    @Override
    public List<SystemMenuVO> getSystemMenuList(List<Long> idList) {
        // 查询
        List<SystemMenuDO> records = systemMenuDAO.selectBatchIds(idList);
        if (records.isEmpty()) {
            return Lists.empty();
        }
        // 转换
        return SystemMenuConvert.MAPPER.to(records);
    }

    @Override
    public DataGrid<SystemMenuVO> getSystemMenuPage(SystemMenuQueryRequest request) {
        // 构造条件
        LambdaQueryWrapper<SystemMenuDO> wrapper = systemMenuDAO.wrapper()
                .eq(SystemMenuDO::getId, request.getId())
                .eq(SystemMenuDO::getParentId, request.getParentId())
                .eq(SystemMenuDO::getName, request.getName())
                .eq(SystemMenuDO::getPermission, request.getPermission())
                .eq(SystemMenuDO::getType, request.getType())
                .eq(SystemMenuDO::getSort, request.getSort())
                .eq(SystemMenuDO::getStatus, request.getStatus())
                .eq(SystemMenuDO::getCache, request.getCache())
                .eq(SystemMenuDO::getIcon, request.getIcon())
                .eq(SystemMenuDO::getPath, request.getPath())
                .eq(SystemMenuDO::getComponentName, request.getComponentName())
                .eq(SystemMenuDO::getComponent, request.getComponent())
                .orderByDesc(SystemMenuDO::getId);
        // 查询
        return systemMenuDAO.of()
                .wrapper(wrapper)
                .page(request)
                .dataGrid(SystemMenuConvert.MAPPER::to);
    }

    @Override
    public Integer deleteSystemMenu(Long id) {
        int effect = systemMenuDAO.deleteById(id);
        log.info("SystemMenuService-deleteSystemMenu id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer batchDeleteSystemMenu(List<Long> idList) {
        int effect = systemMenuDAO.deleteBatchIds(idList);
        log.info("SystemMenuService-batchDeleteSystemMenu idList: {}, effect: {}", JSON.toJSONString(idList), effect);
        return effect;
    }

    /**
     * 检测对象是否存在
     *
     * @param domain domain
     */
    private void checkSystemMenuPresent(SystemMenuDO domain) {
        // 构造条件
        LambdaQueryWrapper<SystemMenuDO> wrapper = systemMenuDAO.wrapper()
                // 更新时忽略当前记录
                .ne(SystemMenuDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(SystemMenuDO::getParentId, domain.getParentId())
                .eq(SystemMenuDO::getName, domain.getName())
                .eq(SystemMenuDO::getPermission, domain.getPermission())
                .eq(SystemMenuDO::getType, domain.getType())
                .eq(SystemMenuDO::getSort, domain.getSort())
                .eq(SystemMenuDO::getStatus, domain.getStatus())
                .eq(SystemMenuDO::getCache, domain.getCache())
                .eq(SystemMenuDO::getIcon, domain.getIcon())
                .eq(SystemMenuDO::getPath, domain.getPath())
                .eq(SystemMenuDO::getComponentName, domain.getComponentName())
                .eq(SystemMenuDO::getComponent, domain.getComponent());
        // 检查是否存在
        boolean present = systemMenuDAO.of().wrapper(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

}
