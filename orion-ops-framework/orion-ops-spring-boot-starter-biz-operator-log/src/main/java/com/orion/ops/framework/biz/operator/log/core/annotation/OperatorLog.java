package com.orion.ops.framework.biz.operator.log.core.annotation;

import com.orion.ops.framework.biz.operator.log.core.enums.ReturnType;

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
     * 返回值处理
     */
    ReturnType ret() default ReturnType.JSON;

}
