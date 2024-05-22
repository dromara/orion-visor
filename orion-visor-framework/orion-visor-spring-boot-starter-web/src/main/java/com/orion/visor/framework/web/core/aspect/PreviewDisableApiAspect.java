package com.orion.visor.framework.web.core.aspect;

import com.orion.visor.framework.common.constant.ErrorCode;
import com.orion.visor.framework.web.core.annotation.PreviewDisableApi;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 预览禁用 api 切面
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/21 16:52
 */
@Aspect
@Slf4j
public class PreviewDisableApiAspect {

    public PreviewDisableApiAspect() {
    }

    @Around("@annotation(o)")
    public Object around(ProceedingJoinPoint joinPoint, PreviewDisableApi o) {
        throw ErrorCode.PREVIEW_DISABLE_API.exception();
    }

}
