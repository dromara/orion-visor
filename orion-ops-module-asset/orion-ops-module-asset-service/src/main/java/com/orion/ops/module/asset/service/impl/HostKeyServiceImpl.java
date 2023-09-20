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
import com.orion.ops.module.asset.entity.domain.HostKeyDO;
import com.orion.ops.module.asset.dao.HostKeyDAO;
import com.orion.ops.module.asset.service.HostKeyService;
import com.orion.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 主机秘钥 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Slf4j
@Service
public class HostKeyServiceImpl implements HostKeyService {

    @Resource
    private HostKeyDAO hostKeyDAO;

    @Override
    public Long createHostKey(HostKeyCreateRequest request) {
        log.info("HostKeyService-createHostKey request: {}", JSON.toJSONString(request));
        // 转换
        HostKeyDO record = HostKeyConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkHostKeyPresent(record);
        // 插入
        int effect = hostKeyDAO.insert(record);
        log.info("HostKeyService-createHostKey effect: {}", effect);
        return record.getId();
    }

    @Override
    public Integer updateHostKeyById(HostKeyUpdateRequest request) {
        log.info("HostKeyService-updateHostKeyById request: {}", JSON.toJSONString(request));
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        HostKeyDO record = hostKeyDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        HostKeyDO updateRecord = HostKeyConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkHostKeyPresent(updateRecord);
        // 更新
        int effect = hostKeyDAO.updateById(updateRecord);
        log.info("HostKeyService-updateHostKeyById effect: {}", effect);
        return effect;
    }

    @Override
    public Integer updateHostKey(HostKeyQueryRequest query, HostKeyUpdateRequest update) {
        log.info("HostKeyService.updateHostKey query: {}, update: {}", JSON.toJSONString(query), JSON.toJSONString(update));
        // 条件
        LambdaQueryWrapper<HostKeyDO> wrapper = this.buildQueryWrapper(query);
        // 转换
        HostKeyDO updateRecord = HostKeyConvert.MAPPER.to(update);
        // 更新
        int effect = hostKeyDAO.update(updateRecord, wrapper);
        log.info("HostKeyService.updateHostKey effect: {}", effect);
        return effect;
    }

    @Override
    public HostKeyVO getHostKeyById(Long id) {
        // 查询
        HostKeyDO record = hostKeyDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return HostKeyConvert.MAPPER.to(record);
    }

    @Override
    public List<HostKeyVO> getHostKeyByIdList(List<Long> idList) {
        // 查询
        List<HostKeyDO> records = hostKeyDAO.selectBatchIds(idList);
        if (records.isEmpty()) {
            return Lists.empty();
        }
        // 转换
        return HostKeyConvert.MAPPER.to(records);
    }

    @Override
    public List<HostKeyVO> getHostKeyList(HostKeyQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostKeyDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return hostKeyDAO.of(wrapper).list(HostKeyConvert.MAPPER::to);
    }

    @Override
    public Long getHostKeyCount(HostKeyQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostKeyDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return hostKeyDAO.selectCount(wrapper);
    }

    @Override
    public DataGrid<HostKeyVO> getHostKeyPage(HostKeyQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostKeyDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return hostKeyDAO.of(wrapper)
                .page(request)
                .dataGrid(HostKeyConvert.MAPPER::to);
    }

    @Override
    public Integer deleteHostKeyById(Long id) {
        log.info("HostKeyService-deleteHostKeyById id: {}", id);
        int effect = hostKeyDAO.deleteById(id);
        log.info("HostKeyService-deleteHostKeyById effect: {}", effect);
        return effect;
    }

    @Override
    public Integer batchDeleteHostKeyByIdList(List<Long> idList) {
        log.info("HostKeyService-batchDeleteHostKeyByIdList idList: {}", idList);
        int effect = hostKeyDAO.deleteBatchIds(idList);
        log.info("HostKeyService-batchDeleteHostKeyByIdList effect: {}", effect);
        return effect;
    }

    @Override
    public Integer deleteHostKey(HostKeyQueryRequest request) {
        log.info("HostKeyService.deleteHostKey request: {}", JSON.toJSONString(request));
        // 条件
        LambdaQueryWrapper<HostKeyDO> wrapper = this.buildQueryWrapper(request);
        // 删除
        int effect = hostKeyDAO.delete(wrapper);
        log.info("HostKeyService.deleteHostKey effect: {}", effect);
        return effect;
    }

    @Override
    public void exportHostKey(HostKeyQueryRequest request, HttpServletResponse response) throws IOException {
        log.info("HostKeyService.exportHostKey request: {}", JSON.toJSONString(request));
        // 条件
        LambdaQueryWrapper<HostKeyDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        List<HostKeyExport> rows = hostKeyDAO.of(wrapper).list(HostKeyConvert.MAPPER::toExport);
        log.info("HostKeyService.exportHostKey size: {}", rows.size());
        // 导出
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExcelExport.create(HostKeyExport.class)
                .addRows(rows)
                .write(out)
                .close();
        // 传输
        Servlets.transfer(response, out.toByteArray(), FileNames.exportName(HostKeyExport.TITLE));
    }

    /**
     * 检测对象是否存在
     *
     * @param domain domain
     */
    private void checkHostKeyPresent(HostKeyDO domain) {
        // 构造条件
        LambdaQueryWrapper<HostKeyDO> wrapper = hostKeyDAO.wrapper()
                // 更新时忽略当前记录
                .ne(HostKeyDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(HostKeyDO::getName, domain.getName())
                .eq(HostKeyDO::getPublicKey, domain.getPublicKey())
                .eq(HostKeyDO::getPrivateKey, domain.getPrivateKey())
                .eq(HostKeyDO::getPassword, domain.getPassword());
        // 检查是否存在
        boolean present = hostKeyDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<HostKeyDO> buildQueryWrapper(HostKeyQueryRequest request) {
        return hostKeyDAO.wrapper()
                .eq(HostKeyDO::getId, request.getId())
                .eq(HostKeyDO::getName, request.getName())
                .eq(HostKeyDO::getPublicKey, request.getPublicKey())
                .eq(HostKeyDO::getPrivateKey, request.getPrivateKey())
                .eq(HostKeyDO::getPassword, request.getPassword());
    }

}
