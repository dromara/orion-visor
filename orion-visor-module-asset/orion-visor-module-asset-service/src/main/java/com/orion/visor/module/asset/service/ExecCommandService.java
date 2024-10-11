/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.asset.service;

import com.orion.visor.module.asset.entity.dto.ExecCommandExecDTO;
import com.orion.visor.module.asset.entity.request.exec.ExecCommandRequest;
import com.orion.visor.module.asset.entity.vo.ExecLogVO;

/**
 * 批量执行服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 12:02
 */
public interface ExecCommandService {

    /**
     * 批量执行命令
     *
     * @param request request
     * @return result
     */
    ExecLogVO execCommand(ExecCommandRequest request);

    /**
     * 批量执行命令
     *
     * @param request request
     * @return result
     */
    ExecLogVO execCommandWithSource(ExecCommandExecDTO request);

    /**
     * 重新执行命令
     *
     * @param logId logId
     * @return result
     */
    ExecLogVO reExecCommand(Long logId);

}
