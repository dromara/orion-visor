package com.orion.visor.framework.web.core.annotation;

import java.lang.annotation.*;

/**
 * 预览禁用 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/21 16:44
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreviewDisableApi {
}
