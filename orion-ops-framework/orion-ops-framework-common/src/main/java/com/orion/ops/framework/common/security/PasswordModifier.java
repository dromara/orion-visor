package com.orion.ops.framework.common.security;

import com.orion.lang.utils.Booleans;
import com.orion.lang.utils.Strings;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.utils.CryptoUtils;

/**
 * 密码修改器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/21 11:35
 */
public class PasswordModifier {

    private PasswordModifier() {
    }

    /**
     * 获取新密码
     *
     * @param action action
     * @return password
     */
    public static String getEncryptNewPassword(UpdatePasswordAction action) {
        if (Booleans.isTrue(action.getUseNewPassword())) {
            // 使用新密码
            String password = action.getPassword();
            if (Strings.isBlank(password)) {
                return Const.EMPTY;
            } else {
                return CryptoUtils.encryptAsString(password);
            }
        } else {
            return null;
        }
    }

}
