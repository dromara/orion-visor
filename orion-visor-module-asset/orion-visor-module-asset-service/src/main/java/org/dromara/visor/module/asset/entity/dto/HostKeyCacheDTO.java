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
package org.dromara.visor.module.asset.entity.dto;

import cn.orionsec.kit.lang.define.cache.key.model.LongCacheIdModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 主机密钥缓存
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/20 13:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostKeyCacheDTO", description = "主机密钥缓存")
public class HostKeyCacheDTO implements LongCacheIdModel, Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "名称")
    private String name;

    /**
     * 资产页面展示
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 资产页面展示
     */
    @Schema(description = "修改时间")
    private Date updateTime;

}