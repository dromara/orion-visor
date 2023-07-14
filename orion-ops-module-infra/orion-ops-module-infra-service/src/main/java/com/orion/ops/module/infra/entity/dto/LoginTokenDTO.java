package com.orion.ops.module.infra.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登陆 token 缓存
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
     * 状态
     */
    private Integer status;

    /**
     * 登陆时间
     */
    private Long loginTime;

    /**
     * 登陆 ip
     */
    private String ip;

    /**
     * 登陆地址
     */
    private String location;

}
