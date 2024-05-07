package com.orion.ops.module.asset.enums;

/**
 * 上传任务状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 22:21
 */
public enum UploadTaskStatusEnum {

    /**
     * 准备中
     */
    PREPARATION,

    /**
     * 上传中
     */
    UPLOADING,

    /**
     * 已完成
     */
    FINISHED,

    /**
     * 已取消
     */
    CANCELED,

    ;

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
