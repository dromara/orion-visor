/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.framework.biz.operator.log.core.annotation;

import org.dromara.visor.framework.biz.operator.log.core.enums.ReturnType;

import java.lang.annotation.*;

/**
 * 操作日志
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/9 18:44
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperatorLog {

    /**
     * 操作类型
     */
    String value();

    /**
     * 是否记录参数
     * <p>
     * - {@link org.springframework.web.bind.annotation.RequestBody}
     * - {@link org.springframework.web.bind.annotation.RequestParam}
     * - {@link org.springframework.web.bind.annotation.RequestHeader}
     * - {@link org.springframework.web.bind.annotation.PathVariable}
     * <p>
     * 使用 @IgnoreParameter 可以忽略参数记录 {@link IgnoreParameter}
     * 如果只需要忽略对象中的某个字段可以使用 {@code @Desensitize(toEmpty = true)} 标注
     */
    boolean parameter() default true;

    /**
     * 返回值处理
     */
    ReturnType ret() default ReturnType.JSON;

}
