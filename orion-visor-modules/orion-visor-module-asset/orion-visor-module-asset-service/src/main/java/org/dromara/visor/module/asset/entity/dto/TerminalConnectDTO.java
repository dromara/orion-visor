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
package org.dromara.visor.module.asset.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.visor.framework.desensitize.core.annotation.Desensitize;
import org.dromara.visor.framework.desensitize.core.annotation.DesensitizeObject;

/**
 * 终端连接参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 15:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DesensitizeObject
@Schema(name = "TerminalConnectDTO", description = "终端连接参数")
public class TerminalConnectDTO {

    @Schema(description = "logId")
    private Long logId;

    @Schema(description = "连接类型")
    private String connectType;

    @Schema(description = "hostId")
    private Long hostId;

    @Schema(description = "hostName")
    private String hostName;

    @Schema(description = "主机编码")
    private String hostCode;

    @Schema(description = "主机地址")
    private String hostAddress;

    @Schema(description = "主机端口")
    private Integer hostPort;

    @Schema(description = "系统类型")
    private String osType;

    @Schema(description = "系统架构")
    private String archType;

    @Schema(description = "超时时间")
    private Integer timeout;

    @Schema(description = "SSH输出编码")
    private String charset;

    @Schema(description = "文件名称编码")
    private String fileNameCharset;

    @Schema(description = "文件内容编码")
    private String fileContentCharset;

    @Schema(description = "用户名")
    private String username;

    @Desensitize(toEmpty = true)
    @Schema(description = "密码")
    private String password;

    @Schema(description = "密钥id")
    private Long keyId;

    @Desensitize(toEmpty = true)
    @Schema(description = "公钥文本")
    private String publicKey;

    @Desensitize(toEmpty = true)
    @Schema(description = "私钥文本")
    private String privateKey;

    @Desensitize(toEmpty = true)
    @Schema(description = "私钥密码")
    private String privateKeyPassword;

}
