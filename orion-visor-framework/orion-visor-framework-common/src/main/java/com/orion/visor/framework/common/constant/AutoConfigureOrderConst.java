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
package com.orion.visor.framework.common.constant;

/**
 * 自动装配排序常量
 * <p>
 * 实际遵循 DependsOn
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/11 17:55
 */
public interface AutoConfigureOrderConst {

    int FRAMEWORK_COMMON = Integer.MIN_VALUE + 1000;

    int FRAMEWORK_WEB = Integer.MIN_VALUE + 1100;

    int FRAMEWORK_SECURITY = Integer.MIN_VALUE + 1200;

    int FRAMEWORK_SECURITY_CRYPTO = Integer.MIN_VALUE + 1250;

    int FRAMEWORK_WEBSOCKET = Integer.MIN_VALUE + 1300;

    int FRAMEWORK_DESENSITIZE = Integer.MIN_VALUE + 1400;

    int FRAMEWORK_LOG = Integer.MIN_VALUE + 1500;

    int FRAMEWORK_SWAGGER = Integer.MIN_VALUE + 1600;

    int FRAMEWORK_DATASOURCE = Integer.MIN_VALUE + 1700;

    int FRAMEWORK_MYBATIS = Integer.MIN_VALUE + 1800;

    int FRAMEWORK_REDIS = Integer.MIN_VALUE + 1900;

    int FRAMEWORK_REDIS_CACHE = Integer.MIN_VALUE + 1950;

    int FRAMEWORK_STORAGE = Integer.MIN_VALUE + 2000;

    int FRAMEWORK_JOB = Integer.MIN_VALUE + 2100;

    int FRAMEWORK_JOB_QUARTZ = Integer.MIN_VALUE + 2150;

    int FRAMEWORK_MONITOR = Integer.MIN_VALUE + 2200;

    int FRAMEWORK_BIZ_OPERATOR_LOG = Integer.MIN_VALUE + 3000;

    int FRAMEWORK_BANNER = Integer.MIN_VALUE + 10000;

}
