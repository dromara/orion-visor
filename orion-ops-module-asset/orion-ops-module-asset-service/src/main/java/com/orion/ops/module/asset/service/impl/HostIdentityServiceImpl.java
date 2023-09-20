package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.collect.Lists;
import com.orion.office.excel.writer.exporting.ExcelExport;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.FileNames;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.entity.vo.*;
import com.orion.ops.module.asset.entity.request.host.*;
import com.orion.ops.module.asset.entity.export.*;
import com.orion.ops.module.asset.convert.*;
import com.orion.ops.module.asset.entity.domain.HostIdentityDO;
import com.orion.ops.module.asset.dao.HostIdentityDAO;
import com.orion.ops.module.asset.service.HostIdentityService;
import com.orion.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 主机身份 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Slf4j
@Service
public class HostIdentityServiceImpl implements HostIdentityService {

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    @Override
    public Long createHostIdentity(HostIdentityCreateRequest request) {
        log.info("HostIdentityService-createHostIdentity request: {}", JSON.toJSONString(request));
        // 转换
        HostIdentityDO record = HostIdentityConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkHostIdentityPresent(record);
        // 插入
        int effect = hostIdentityDAO.insert(record);
        log.info("HostIdentityService-createHostIdentity effect: {}", effect);
        return record.getId();
    }

    @Override
    public Integer updateHostIdentityById(HostIdentityUpdateRequest request) {
        log.info("HostIdentityService-updateHostIdentityById request: {}", JSON.toJSONString(request));
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        HostIdentityDO record = hostIdentityDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        HostIdentityDO updateRecord = HostIdentityConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkHostIdentityPresent(updateRecord);
        // 更新
        int effect = hostIdentityDAO.updateById(updateRecord);
        log.info("HostIdentityService-updateHostIdentityById effect: {}", effect);
        return effect;
    }

    @Override
    public Integer updateHostIdentity(HostIdentityQueryRequest query, HostIdentityUpdateRequest update) {
        log.info("HostIdentityService.updateHostIdentity query: {}, update: {}", JSON.toJSONString(query), JSON.toJSONString(update));
        // 条件
        LambdaQueryWrapper<HostIdentityDO> wrapper = this.buildQueryWrapper(query);
        // 转换
        HostIdentityDO updateRecord = HostIdentityConvert.MAPPER.to(update);
        // 更新
        int effect = hostIdentityDAO.update(updateRecord, wrapper);
        log.info("HostIdentityService.updateHostIdentity effect: {}", effect);
        return effect;
    }

    @Override
    public HostIdentityVO getHostIdentityById(Long id) {
        // 查询
        HostIdentityDO record = hostIdentityDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return HostIdentityConvert.MAPPER.to(record);
    }

    @Override
    public List<HostIdentityVO> getHostIdentityByIdList(List<Long> idList) {
        // 查询
        List<HostIdentityDO> records = hostIdentityDAO.selectBatchIds(idList);
        if (records.isEmpty()) {
            return Lists.empty();
        }
        // 转换
        return HostIdentityConvert.MAPPER.to(records);
    }

    @Override
    public List<HostIdentityVO> getHostIdentityList(HostIdentityQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostIdentityDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return hostIdentityDAO.of(wrapper).list(HostIdentityConvert.MAPPER::to);
    }

    @Override
    public Long getHostIdentityCount(HostIdentityQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostIdentityDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return hostIdentityDAO.selectCount(wrapper);
    }

    @Override
    public DataGrid<HostIdentityVO> getHostIdentityPage(HostIdentityQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostIdentityDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return hostIdentityDAO.of(wrapper)
                .page(request)
                .dataGrid(HostIdentityConvert.MAPPER::to);
    }

    @Override
    public Integer deleteHostIdentityById(Long id) {
        log.info("HostIdentityService-deleteHostIdentityById id: {}", id);
        int effect = hostIdentityDAO.deleteById(id);
        log.info("HostIdentityService-deleteHostIdentityById effect: {}", effect);
        return effect;
    }

    @Override
    public Integer batchDeleteHostIdentityByIdList(List<Long> idList) {
        log.info("HostIdentityService-batchDeleteHostIdentityByIdList idList: {}", idList);
        int effect = hostIdentityDAO.deleteBatchIds(idList);
        log.info("HostIdentityService-batchDeleteHostIdentityByIdList effect: {}", effect);
        return effect;
    }

    @Override
    public Integer deleteHostIdentity(HostIdentityQueryRequest request) {
        log.info("HostIdentityService.deleteHostIdentity request: {}", JSON.toJSONString(request));
        // 条件
        LambdaQueryWrapper<HostIdentityDO> wrapper = this.buildQueryWrapper(request);
        // 删除
        int effect = hostIdentityDAO.delete(wrapper);
        log.info("HostIdentityService.deleteHostIdentity effect: {}", effect);
        return effect;
    }

    @Override
    public void exportHostIdentity(HostIdentityQueryRequest request, HttpServletResponse response) throws IOException {
        log.info("HostIdentityService.exportHostIdentity request: {}", JSON.toJSONString(request));
        // 条件
        LambdaQueryWrapper<HostIdentityDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        List<HostIdentityExport> rows = hostIdentityDAO.of(wrapper).list(HostIdentityConvert.MAPPER::toExport);
        log.info("HostIdentityService.exportHostIdentity size: {}", rows.size());
        // 导出
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExcelExport.create(HostIdentityExport.class)
                .addRows(rows)
                .write(out)
                .close();
        // 传输
        Servlets.transfer(response, out.toByteArray(), FileNames.exportName(HostIdentityExport.TITLE));
    }

    /**
     * 检测对象是否存在
     *
     * @param domain domain
     */
    private void checkHostIdentityPresent(HostIdentityDO domain) {
        // 构造条件
        LambdaQueryWrapper<HostIdentityDO> wrapper = hostIdentityDAO.wrapper()
                // 更新时忽略当前记录
                .ne(HostIdentityDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(HostIdentityDO::getName, domain.getName())
                .eq(HostIdentityDO::getUsername, domain.getUsername())
                .eq(HostIdentityDO::getPassword, domain.getPassword())
                .eq(HostIdentityDO::getKeyId, domain.getKeyId());
        // 检查是否存在
        boolean present = hostIdentityDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<HostIdentityDO> buildQueryWrapper(HostIdentityQueryRequest request) {
        return hostIdentityDAO.wrapper()
                .eq(HostIdentityDO::getId, request.getId())
                .eq(HostIdentityDO::getName, request.getName())
                .eq(HostIdentityDO::getUsername, request.getUsername())
                .eq(HostIdentityDO::getPassword, request.getPassword())
                .eq(HostIdentityDO::getKeyId, request.getKeyId());
    }

}
