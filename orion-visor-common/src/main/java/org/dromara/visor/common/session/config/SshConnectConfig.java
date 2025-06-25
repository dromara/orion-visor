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
 * SSH 连接参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 15:47
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SshConnectConfig", description = "SSH 连接参数")
public class SshConnectConfig extends BaseConnectConfig {

    @Schema(description = "超时时间")
    private Integer timeout;

    @Schema(description = "SSH输出编码")
    private String charset;

    @Schema(description = "文件名称编码")
    private String fileNameCharset;

    @Schema(description = "文件内容编码")
    private String fileContentCharset;

    @Schema(description = "密钥id")
    private Long keyId;

    @Schema(description = "公钥文本")
    private String publicKey;

    @Schema(description = "私钥文本")
    private String privateKey;

    @Schema(description = "私钥密码")
    private String privateKeyPassword;

}
