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
package org.dromara.visor.framework.mybatis.core.utils;

import cn.orionsec.kit.lang.utils.Exceptions;
import org.dromara.visor.common.security.SecurityHolder;
import org.dromara.visor.framework.mybatis.core.domain.BaseDO;

import java.util.Date;
import java.util.Objects;

/**
 * 对象填充器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/13 10:54
 */
public class DomainFillUtils {

    /**
     * 安全信息持有者
     */
    private static SecurityHolder securityHolder;

    /**
     * 填充插入
     *
     * @param baseDO baseDO
     */
    public static void fillInsert(BaseDO baseDO) {
        Date now = new Date();
        String username = securityHolder.getLoginUsername();
        // 创建时间
        if (Objects.isNull(baseDO.getCreateTime())) {
            baseDO.setCreateTime(now);
        }
        // 更新时间
        if (Objects.isNull(baseDO.getUpdateTime())) {
            baseDO.setUpdateTime(now);
        }
        // 创建人
        if (Objects.nonNull(username) && Objects.isNull(baseDO.getCreator())) {
            baseDO.setCreator(username);
        }
        // 更新人
        if (Objects.nonNull(username) && Objects.isNull(baseDO.getUpdater())) {
            baseDO.setUpdater(username);
        }
        // 逻辑删除字段
        if (Objects.isNull(baseDO.getDeleted())) {
            baseDO.setDeleted(false);
        }
    }

    /**
     * 填充更新
     *
     * @param baseDO baseDO
     */
    public static void fillUpdate(BaseDO baseDO) {
        // 更新时间
        if (Objects.isNull(baseDO.getUpdateTime())) {
            baseDO.setUpdateTime(new Date());
        }
        // 更新人
        String username = securityHolder.getLoginUsername();
        if (Objects.nonNull(username) && Objects.isNull(baseDO.getUpdater())) {
            baseDO.setUpdater(username);
        }
    }

    public static void setSecurityHolder(SecurityHolder securityHolder) {
        if (DomainFillUtils.securityHolder != null) {
            // unmodified
            throw Exceptions.state();
        }
        DomainFillUtils.securityHolder = securityHolder;
    }

}
