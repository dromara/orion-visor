/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.asset.handler.host.extra.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.visor.framework.common.handler.data.model.GenericsDataModel;

/**
 * 主机拓展信息 - ssh 模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 21:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostSshExtraModel implements GenericsDataModel {

    /**
     * 认证方式
     */
    private String authType;

    /**
     * 认证方式
     */
    private String username;

    /**
     * 主机密钥
     */
    private Long keyId;

    /**
     * 主机身份
     */
    private Long identityId;

}
