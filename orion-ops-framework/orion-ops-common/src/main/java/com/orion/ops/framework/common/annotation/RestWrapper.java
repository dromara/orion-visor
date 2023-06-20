package com.orion.ops.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 统一返回包装
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/2 12:34
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestWrapper {

}
