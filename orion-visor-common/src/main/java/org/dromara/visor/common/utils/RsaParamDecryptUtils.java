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
package org.dromara.visor.common.utils;

import cn.orionsec.kit.lang.utils.Exceptions;
import org.dromara.visor.common.cipher.RsaDecryptor;

/**
 * rsa 参数解密工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/5 21:13
 */
public class RsaParamDecryptUtils {

    private static RsaDecryptor delegate;

    private RsaParamDecryptUtils() {
    }

    /**
     * 解密
     *
     * @param value value
     * @return value
     */
    public static String decrypt(String value) {
        return delegate.decrypt(value);
    }

    public static void setDelegate(RsaDecryptor delegate) {
        if (RsaParamDecryptUtils.delegate != null) {
            // unmodified
            throw Exceptions.state();
        }
        RsaParamDecryptUtils.delegate = delegate;
    }

}
