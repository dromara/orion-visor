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
package org.dromara.visor.framework.web.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.dromara.visor.framework.common.constant.BeanOrderConst;
import org.dromara.visor.framework.common.constant.ErrorCode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.springframework.core.annotation.Order;

/**
 * 演示模式禁用 api 切面
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/21 16:52
 */
@Aspect
@Slf4j
@Order(BeanOrderConst.DEMO_DISABLE_API_ASPECT)
public class DemoDisableApiAspect {

    @Pointcut("@annotation(e)")
    public void disableApi(DemoDisableApi e) {
    }

    @Before(value = "disableApi(e)", argNames = "e")
    public void beforeDisableApi(DemoDisableApi e) {
        throw ErrorCode.DEMO_DISABLE_API.exception();
    }

}
