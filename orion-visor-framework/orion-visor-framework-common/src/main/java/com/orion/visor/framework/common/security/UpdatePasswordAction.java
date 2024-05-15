package com.orion.visor.framework.common.security;

import java.io.Serializable;

/**
 * 更新密码操作
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/21 11:32
 */
public interface UpdatePasswordAction extends Serializable {

    /**
     * 是否使用新密码
     *
     * @return use
     */
    Boolean getUseNewPassword();

    /**
     * 获取密码
     *
     * @return password
     */
    String getPassword();

}
