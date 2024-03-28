package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.convert.ExecJobConvert;
import com.orion.ops.module.asset.dao.ExecJobDAO;
import com.orion.ops.module.asset.entity.domain.ExecJobDO;
import com.orion.ops.module.asset.entity.request.exec.ExecJobCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobUpdateRequest;
import com.orion.ops.module.asset.entity.vo.ExecJobVO;
import com.orion.ops.module.asset.service.ExecJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 计划执行任务 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Slf4j
@Service
public class ExecJobServiceImpl implements ExecJobService {

    @Resource
    private ExecJobDAO execJobDAO;

    @Override
    public Long createExecJob(ExecJobCreateRequest request) {
        log.info("ExecJobService-createExecJob request: {}", JSON.toJSONString(request));
        // 转换
        ExecJobDO record = ExecJobConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkExecJobPresent(record);
        // 插入
        int effect = execJobDAO.insert(record);
        Long id = record.getId();
        log.info("ExecJobService-createExecJob id: {}, effect: {}", id, effect);
        return id;
    }

    @Override
    public Integer updateExecJobById(ExecJobUpdateRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("ExecJobService-updateExecJobById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        ExecJobDO record = execJobDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        ExecJobDO updateRecord = ExecJobConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkExecJobPresent(updateRecord);
        // 更新
        int effect = execJobDAO.updateById(updateRecord);
        log.info("ExecJobService-updateExecJobById effect: {}", effect);
        return effect;
    }

    @Override
    public Integer updateExecJob(ExecJobQueryRequest query, ExecJobUpdateRequest update) {
        log.info("ExecJobService.updateExecJob query: {}, update: {}", JSON.toJSONString(query), JSON.toJSONString(update));
        // 条件
        LambdaQueryWrapper<ExecJobDO> wrapper = this.buildQueryWrapper(query);
        // 转换
        ExecJobDO updateRecord = ExecJobConvert.MAPPER.to(update);
        // 更新
        int effect = execJobDAO.update(updateRecord, wrapper);
        log.info("ExecJobService.updateExecJob effect: {}", effect);
        return effect;
    }

    @Override
    public ExecJobVO getExecJobById(Long id) {
        // 查询
        ExecJobDO record = execJobDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return ExecJobConvert.MAPPER.to(record);
    }

    @Override
    public List<ExecJobVO> getExecJobByIdList(List<Long> idList) {
        // 查询
        List<ExecJobDO> records = execJobDAO.selectBatchIds(idList);
        if (records.isEmpty()) {
            return Lists.empty();
        }
        // 转换
        return ExecJobConvert.MAPPER.to(records);
    }

    @Override
    public List<ExecJobVO> getExecJobList(ExecJobQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecJobDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return execJobDAO.of(wrapper).list(ExecJobConvert.MAPPER::to);
    }

    @Override
    public Long getExecJobCount(ExecJobQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecJobDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return execJobDAO.selectCount(wrapper);
    }

    @Override
    public DataGrid<ExecJobVO> getExecJobPage(ExecJobQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecJobDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return execJobDAO.of(wrapper)
                .page(request)
                .dataGrid(ExecJobConvert.MAPPER::to);
    }

    @Override
    public Integer deleteExecJobById(Long id) {
        log.info("ExecJobService-deleteExecJobById id: {}", id);
        // 检查数据是否存在
        ExecJobDO record = execJobDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = execJobDAO.deleteById(id);
        log.info("ExecJobService-deleteExecJobById id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer deleteExecJobByIdList(List<Long> idList) {
        log.info("ExecJobService-deleteExecJobByIdList idList: {}", idList);
        int effect = execJobDAO.deleteBatchIds(idList);
        log.info("ExecJobService-deleteExecJobByIdList effect: {}", effect);
        return effect;
    }

    @Override
    public Integer deleteExecJob(ExecJobQueryRequest request) {
        log.info("ExecJobService.deleteExecJob request: {}", JSON.toJSONString(request));
        // 条件
        LambdaQueryWrapper<ExecJobDO> wrapper = this.buildQueryWrapper(request);
        // 删除
        int effect = execJobDAO.delete(wrapper);
        log.info("ExecJobService.deleteExecJob effect: {}", effect);
        return effect;
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkExecJobPresent(ExecJobDO domain) {
        // 构造条件
        LambdaQueryWrapper<ExecJobDO> wrapper = execJobDAO.wrapper()
                // 更新时忽略当前记录
                .ne(ExecJobDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(ExecJobDO::getName, domain.getName())
                .eq(ExecJobDO::getExpression, domain.getExpression())
                .eq(ExecJobDO::getTimeout, domain.getTimeout())
                .eq(ExecJobDO::getCommand, domain.getCommand())
                .eq(ExecJobDO::getParameterSchema, domain.getParameterSchema())
                .eq(ExecJobDO::getStatus, domain.getStatus())
                .eq(ExecJobDO::getRecentLogId, domain.getRecentLogId());
        // 检查是否存在
        boolean present = execJobDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<ExecJobDO> buildQueryWrapper(ExecJobQueryRequest request) {
        String searchValue = request.getSearchValue();
        return execJobDAO.wrapper()
                .eq(ExecJobDO::getId, request.getId())
                .eq(ExecJobDO::getName, request.getName())
                .eq(ExecJobDO::getExpression, request.getExpression())
                .eq(ExecJobDO::getTimeout, request.getTimeout())
                .eq(ExecJobDO::getCommand, request.getCommand())
                .eq(ExecJobDO::getParameterSchema, request.getParameterSchema())
                .eq(ExecJobDO::getStatus, request.getStatus())
                .eq(ExecJobDO::getRecentLogId, request.getRecentLogId())
                .and(Strings.isNotEmpty(searchValue), c -> c
                        .eq(ExecJobDO::getId, searchValue).or()
                        .eq(ExecJobDO::getName, searchValue).or()
                        .eq(ExecJobDO::getExpression, searchValue).or()
                        .eq(ExecJobDO::getTimeout, searchValue).or()
                        .eq(ExecJobDO::getCommand, searchValue).or()
                        .eq(ExecJobDO::getParameterSchema, searchValue).or()
                        .eq(ExecJobDO::getStatus, searchValue).or()
                        .eq(ExecJobDO::getRecentLogId, searchValue)
                )
                .orderByDesc(ExecJobDO::getId);
    }

}
