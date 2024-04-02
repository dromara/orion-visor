package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.convert.ExecJobConvert;
import com.orion.ops.module.asset.dao.ExecJobDAO;
import com.orion.ops.module.asset.entity.domain.ExecJobDO;
import com.orion.ops.module.asset.entity.request.exec.ExecJobCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobUpdateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobUpdateStatusRequest;
import com.orion.ops.module.asset.entity.vo.ExecJobVO;
import com.orion.ops.module.asset.service.ExecJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
        // TODO 查询主机是否存在
        // 查询是否有主机权限
        // 插入
        int effect = execJobDAO.insert(record);
        Long id = record.getId();
        // TODO 插入主机
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
    public Integer updateExecJobStatus(ExecJobUpdateStatusRequest request) {
        return null;
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
                .eq(ExecJobDO::getName, domain.getName());
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
        return execJobDAO.wrapper()
                .eq(ExecJobDO::getId, request.getId())
                .eq(ExecJobDO::getName, request.getName())
                .eq(ExecJobDO::getCommand, request.getCommand())
                .eq(ExecJobDO::getStatus, request.getStatus())
                .orderByDesc(ExecJobDO::getId);
    }

}
