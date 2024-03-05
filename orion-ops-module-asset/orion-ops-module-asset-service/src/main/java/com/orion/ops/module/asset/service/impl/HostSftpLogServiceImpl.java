package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.constant.ExtraFieldConst;
import com.orion.ops.module.asset.convert.HostSftpLogConvert;
import com.orion.ops.module.asset.define.operator.HostTerminalOperatorType;
import com.orion.ops.module.asset.entity.request.host.HostSftpLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.HostSftpLogVO;
import com.orion.ops.module.asset.service.HostSftpLogService;
import com.orion.ops.module.infra.api.OperatorLogApi;
import com.orion.ops.module.infra.entity.dto.operator.OperatorLogDTO;
import com.orion.ops.module.infra.entity.dto.operator.OperatorLogQueryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SFTP 操作日志 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/4 23:35
 */
@Slf4j
@Service
public class HostSftpLogServiceImpl implements HostSftpLogService {

    @Resource
    private OperatorLogApi operatorLogApi;

    @Override
    public DataGrid<HostSftpLogVO> getHostSftpLogPage(HostSftpLogQueryRequest request) {
        // 查询
        OperatorLogQueryDTO query = this.buildQueryInfo(request);
        DataGrid<OperatorLogDTO> dataGrid = operatorLogApi.getOperatorLogPage(query);
        // 转换
        List<HostSftpLogVO> rows = dataGrid.stream()
                .map(s -> {
                    JSONObject extra = JSON.parseObject(s.getExtra());
                    HostSftpLogVO vo = HostSftpLogConvert.MAPPER.to(s);
                    vo.setHostId(extra.getLong(ExtraFieldConst.HOST_ID));
                    vo.setHostName(extra.getString(ExtraFieldConst.HOST_NAME));
                    vo.setHostAddress(extra.getString(ExtraFieldConst.ADDRESS));
                    vo.setPaths(extra.getString(ExtraFieldConst.PATH).split("\\|"));
                    vo.setExtra(extra);
                    return vo;
                }).collect(Collectors.toList());
        // 返回
        DataGrid<HostSftpLogVO> result = new DataGrid<>();
        result.setRows(rows);
        result.setPage(dataGrid.getPage());
        result.setLimit(dataGrid.getLimit());
        result.setSize(dataGrid.getSize());
        result.setTotal(dataGrid.getTotal());
        return result;
    }

    @Override
    public Integer deleteHostSftpLog(List<Long> idList) {
        log.info("HostSftpLogService.deleteSftpLog start {}", JSON.toJSONString(idList));
        Integer effect = operatorLogApi.deleteOperatorLog(idList);
        log.info("HostSftpLogService.deleteSftpLog finish {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    /**
     * 构建查询对象
     *
     * @param request request
     * @return query
     */
    private OperatorLogQueryDTO buildQueryInfo(HostSftpLogQueryRequest request) {
        Long hostId = request.getHostId();
        String type = request.getType();
        // 构建参数
        OperatorLogQueryDTO query = OperatorLogQueryDTO.builder()
                .userId(request.getUserId())
                .result(request.getResult())
                .startTimeStart(Arrays1.getIfPresent(request.getStartTimeRange(), 0))
                .startTimeEnd(Arrays1.getIfPresent(request.getStartTimeRange(), 1))
                .build();
        query.setPage(request.getPage());
        query.setLimit(request.getLimit());
        if (Strings.isBlank(type)) {
            // 查询全部 SFTP 类型
            query.setTypeList(HostTerminalOperatorType.SFTP_TYPES);
        } else {
            query.setType(type);
        }
        // 模糊查询
        if (hostId != null) {
            query.setExtra("\"hostId\": " + hostId + ",");
        }
        return query;
    }

}
