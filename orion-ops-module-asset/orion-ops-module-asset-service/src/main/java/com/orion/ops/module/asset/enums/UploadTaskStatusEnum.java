package com.orion.ops.module.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传任务状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 22:21
 */
@Getter
@AllArgsConstructor
public enum UploadTaskStatusEnum {

    /**
     * 准备中
     */
    PREPARATION(true),

    /**
     * 上传中
     */
    UPLOADING(true),

    /**
     * 已完成
     */
    FINISHED(false),

    /**
     * 已取消
     */
    CANCELED(false),

    ;

    private final boolean cancelable;

    public static UploadTaskStatusEnum of(String status) {
        if (status == null) {
            return null;
        }
        for (UploadTaskStatusEnum value : values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }

}
