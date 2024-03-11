package com.orion.ops.module.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.asset.convert.ExecHostLogConvert;
import com.orion.ops.module.asset.dao.ExecHostLogDAO;
import com.orion.ops.module.asset.entity.domain.ExecHostLogDO;
import com.orion.ops.module.asset.entity.request.exec.ExecHostLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.ExecHostLogVO;
import com.orion.ops.module.asset.service.ExecHostLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 批量执行主机日志 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 14:05
 */
@Slf4j
@Service
public class ExecHostLogServiceImpl implements ExecHostLogService {

    @Resource
    private ExecHostLogDAO execHostLogDAO;

    @Override
    public List<ExecHostLogVO> getExecHostLogList(ExecHostLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<ExecHostLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return execHostLogDAO.of(wrapper).list(ExecHostLogConvert.MAPPER::to);
    }

    @Override
    public Integer deleteExecHostLogById(Long id) {
        log.info("ExecHostLogService-deleteExecHostLogById id: {}", id);
        // 检查数据是否存在
        ExecHostLogDO record = execHostLogDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = execHostLogDAO.deleteById(id);
        log.info("ExecHostLogService-deleteExecHostLogById id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer deleteExecHostLogByIdList(List<Long> idList) {
        log.info("ExecHostLogService-deleteExecHostLogByIdList idList: {}", idList);
        int effect = execHostLogDAO.deleteBatchIds(idList);
        log.info("ExecHostLogService-deleteExecHostLogByIdList effect: {}", effect);
        return effect;
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<ExecHostLogDO> buildQueryWrapper(ExecHostLogQueryRequest request) {
        return execHostLogDAO.wrapper()
                .eq(ExecHostLogDO::getId, request.getId())
                .eq(ExecHostLogDO::getLogId, request.getLogId())
                .eq(ExecHostLogDO::getHostId, request.getHostId())
                .eq(ExecHostLogDO::getHostName, request.getHostName())
                .eq(ExecHostLogDO::getStatus, request.getStatus())
                .eq(ExecHostLogDO::getCommand, request.getCommand())
                .eq(ExecHostLogDO::getParameter, request.getParameter())
                .eq(ExecHostLogDO::getExitStatus, request.getExitStatus())
                .eq(ExecHostLogDO::getLogPath, request.getLogPath())
                .eq(ExecHostLogDO::getStartTime, request.getStartTime())
                .eq(ExecHostLogDO::getFinishTime, request.getFinishTime())
                .orderByDesc(ExecHostLogDO::getId);
    }

}
