package com.orion.visor.framework.web.core.aspect;

import com.orion.visor.framework.common.constant.BeanOrderConst;
import com.orion.visor.framework.common.constant.ErrorCode;
import com.orion.visor.framework.web.core.annotation.DemoDisableApi;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

/**
 * 演示模式禁用 api 切面
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/21 16:52
 */
@Aspect
@Slf4j
@Order(BeanOrderConst.DEMO_DISABLE_API_ASPECT)
public class DemoDisableApiAspect {

    public DemoDisableApiAspect() {
    }

    @Pointcut("@annotation(e)")
    public void disableApi(DemoDisableApi e) {
    }

    @Before(value = "disableApi(e)", argNames = "e")
    public void beforeDisableApi(DemoDisableApi e) {
        throw ErrorCode.DEMO_DISABLE_API.exception();
    }

}
