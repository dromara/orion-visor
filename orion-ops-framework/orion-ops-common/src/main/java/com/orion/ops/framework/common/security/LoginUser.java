package com.orion.ops.framework.common.security;

import lombok.Data;

import java.util.List;

/**
 * 当前登录用户
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 18:36
 */
@Data
public class LoginUser {

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 花名
     */
    private String nickname;

    /**
     * 角色
     */
    private List<String> roles;

}
