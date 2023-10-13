package com.orion.ops.framework.biz.operator.log.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 模块
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/9 18:44
 */
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Module {

    /**
     * 模块
     */
    String value();

}
