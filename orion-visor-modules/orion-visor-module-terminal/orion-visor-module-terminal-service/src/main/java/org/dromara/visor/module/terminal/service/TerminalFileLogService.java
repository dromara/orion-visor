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
package org.dromara.visor.module.terminal.service;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import org.dromara.visor.module.terminal.entity.request.terminal.TerminalFileLogQueryRequest;
import org.dromara.visor.module.terminal.entity.vo.TerminalFileLogVO;

import java.util.List;

/**
 * 终端文件日志服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
public interface TerminalFileLogService {

    /**
     * 分页查询终端文件操作日志
     *
     * @param request request
     * @return rows
     */
    DataGrid<TerminalFileLogVO> getTerminalFileLogPage(TerminalFileLogQueryRequest request);

    /**
     * 获取终端文件操作日志数量
     *
     * @param request request
     * @return count
     */
    Long getTerminalFileLogCount(TerminalFileLogQueryRequest request);

    /**
     * 删除终端文件操作日志
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteTerminalFileLog(List<Long> idList);

}
