package com.orion.ops.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 脱敏配置元注解
 * <p>
 * 标注在字段上则标记该字段执行 http 序列化时脱敏
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 16:58
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitize {

    /**
     * @return 起始保留字符数
     */
    int keepStart() default 1;

    /**
     * @return 结束保留字符数
     */
    int keepEnd() default 1;

    /**
     * @return 脱敏字符
     */
    char replacer() default '*';

}
