/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.terminal.service.impl;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Strings;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ExtraFieldConst;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.module.infra.api.OperatorLogApi;
import org.dromara.visor.module.infra.entity.dto.operator.OperatorLogQueryDTO;
import org.dromara.visor.module.terminal.convert.TerminalFileLogConvert;
import org.dromara.visor.module.terminal.define.operator.TerminalFileLogOperatorType;
import org.dromara.visor.module.terminal.entity.request.terminal.TerminalFileLogQueryRequest;
import org.dromara.visor.module.terminal.entity.vo.TerminalFileLogVO;
import org.dromara.visor.module.terminal.service.TerminalFileLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 终端文件日志服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/4 23:35
 */
@Slf4j
@Service
public class TerminalFileLogServiceImpl implements TerminalFileLogService {

    @Resource
    private OperatorLogApi operatorLogApi;

    @Override
    public DataGrid<TerminalFileLogVO> getTerminalFileLogPage(TerminalFileLogQueryRequest request) {
        // 查询
        OperatorLogQueryDTO query = this.buildQueryInfo(request);
        // 转换
        return operatorLogApi.getOperatorLogPage(query)
                .map(s -> {
                    JSONObject extra = JSON.parseObject(s.getExtra());
                    TerminalFileLogVO vo = TerminalFileLogConvert.MAPPER.to(s);
                    vo.setHostId(extra.getLong(ExtraFieldConst.HOST_ID));
                    vo.setHostName(extra.getString(ExtraFieldConst.HOST_NAME));
                    vo.setHostAddress(extra.getString(ExtraFieldConst.ADDRESS));
                    String[] paths = Optional.ofNullable(extra.getString(ExtraFieldConst.PATH))
                            .map(p -> p.split("\\|"))
                            .orElse(new String[0]);
                    vo.setPaths(paths);
                    vo.setExtra(extra);
                    return vo;
                });
    }

    @Override
    public Long getTerminalFileLogCount(TerminalFileLogQueryRequest request) {
        // 查询
        OperatorLogQueryDTO query = this.buildQueryInfo(request);
        // 转换
        return operatorLogApi.getOperatorLogCount(query);
    }

    @Override
    public Integer deleteTerminalFileLog(List<Long> idList) {
        log.info("TerminalSftpService.deleteTerminalFileLog start {}", JSON.toJSONString(idList));
        Integer effect = operatorLogApi.deleteOperatorLog(idList);
        log.info("TerminalSftpService.deleteTerminalFileLog finish {}", effect);
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
    private OperatorLogQueryDTO buildQueryInfo(TerminalFileLogQueryRequest request) {
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
        query.setOrder(request.getOrder());
        if (Strings.isBlank(type)) {
            // 查询全部文件操作类型
            query.setTypeList(TerminalFileLogOperatorType.TYPES);
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
