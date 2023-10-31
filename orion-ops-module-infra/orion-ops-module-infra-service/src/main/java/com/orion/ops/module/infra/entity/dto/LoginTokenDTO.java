package com.orion.ops.module.infra.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录 token 缓存
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 16:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokenDTO {

    /**
     * 用户id
     */
    private Long id;

    /**
     * token 状态
     *
     * @see com.orion.ops.module.infra.enums.LoginTokenStatusEnum
     */
    private Integer status;

    /**
     * 已续签次数
     */
    private Integer refreshCount;

    /**
     * 原始登录身份
     */
    private Identity origin;

    /**
     * 覆盖登录身份
     */
    private Identity override;

    /**
     * 身份信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Identity {

        /**
         * 原始登录时间
         */
        private Long loginTime;

        /**
         * 当前设备登录地址
         */
        private String address;

        /**
         * 当前设备登录地址
         */
        private String location;

        /**
         * 当前设备 userAgent
         */
        private String userAgent;

    }

}
