package com.orion.ops.module.asset.handler.host.terminal.enums;

import com.orion.ops.module.asset.handler.host.terminal.handler.ITerminalHandler;
import com.orion.ops.module.asset.handler.host.terminal.handler.TerminalCheckHandler;
import com.orion.ops.module.asset.handler.host.terminal.handler.TerminalConnectHandler;
import com.orion.spring.SpringHolder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 输入操作类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:33
 */
public enum InputOperatorTypeEnum {

    /**
     * 主机连接检查 (临时 token 换实际 token / 检查权限)
     */
    CHECK("ck", TerminalCheckHandler.class),

    /**
     * 连接主机
     */
    CONNECT("co", TerminalConnectHandler.class),

    /**
     * 关闭连接
     */
    CLOSE("cl", TerminalConnectHandler.class),

    /**
     * ping
     */
    PING("p", TerminalConnectHandler.class),

    /**
     * 修改大小
     */
    RESIZE("rs", TerminalConnectHandler.class),

    /**
     * 执行
     */
    EXEC("e", TerminalConnectHandler.class),

    /**
     * 输入
     */
    INPUT("i", TerminalConnectHandler.class),

    ;

    @Getter
    private final String type;

    private final Class<? extends ITerminalHandler> handlerBean;

    @Getter
    private ITerminalHandler handler;

    InputOperatorTypeEnum(String type, Class<? extends ITerminalHandler> handlerBean) {
        this.type = type;
        this.handlerBean = handlerBean;
    }

    public static InputOperatorTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (InputOperatorTypeEnum value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 类型字段定义
     */
    @Component
    static class TypeFieldDefinition {

        @PostConstruct
        public void init() {
            for (InputOperatorTypeEnum value : InputOperatorTypeEnum.values()) {
                value.handler = SpringHolder.getBean(value.handlerBean);
            }
        }

    }

}
