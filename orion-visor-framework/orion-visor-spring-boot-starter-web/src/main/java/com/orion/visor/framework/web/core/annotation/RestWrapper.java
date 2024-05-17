package com.orion.visor.framework.web.core.annotation;

import java.lang.annotation.*;

/**
 * 统一返回包装
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see Void#TYPE
 * @see IgnoreWrapper
 * @since 2021/4/2 12:34
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestWrapper {

}
