package com.orion.visor.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Valid;
import com.orion.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.utils.SqlUtils;
import com.orion.visor.module.infra.convert.OperatorLogConvert;
import com.orion.visor.module.infra.dao.OperatorLogDAO;
import com.orion.visor.module.infra.define.operator.AuthenticationOperatorType;
import com.orion.visor.module.infra.entity.domain.OperatorLogDO;
import com.orion.visor.module.infra.entity.request.operator.OperatorLogClearRequest;
import com.orion.visor.module.infra.entity.request.operator.OperatorLogQueryRequest;
import com.orion.visor.module.infra.entity.vo.LoginHistoryVO;
import com.orion.visor.module.infra.entity.vo.OperatorLogVO;
import com.orion.visor.module.infra.service.OperatorLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Slf4j
@Service
public class OperatorLogServiceImpl implements OperatorLogService {

    @Resource
    private OperatorLogDAO operatorLogDAO;

    @Override
    public void addOperatorLog(OperatorLogModel model) {
        // 转换
        OperatorLogDO record = OperatorLogConvert.MAPPER.to(model);
        // 插入
        operatorLogDAO.insert(record);
    }

    @Override
    public DataGrid<OperatorLogVO> getOperatorLogPage(OperatorLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<OperatorLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return operatorLogDAO.of(wrapper)
                .page(request)
                .dataGrid(OperatorLogConvert.MAPPER::to);
    }

    @Override
    public Integer deleteOperatorLog(List<Long> idList) {
        log.info("OperatorLogService.deleteOperatorLog start {}", JSON.toJSONString(idList));
        int effect = operatorLogDAO.deleteBatchIds(idList);
        log.info("OperatorLogService.deleteOperatorLog finish {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    public Long getOperatorLogCount(OperatorLogQueryRequest request) {
        return operatorLogDAO.of()
                .wrapper(this.buildQueryWrapper(request))
                .countMax(request.getLimit());
    }

    @Override
    public Integer clearOperatorLog(OperatorLogClearRequest request) {
        log.info("OperatorLogService.clearOperatorLog start {}", JSON.toJSONString(request));
        // 删除参数
        LambdaQueryWrapper<OperatorLogDO> wrapper = this.buildQueryWrapper(request)
                .last(SqlUtils.limit(request.getLimit()));
        // 删除
        int effect = operatorLogDAO.delete(wrapper);
        log.info("OperatorLogService.clearOperatorLog finish {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    public List<LoginHistoryVO> getLoginHistory(String username, Integer count) {
        Valid.gt(count, 0, ErrorMessage.PARAM_ERROR);
        // 条件
        OperatorLogQueryRequest request = new OperatorLogQueryRequest();
        request.setUsername(username);
        request.setType(AuthenticationOperatorType.LOGIN);
        LambdaQueryWrapper<OperatorLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return operatorLogDAO.of(wrapper)
                .limit(count)
                .list(OperatorLogConvert.MAPPER::toLoginHistory);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<OperatorLogDO> buildQueryWrapper(OperatorLogQueryRequest request) {
        return operatorLogDAO.wrapper()
                .eq(OperatorLogDO::getUserId, request.getUserId())
                .eq(OperatorLogDO::getUsername, request.getUsername())
                .eq(OperatorLogDO::getRiskLevel, request.getRiskLevel())
                .eq(OperatorLogDO::getModule, request.getModule())
                .eq(OperatorLogDO::getType, request.getType())
                .eq(OperatorLogDO::getResult, request.getResult())
                .ge(OperatorLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 0))
                .le(OperatorLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 1))
                .orderByDesc(OperatorLogDO::getId);
    }

}
