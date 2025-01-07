/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.common.security;

import cn.orionsec.kit.lang.utils.Booleans;
import cn.orionsec.kit.lang.utils.Strings;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.utils.CryptoUtils;

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
