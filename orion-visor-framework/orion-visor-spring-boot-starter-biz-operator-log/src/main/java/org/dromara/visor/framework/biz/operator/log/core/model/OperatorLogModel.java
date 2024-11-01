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
package org.dromara.visor.framework.biz.operator.log.core.model;

import lombok.Data;
import org.dromara.visor.framework.common.entity.RequestIdentity;

import java.util.Date;

/**
 * 操作日志模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/9 18:44
 */
@Data
public class OperatorLogModel implements RequestIdentity {

    /**
     * userId
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * traceId
     */
    private String traceId;

    /**
     * 请求 ip
     */
    private String address;

    /**
     * 请求地址
     */
    private String location;

    /**
     * user-agent
     */
    private String userAgent;

    /**
     * 日志
     */
    private String logInfo;

    /**
     * 风险等级
     */
    private String riskLevel;

    /**
     * 模块
     */
    private String module;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 参数
     */
    private String extra;

    /**
     * 操作结果 0失败 1成功
     */
    private Integer result;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 返回值
     */
    private String returnValue;

    /**
     * 操作时间
     */
    private Integer duration;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

}
