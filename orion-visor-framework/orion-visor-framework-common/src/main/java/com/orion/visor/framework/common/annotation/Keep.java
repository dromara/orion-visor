package com.orion.visor.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 保留
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/6 15:26
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Keep {

}
