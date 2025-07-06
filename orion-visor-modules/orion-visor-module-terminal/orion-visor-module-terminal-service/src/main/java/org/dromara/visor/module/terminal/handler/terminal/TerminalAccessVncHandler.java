package org.dromara.visor.module.terminal.handler.terminal;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.terminal.handler.terminal.enums.InputProtocolEnum;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.sender.IGuacdTerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.sender.WebsocketGuacdTerminalSender;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * VNC 终端处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/28 14:33
 */
@Slf4j
@Component
public class TerminalAccessVncHandler extends AbstractTerminalAccessHandler<IGuacdTerminalSender> {

    @Override
    protected IGuacdTerminalSender createSender(WebSocketSession channel) {
        return new WebsocketGuacdTerminalSender(channel);
    }

    @Override
    protected void handleMessage(WebSocketSession channel, TextMessage message, TerminalChannelProps props, IGuacdTerminalSender sender) {
        String payload = message.getPayload();
        try {
            // 解析类型
            InputProtocolEnum type = InputProtocolEnum.of(payload);
            if (type == null) {
                return;
            }
            // 解析并处理消息
            type.getHandler().handle(props, sender, type.parse(payload));
        } catch (Exception e) {
            log.error("TerminalAccessVncHandler-handleMessage-error id: {}, msg: {}", channel.getId(), payload, e);
        }
    }

}
