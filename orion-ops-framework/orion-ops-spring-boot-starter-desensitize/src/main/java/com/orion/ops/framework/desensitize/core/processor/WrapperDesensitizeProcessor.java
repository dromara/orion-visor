package com.orion.ops.framework.desensitize.core.processor;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.lang.define.wrapper.RpcWrapper;

/**
 * wrapper 对象脱敏处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 18:17
 */
public class WrapperDesensitizeProcessor extends ObjectDesensitizeProcessor {

    @Override
    public void execute(Object data) {
        if (data instanceof HttpWrapper<?>) {
            super.execute(((HttpWrapper<?>) data).getData());
        } else if (data instanceof RpcWrapper<?>) {
            super.execute(data);
        } else {
            super.execute(data);
        }
    }

}
