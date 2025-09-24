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
package org.dromara.visor.module.monitor.enums;

/**
 * 告警记录处理状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/19 17:43
 */
public enum AlarmHandleStatusEnum {

    /**
     * 新告警
     */
    NEW,

    /**
     * 已确认
     */
    CONFIRMED,

    /**
     * 已处理
     */
    RESOLVE,

    /**
     * 忽略
     */
    IGNORED,

    ;

}
