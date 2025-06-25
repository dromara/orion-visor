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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * RDP 连接参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/4/1 16:57
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "RdpConnectConfig", description = "RDP 连接参数")
public class RdpConnectConfig extends BaseConnectConfig {

    @Schema(description = "低带宽模式")
    private Boolean lowBandwidthMode;

    @Schema(description = "RDP 版本是否大于8.1")
    private Boolean versionGt81;

    @Schema(description = "时区")
    private String timezone;

    @Schema(description = "键盘布局")
    private String keyboardLayout;

    @Schema(description = "剪切板规范")
    private String clipboardNormalize;

    @Schema(description = "域")
    private String domain;

    @Schema(description = "预连接id")
    private String preConnectionId;

    @Schema(description = "预连接数据")
    private String preConnectionBlob;

    @Schema(description = "远程应用")
    private String remoteApp;

    @Schema(description = "远程应用路径")
    private String remoteAppDir;

    @Schema(description = "远程应用参数")
    private String remoteAppArgs;

}
