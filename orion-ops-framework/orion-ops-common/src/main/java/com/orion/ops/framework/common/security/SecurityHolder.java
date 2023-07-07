package com.orion.ops.framework.common.security;

/**
 * SecurityUtils 的 bean 对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 15:20
 */
public interface SecurityHolder {

    /**
     * 获取当前用户
     *
     * @return 当前用户
     */
    LoginUser getLoginUser();

    /**
     * 获取当前用户id
     *
     * @return id
     */
    Long getLoginUserId();

}
