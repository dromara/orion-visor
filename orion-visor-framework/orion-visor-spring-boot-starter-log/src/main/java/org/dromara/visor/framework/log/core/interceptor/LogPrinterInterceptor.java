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
package org.dromara.visor.framework.log.core.interceptor;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * 日志打印拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 10:36
 */
public interface LogPrinterInterceptor extends MethodInterceptor {

    String EMPTY_TAG = "<EMPTY>";

    String ERROR_TAG = "<ERROR>";

    String VOID_TAG = "<VOID>";

    String NULL_TAG = "<NULL>";

    String IGNORED_TAG = "<IGNORED>";

    /**
     * 初始化
     */
    void init();

}
