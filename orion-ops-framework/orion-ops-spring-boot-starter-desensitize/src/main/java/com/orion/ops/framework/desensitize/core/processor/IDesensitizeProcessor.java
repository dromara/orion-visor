package com.orion.ops.framework.desensitize.core.processor;

/**
 * 脱敏处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 17:50
 */
public interface IDesensitizeProcessor<T> {

    /**
     * 脱敏操作
     *
     * @param data data
     */
    void execute(T data);

}
