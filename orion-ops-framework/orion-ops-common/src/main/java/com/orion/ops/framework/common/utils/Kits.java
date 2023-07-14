package com.orion.ops.framework.common.utils;

import com.orion.lang.utils.Strings;
import com.orion.ops.framework.common.constant.Const;

/**
 * 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 16:34
 */
public class Kits {

    private Kits() {
    }

    /**
     * 获取登陆凭证
     *
     * @param authorization authorization
     * @return token
     */
    public static String getAuthorization(String authorization) {
        if (Strings.isEmpty(authorization)) {
            return null;
        }
        if (!authorization.contains(Const.BEARER) || authorization.length() <= Const.BEARER_PREFIX_LEN) {
            return null;
        }
        return authorization.substring(Const.BEARER_PREFIX_LEN).trim();
    }

}
