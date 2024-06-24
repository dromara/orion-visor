package com.orion.visor.framework.common.enums;

import com.orion.lang.utils.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 端点定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/21 19:15
 */
@Getter
@AllArgsConstructor
public enum EndpointDefine {

    /**
     * 上传临时分区
     */
    UPLOAD_SWAP("/upload/swap/{}"),

    ;

    /**
     * 端点
     */
    private final String endpoint;

    /**
     * 格式化
     *
     * @param params params
     * @return path
     */
    public String format(Object... params) {
        return Strings.format(this.endpoint, params);
    }

}
