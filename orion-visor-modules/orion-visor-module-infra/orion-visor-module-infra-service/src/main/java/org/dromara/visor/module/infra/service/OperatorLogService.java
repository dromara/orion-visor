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
package org.dromara.visor.module.infra.service;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import org.dromara.visor.module.infra.entity.domain.OperatorLogDO;
import org.dromara.visor.module.infra.entity.request.operator.OperatorLogClearRequest;
import org.dromara.visor.module.infra.entity.request.operator.OperatorLogQueryRequest;
import org.dromara.visor.module.infra.entity.vo.LoginHistoryVO;
import org.dromara.visor.module.infra.entity.vo.OperatorLogVO;

import java.util.List;

/**
 * 操作日志 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
public interface OperatorLogService {

    /**
     * 添加操作日志
     *
     * @param model model
     */
    void addOperatorLog(OperatorLogModel model);

    /**
     * 分页查询操作日志
     *
     * @param request request
     * @return rows
     */
    DataGrid<OperatorLogVO> getOperatorLogPage(OperatorLogQueryRequest request);

    /**
     * 删除操作日志
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteOperatorLog(List<Long> idList);

    /**
     * 查询操作日志数量
     *
     * @param request request
     * @return count
     */
    Long getOperatorLogCount(OperatorLogQueryRequest request);

    /**
     * 清理操作日志
     *
     * @param request request
     * @return effect
     */
    Integer clearOperatorLog(OperatorLogClearRequest request);

    /**
     * 查询用户登录日志
     *
     * @param username username
     * @param count    count
     * @return rows
     */
    List<LoginHistoryVO> getLoginHistory(String username, Integer count);

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    LambdaQueryWrapper<OperatorLogDO> buildQueryWrapper(OperatorLogQueryRequest request);

}
