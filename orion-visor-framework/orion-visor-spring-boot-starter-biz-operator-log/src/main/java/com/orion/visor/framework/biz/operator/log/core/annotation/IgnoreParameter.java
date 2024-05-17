package com.orion.visor.framework.biz.operator.log.core.annotation;

import java.lang.annotation.*;

/**
 * 用于记录操作日志时候 忽略参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/11 17:45
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreParameter {

}
