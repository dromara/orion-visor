package com.orion.ops.module.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Arrays1;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.convert.ExecLogConvert;
import com.orion.ops.module.asset.dao.ExecLogDAO;
import com.orion.ops.module.asset.entity.domain.ExecLogDO;
import com.orion.ops.module.asset.entity.request.exec.ExecLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.ExecLogVO;
import com.orion.ops.module.asset.service.ExecLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 批量执行日志 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Slf4j
@Service
public class ExecLogServiceImpl implements ExecLogService {

    @Resource
    private ExecLogDAO execLogDAO;

    @Override
    public ExecLogVO getExecLogById(Long id) {
        // 查询
        ExecLogDO record = execLogDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return ExecLogConvert.MAPPER.to(record);
    }

    @Override
    public DataGrid<ExecLogVO> getExecLogPage(ExecLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return execLogDAO.of(wrapper)
                .page(request)
                .dataGrid(ExecLogConvert.MAPPER::to);
    }

    @Override
    public Integer deleteExecLogById(Long id) {
        log.info("ExecLogService-deleteExecLogById id: {}", id);
        // 检查数据是否存在
        ExecLogDO record = execLogDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = execLogDAO.deleteById(id);
        log.info("ExecLogService-deleteExecLogById id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer deleteExecLogByIdList(List<Long> idList) {
        log.info("ExecLogService-deleteExecLogByIdList idList: {}", idList);
        int effect = execLogDAO.deleteBatchIds(idList);
        log.info("ExecLogService-deleteExecLogByIdList effect: {}", effect);
        return effect;
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<ExecLogDO> buildQueryWrapper(ExecLogQueryRequest request) {
        return execLogDAO.wrapper()
                .eq(ExecLogDO::getId, request.getId())
                .eq(ExecLogDO::getUserId, request.getUserId())
                .eq(ExecLogDO::getSource, request.getSource())
                .eq(ExecLogDO::getSourceId, request.getSourceId())
                .eq(ExecLogDO::getDesc, request.getDesc())
                .eq(ExecLogDO::getCommand, request.getCommand())
                .eq(ExecLogDO::getStatus, request.getStatus())
                .ge(ExecLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 0))
                .le(ExecLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 1))
                .orderByDesc(ExecLogDO::getId);
    }

}
