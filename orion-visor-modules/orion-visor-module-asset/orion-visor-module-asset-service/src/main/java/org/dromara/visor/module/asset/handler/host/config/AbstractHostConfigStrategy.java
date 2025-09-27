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
package org.dromara.visor.module.asset.handler.host.config;

import cn.orionsec.kit.lang.utils.Booleans;
import cn.orionsec.kit.lang.utils.Charsets;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.handler.data.model.GenericsDataModel;
import org.dromara.visor.common.handler.data.strategy.AbstractGenericsDataStrategy;
import org.dromara.visor.common.security.UpdatePasswordAction;
import org.dromara.visor.common.utils.AesEncryptUtils;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.common.utils.RsaParamDecryptUtils;
import org.dromara.visor.module.asset.enums.HostAuthTypeEnum;

/**
 * 主机配置策略基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/31 19:44
 */
public abstract class AbstractHostConfigStrategy<T extends GenericsDataModel> extends AbstractGenericsDataStrategy<T> {

    public AbstractHostConfigStrategy(Class<T> modelClass) {
        super(modelClass);
    }

    /**
     * 检查加密密码
     *
     * @param before before
     * @param after  after
     */
    protected void checkEncryptPassword(String authType, UpdatePasswordAction before, UpdatePasswordAction after) {
        // 非密码认证/使用原始密码则直接赋值
        if (!HostAuthTypeEnum.PASSWORD.name().equals(authType) || !Booleans.isTrue(after.getUseNewPassword())) {
            if (before != null) {
                after.setPassword(before.getPassword());
            }
            return;
        }
        // 检查新密码
        String newPassword = Assert.notBlank(after.getPassword(), ErrorMessage.PASSWORD_MISSING);
        // 解密密码
        newPassword = RsaParamDecryptUtils.decrypt(newPassword);
        Assert.notBlank(newPassword, ErrorMessage.DECRYPT_ERROR);
        // 设置密码
        after.setPassword(AesEncryptUtils.encryptAsString(newPassword));
    }

    /**
     * 检查编码格式
     *
     * @param charset charset
     */
    protected void validCharset(String charset) {
        Assert.isTrue(Charsets.isSupported(charset), ErrorMessage.UNSUPPORTED_CHARSET, charset);
    }

}
