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
package org.dromara.visor.framework.common.constant;

/**
 * bean 排序常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 16:09
 */
public interface BeanOrderConst {

    /**
     * 公共返回值包装处理器
     */
    int RESPONSE_ADVICE_WRAPPER = Integer.MIN_VALUE + 1000;

    /**
     * 演示模式切面
     */
    int DEMO_DISABLE_API_ASPECT = Integer.MIN_VALUE + 10;

    /**
     * 全局日志打印
     */
    int LOG_PRINT_ASPECT = Integer.MIN_VALUE + 20;

    /**
     * 操作日志切面
     */
    int OPERATOR_LOG_ASPECT = Integer.MIN_VALUE + 30;

}
