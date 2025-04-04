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
package org.dromara.visor.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dromara.visor.framework.mybatis.core.domain.BaseDO;

import java.util.Date;

/**
 * 操作日志 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "operator_log", autoResultMap = true)
@Schema(name = "OperatorLogDO", description = "操作日志 实体对象")
public class OperatorLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "traceId")
    @TableField("trace_id")
    private String traceId;

    @Schema(description = "请求ip")
    @TableField("address")
    private String address;

    @Schema(description = "请求地址")
    @TableField("location")
    private String location;

    @Schema(description = "userAgent")
    @TableField("user_agent")
    private String userAgent;

    @Schema(description = "风险等级")
    @TableField("risk_level")
    private String riskLevel;

    @Schema(description = "模块")
    @TableField("module")
    private String module;

    @Schema(description = "操作类型")
    @TableField("type")
    private String type;

    @Schema(description = "日志")
    @TableField("log_info")
    private String logInfo;

    @Schema(description = "参数")
    @TableField("extra")
    private String extra;

    @Schema(description = "操作结果 0失败 1成功")
    @TableField("result")
    private Integer result;

    @Schema(description = "错误信息")
    @TableField("error_message")
    private String errorMessage;

    @Schema(description = "返回值")
    @TableField("return_value")
    private String returnValue;

    @Schema(description = "操作时间")
    @TableField("duration")
    private Integer duration;

    @Schema(description = "开始时间")
    @TableField("start_time")
    private Date startTime;

    @Schema(description = "结束时间")
    @TableField("end_time")
    private Date endTime;

    @Schema(description = "修改时间")
    @TableField(exist = false)
    private Date updateTime;

    @Schema(description = "创建人")
    @TableField(exist = false)
    private String creator;

    @Schema(description = "修改人")
    @TableField(exist = false)
    private String updater;

}
