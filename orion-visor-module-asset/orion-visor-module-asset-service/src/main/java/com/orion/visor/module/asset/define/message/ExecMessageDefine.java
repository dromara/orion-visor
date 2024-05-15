package com.orion.visor.module.asset.define.message;

import com.orion.visor.module.infra.define.SystemMessageDefine;
import com.orion.visor.module.infra.enums.MessageClassifyEnum;
import lombok.Getter;

/**
 * 命令执行 系统消息定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/14 17:23
 */
@Getter
public enum ExecMessageDefine implements SystemMessageDefine {

    /**
     * 命令执行部分失败
     */
    EXEC_FAILED(MessageClassifyEnum.NOTICE,
            "命令执行失败",
            "您在 <sb>${time}</sb> 执行的命令部分失败, 或者返回了非零的 exitCode。点击查看详情 <sb>#${id}</sb> >>>"),

    ;

    ExecMessageDefine(MessageClassifyEnum classify, String title, String content) {
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
