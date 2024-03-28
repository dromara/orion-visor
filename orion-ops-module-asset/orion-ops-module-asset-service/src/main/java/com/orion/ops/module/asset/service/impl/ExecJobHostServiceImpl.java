package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.convert.ExecJobHostConvert;
import com.orion.ops.module.asset.dao.ExecJobHostDAO;
import com.orion.ops.module.asset.entity.domain.ExecJobHostDO;
import com.orion.ops.module.asset.entity.request.exec.ExecJobHostCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobHostQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobHostUpdateRequest;
import com.orion.ops.module.asset.entity.vo.ExecJobHostVO;
import com.orion.ops.module.asset.service.ExecJobHostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 计划执行任务主机 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Slf4j
@Service
public class ExecJobHostServiceImpl implements ExecJobHostService {

    @Resource
    private ExecJobHostDAO execJobHostDAO;

    @Override
    public Long createExecJobHost(ExecJobHostCreateRequest request) {
        log.info("ExecJobHostService-createExecJobHost request: {}", JSON.toJSONString(request));
        // 转换
        ExecJobHostDO record = ExecJobHostConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkExecJobHostPresent(record);
        // 插入
        int effect = execJobHostDAO.insert(record);
        Long id = record.getId();
        log.info("ExecJobHostService-createExecJobHost id: {}, effect: {}", id, effect);
        return id;
    }

    @Override
    public Integer updateExecJobHostById(ExecJobHostUpdateRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("ExecJobHostService-updateExecJobHostById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        ExecJobHostDO record = execJobHostDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        ExecJobHostDO updateRecord = ExecJobHostConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkExecJobHostPresent(updateRecord);
        // 更新
        int effect = execJobHostDAO.updateById(updateRecord);
        log.info("ExecJobHostService-updateExecJobHostById effect: {}", effect);
        return effect;
    }

    @Override
    public Integer updateExecJobHost(ExecJobHostQueryRequest query, ExecJobHostUpdateRequest update) {
        log.info("ExecJobHostService.updateExecJobHost query: {}, update: {}", JSON.toJSONString(query), JSON.toJSONString(update));
        // 条件
        LambdaQueryWrapper<ExecJobHostDO> wrapper = this.buildQueryWrapper(query);
        // 转换
        ExecJobHostDO updateRecord = ExecJobHostConvert.MAPPER.to(update);
        // 更新
        int effect = execJobHostDAO.update(updateRecord, wrapper);
        log.info("ExecJobHostService.updateExecJobHost effect: {}", effect);
        return effect;
    }

    @Override
    public ExecJobHostVO getExecJobHostById(Long id) {
        // 查询
        ExecJobHostDO record = execJobHostDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return ExecJobHostConvert.MAPPER.to(record);
    }

    @Override
    public List<ExecJobHostVO> getExecJobHostByIdList(List<Long> idList) {
        // 查询
        List<ExecJobHostDO> records = execJobHostDAO.selectBatchIds(idList);
        if (records.isEmpty()) {
            return Lists.empty();
        }
        // 转换
        return ExecJobHostConvert.MAPPER.to(records);
    }

    @Override
    public List<ExecJobHostVO> getExecJobHostList(ExecJobHostQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecJobHostDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return execJobHostDAO.of(wrapper).list(ExecJobHostConvert.MAPPER::to);
    }

    @Override
    public Long getExecJobHostCount(ExecJobHostQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecJobHostDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return execJobHostDAO.selectCount(wrapper);
    }

    @Override
    public DataGrid<ExecJobHostVO> getExecJobHostPage(ExecJobHostQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecJobHostDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return execJobHostDAO.of(wrapper)
                .page(request)
                .dataGrid(ExecJobHostConvert.MAPPER::to);
    }

    @Override
    public Integer deleteExecJobHostById(Long id) {
        log.info("ExecJobHostService-deleteExecJobHostById id: {}", id);
        // 检查数据是否存在
        ExecJobHostDO record = execJobHostDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = execJobHostDAO.deleteById(id);
        log.info("ExecJobHostService-deleteExecJobHostById id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer deleteExecJobHostByIdList(List<Long> idList) {
        log.info("ExecJobHostService-deleteExecJobHostByIdList idList: {}", idList);
        int effect = execJobHostDAO.deleteBatchIds(idList);
        log.info("ExecJobHostService-deleteExecJobHostByIdList effect: {}", effect);
        return effect;
    }

    @Override
    public Integer deleteExecJobHost(ExecJobHostQueryRequest request) {
        log.info("ExecJobHostService.deleteExecJobHost request: {}", JSON.toJSONString(request));
        // 条件
        LambdaQueryWrapper<ExecJobHostDO> wrapper = this.buildQueryWrapper(request);
        // 删除
        int effect = execJobHostDAO.delete(wrapper);
        log.info("ExecJobHostService.deleteExecJobHost effect: {}", effect);
        return effect;
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkExecJobHostPresent(ExecJobHostDO domain) {
        // 构造条件
        LambdaQueryWrapper<ExecJobHostDO> wrapper = execJobHostDAO.wrapper()
                // 更新时忽略当前记录
                .ne(ExecJobHostDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(ExecJobHostDO::getJobId, domain.getJobId())
                .eq(ExecJobHostDO::getHostId, domain.getHostId());
        // 检查是否存在
        boolean present = execJobHostDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<ExecJobHostDO> buildQueryWrapper(ExecJobHostQueryRequest request) {
        String searchValue = request.getSearchValue();
        return execJobHostDAO.wrapper()
                .eq(ExecJobHostDO::getId, request.getId())
                .eq(ExecJobHostDO::getJobId, request.getJobId())
                .eq(ExecJobHostDO::getHostId, request.getHostId())
                .and(Strings.isNotEmpty(searchValue), c -> c
                        .eq(ExecJobHostDO::getId, searchValue).or()
                        .eq(ExecJobHostDO::getJobId, searchValue).or()
                        .eq(ExecJobHostDO::getHostId, searchValue)
                )
                .orderByDesc(ExecJobHostDO::getId);
    }

}
