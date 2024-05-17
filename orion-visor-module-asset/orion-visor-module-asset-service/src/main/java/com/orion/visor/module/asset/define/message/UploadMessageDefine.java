package com.orion.visor.module.asset.define.message;

import com.orion.visor.module.infra.define.SystemMessageDefine;
import com.orion.visor.module.infra.enums.MessageClassifyEnum;
import lombok.Getter;

/**
 * 上传任务 系统消息定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/14 17:23
 */
@Getter
public enum UploadMessageDefine implements SystemMessageDefine {

    /**
     * 上传任务部分失败
     */
    UPLOAD_FAILED(MessageClassifyEnum.NOTICE,
            "批量上传失败",
            "您在 <sb>${time}</sb> 提交的上传任务中, 有部分主机文件上传失败。点击查看详情 <sb>#${id}</sb> >>>"),

    ;

    UploadMessageDefine(MessageClassifyEnum classify, String title, String content) {
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
