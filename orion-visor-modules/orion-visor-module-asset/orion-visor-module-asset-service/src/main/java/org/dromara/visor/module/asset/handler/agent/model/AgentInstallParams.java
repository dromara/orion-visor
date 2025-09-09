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
package org.dromara.visor.module.asset.handler.agent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 探针安装参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/28 18:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentInstallParams {

    /**
     * logId
     */
    private Long logId;

    /**
     * hostId
     */
    private Long hostId;

    /**
     * 系统类型
     */
    private String osType;

    /**
     * agentKey
     */
    private String agentKey;

    /**
     * 配置文件路径
     */
    private String configFilePath;

    /**
     * 探针文件路径
     */
    private String agentFilePath;

    /**
     * 启动脚本路径
     */
    private String startScriptPath;

    /**
     * 替换变量
     */
    private Map<String, String> replaceVars;

}
