package com.orion.ops.module.asset.handler.host.terminal.enums;

import com.alibaba.fastjson.JSONObject;
import com.orion.ops.module.asset.handler.host.terminal.handler.*;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalBasePayload;
import com.orion.ops.module.asset.handler.host.terminal.model.request.*;
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
public enum InputTypeEnum {

    /**
     * 主机连接检查
     */
    CHECK("ck",
            TerminalCheckHandler.class,
            new String[]{"type", "sessionId", "hostId"},
            TerminalCheckRequest.class),

    /**
     * 连接主机
     */
    CONNECT("co",
            TerminalConnectHandler.class,
            new String[]{"type", "sessionId", "cols", "rows"},
            TerminalConnectRequest.class),

    /**
     * 关闭连接
     */
    CLOSE("cl",
            TerminalCloseHandler.class,
            new String[]{"type", "sessionId"},
            TerminalBasePayload.class),

    /**
     * ping
     */
    PING("p",
            TerminalPingHandler.class,
            new String[]{"type"},
            TerminalBasePayload.class),

    /**
     * 修改大小
     */
    RESIZE("rs",
            TerminalResizeHandler.class,
            new String[]{"type", "sessionId", "cols", "rows"},
            TerminalResizeRequest.class),

    /**
     * 执行
     */
    EXEC("e",
            TerminalExecHandler.class,
            new String[]{"type", "sessionId", "command"},
            TerminalExecRequest.class),

    /**
     * 输入
     */
    INPUT("i",
            TerminalInputHandler.class,
            new String[]{"type", "sessionId", "command"},
            TerminalInputRequest.class),

    ;

    private static final char SEPARATOR = '|';

    @Getter
    private final String type;

    private final Class<? extends ITerminalHandler<? extends TerminalBasePayload>> handlerBean;

    private final String[] payloadDefine;

    private final Class<? extends TerminalBasePayload> payloadClass;

    @Getter
    private ITerminalHandler<? extends TerminalBasePayload> handler;

    <T extends TerminalBasePayload> InputTypeEnum(String type,
                                                  Class<? extends ITerminalHandler<T>> handlerBean,
                                                  String[] payloadDefine,
                                                  Class<T> payloadClass) {
        this.type = type;
        this.handlerBean = handlerBean;
        this.payloadDefine = payloadDefine;
        this.payloadClass = payloadClass;
    }

    public static InputTypeEnum of(String payload) {
        if (payload == null) {
            return null;
        }
        for (InputTypeEnum value : values()) {
            if (payload.startsWith(value.type + SEPARATOR) || payload.equals(value.type)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 解析请求
     *
     * @param payload payload
     * @param <T>     T
     * @return payload
     */
    @SuppressWarnings("unchecked")
    public <T extends TerminalBasePayload> T parse(String payload) {
        JSONObject object = new JSONObject();
        int curr = 0;
        int len = payload.length();
        for (int i = 0, pl = payloadDefine.length; i < pl; i++) {
            if (i == pl - 1) {
                // 最后一次
                object.put(payloadDefine[i], payload.substring(curr, len));
            } else {
                // 非最后一次
                StringBuilder tmp = new StringBuilder();
                for (; curr < len; curr++) {
                    char c = payload.charAt(curr);
                    if (c == SEPARATOR) {
                        object.put(payloadDefine[i], tmp.toString());
                        curr++;
                        break;
                    } else {
                        tmp.append(c);
                    }
                }
            }
        }
        return (T) object.toJavaObject(payloadClass);
    }

    /**
     * 类型字段定义
     */
    @Component
    static class TypeFieldDefinition {

        @PostConstruct
        public void init() {
            for (InputTypeEnum value : InputTypeEnum.values()) {
                value.handler = SpringHolder.getBean(value.handlerBean);
            }
        }

    }

}
