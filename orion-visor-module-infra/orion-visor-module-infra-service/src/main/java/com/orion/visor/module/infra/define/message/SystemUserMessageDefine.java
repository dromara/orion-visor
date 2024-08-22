package com.orion.visor.module.infra.define.message;

import com.orion.visor.module.infra.define.SystemMessageDefine;
import com.orion.visor.module.infra.enums.MessageClassifyEnum;
import lombok.Getter;

/**
 * 用户 系统消息定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/14 17:23
 */
@Getter
public enum SystemUserMessageDefine implements SystemMessageDefine {

    /**
     * 登录失败
     */
    LOGIN_FAILED(MessageClassifyEnum.NOTICE,
            "登录失败",
            "您的账号在 <sb>${time}</sb> 登录系统时身份认证失败, 您的密码可能已经泄漏。如非本人操作请尽快修改密码。(<sb>${address} - ${location}</sb>)"),

    ;

    SystemUserMessageDefine(MessageClassifyEnum classify, String title, String content) {
        this.classify = classify;
        this.type = this.name();
        this.title = title;
        this.content = content;
    }

    /**
     * 消息分类
     */
    private final MessageClassifyEnum classify;

    /**
     * 消息类型
     */
    private final String type;

    /**
     * 标题
     */
    private final String title;

    /**
     * 内容
     */
    private final String content;

}
