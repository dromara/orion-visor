package com.orion.ops.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 脱敏配置元注解
 * <p>
 * 标注在方法上则标记过滤开启
 * 多层对象脱敏需要在字段上标注
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 16:58
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DoDesensitize {

}
