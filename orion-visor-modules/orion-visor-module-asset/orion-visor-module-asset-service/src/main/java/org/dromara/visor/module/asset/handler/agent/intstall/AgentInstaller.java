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
package org.dromara.visor.module.asset.handler.agent.intstall;

import cn.orionsec.kit.lang.able.SafeCloseable;
import org.dromara.visor.module.asset.define.AssetThreadPools;
import org.dromara.visor.module.asset.enums.HostOsTypeEnum;
import org.dromara.visor.module.asset.handler.agent.model.AgentInstallParams;

/**
 * 探针安装器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/29 10:28
 */
public interface AgentInstaller extends Runnable, SafeCloseable {

    /**
     * 启动安装
     *
     * @param params params
     */
    static void start(AgentInstallParams params) {
        AgentInstaller installer;
        if (HostOsTypeEnum.WINDOWS.name().equals(params.getOsType())) {
            // windows 安装
            installer = new WindowsAgentInstaller(params);
        } else {
            // 其他安装
            installer = new LinuxAgentInstaller(params);
        }
        AssetThreadPools.AGENT_INSTALL.execute(installer);
    }

}
