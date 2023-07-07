package com.orion.ops.framework.security.core.service;

import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.common.security.SecurityHolder;
import com.orion.ops.framework.security.core.utils.SecurityUtils;

/**
 * SecurityHolder 委托类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 15:33
 */
public class SecurityHolderDelegate implements SecurityHolder {

    @Override
    public LoginUser getLoginUser() {
        return SecurityUtils.getLoginUser();
    }

    @Override
    public Long getLoginUserId() {
        return SecurityUtils.getLoginUserId();
    }

}
