package com.orion.ops.module.infra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorLogModel;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.module.infra.convert.OperatorLogConvert;
import com.orion.ops.module.infra.dao.OperatorLogDAO;
import com.orion.ops.module.infra.define.operator.AuthenticationOperatorType;
import com.orion.ops.module.infra.entity.domain.OperatorLogDO;
import com.orion.ops.module.infra.entity.request.operator.OperatorLogQueryRequest;
import com.orion.ops.module.infra.entity.vo.LoginHistoryVO;
import com.orion.ops.module.infra.entity.vo.OperatorLogVO;
import com.orion.ops.module.infra.service.OperatorLogService;
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
    public List<LoginHistoryVO> getLoginHistory(String username) {
        // 条件
        OperatorLogQueryRequest request = new OperatorLogQueryRequest();
        request.setUsername(username);
        request.setType(AuthenticationOperatorType.LOGIN);
        LambdaQueryWrapper<OperatorLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return operatorLogDAO.of(wrapper)
                .limit(Const.LOGIN_HISTORY_COUNT)
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
                .ge(OperatorLogDO::getStartTime, request.getStartTimeStart())
                .le(OperatorLogDO::getStartTime, request.getStartTimeEnd())
                .orderByDesc(OperatorLogDO::getId);
    }

}
