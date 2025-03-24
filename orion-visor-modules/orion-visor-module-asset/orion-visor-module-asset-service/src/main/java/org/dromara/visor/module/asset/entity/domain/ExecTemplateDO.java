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
package org.dromara.visor.module.asset.entity.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dromara.visor.framework.mybatis.core.domain.BaseDO;

/**
 * 执行模板 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-7 18:08
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exec_template", autoResultMap = true)
@Schema(name = "ExecTemplateDO", description = "执行模板 实体对象")
public class ExecTemplateDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "命令")
    @TableField("command")
    private String command;

    @Schema(description = "超时时间秒 0不超时")
    @TableField("timeout")
    private Integer timeout;

    @Schema(description = "是否使用脚本执行")
    @TableField("script_exec")
    private Integer scriptExec;

    @Schema(description = "参数定义")
    @TableField("parameter_schema")
    private String parameterSchema;

}
