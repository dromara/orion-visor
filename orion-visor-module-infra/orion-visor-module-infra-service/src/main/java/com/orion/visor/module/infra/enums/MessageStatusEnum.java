package com.orion.visor.module.infra.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息状态
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024/5/11 16:35
 */
@Getter
@AllArgsConstructor
public enum MessageStatusEnum {

    /**
     * 未读
     */
    UNREAD(0),

    /**
     * 已读
     */
    READ(1),

    ;

    private final Integer status;

}
