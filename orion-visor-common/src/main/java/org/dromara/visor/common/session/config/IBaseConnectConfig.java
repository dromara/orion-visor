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
package org.dromara.visor.common.session.config;

/**
 * 基础连接配置定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/6/24 17:11
 */
public interface IBaseConnectConfig {

    // -------------------- getter/setter --------------------

    String getOsType();

    void setOsType(String osType);

    String getArchType();

    void setArchType(String archType);

    Long getHostId();

    void setHostId(Long hostId);

    String getHostName();

    void setHostName(String hostName);

    String getHostCode();

    void setHostCode(String hostCode);

    String getHostAddress();

    void setHostAddress(String hostAddress);

    Integer getHostPort();

    void setHostPort(Integer hostPort);

    String getAgentKey();

    void setAgentKey(String agentKey);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

}
