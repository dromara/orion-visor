package com.orion.ops.framework.desensitize.core.handler;

import com.orion.lang.define.wrapper.Wrapper;
import com.orion.ops.framework.common.constant.ResponseAdviceOrderConst;
import com.orion.ops.framework.common.annotation.DoDesensitize;
import com.orion.ops.framework.desensitize.core.processor.IDesensitizeProcessor;
import com.orion.ops.framework.desensitize.core.processor.ObjectDesensitizeProcessor;
import com.orion.ops.framework.desensitize.core.processor.WrapperDesensitizeProcessor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 返回结果脱敏处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/15 17:38
 */
@Order(ResponseAdviceOrderConst.DESENSITIZE)
@ControllerAdvice
public class DesensitizeResultHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, @NotNull Class converterType) {
        return methodParameter.hasMethodAnnotation(DoDesensitize.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, @NotNull MethodParameter methodParameter, @NotNull MediaType selectedContentType, @NotNull Class selectedConverterType,
                                  @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {
        IDesensitizeProcessor processor;
        if (body instanceof Wrapper<?>) {
            // wrapper
            processor = new WrapperDesensitizeProcessor();
        } else {
            // 普通对象
            processor = new ObjectDesensitizeProcessor();
        }
        // 执行脱敏
        processor.execute(body);
        return body;
    }

}
