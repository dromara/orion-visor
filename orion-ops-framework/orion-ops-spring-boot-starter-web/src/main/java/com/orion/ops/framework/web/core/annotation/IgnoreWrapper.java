package com.orion.ops.framework.web.core.annotation;

import java.lang.annotation.*;

/**
 * 无需包装返回
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/2 10:36
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreWrapper {
}
