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
package org.dromara.visor.module.asset.service;

import org.dromara.visor.module.asset.entity.dto.ExecCommandExecDTO;
import org.dromara.visor.module.asset.entity.request.exec.ExecCommandRequest;
import org.dromara.visor.module.asset.entity.vo.ExecLogVO;

/**
 * 批量执行服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 12:02
 */
public interface ExecCommandService {

    /**
     * 执行命令
     *
     * @param request request
     * @return result
     */
    ExecLogVO execCommand(ExecCommandRequest request);

    /**
     * 重新执行命令
     *
     * @param logId logId
     * @return result
     */
    ExecLogVO reExecCommand(Long logId);

    /**
     * 执行命令
     *
     * @param request request
     * @return result
     */
    ExecLogVO execCommandWithSource(ExecCommandExecDTO request);

    /**
     * 创建执行命令
     *
     * @param request request
     * @return result
     */
    ExecLogVO createCommandWithSource(ExecCommandExecDTO request);

}
