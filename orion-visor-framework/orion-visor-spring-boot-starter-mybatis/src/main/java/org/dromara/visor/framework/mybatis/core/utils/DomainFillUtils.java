/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
import org.dromara.visor.framework.common.security.SecurityHolder;
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
        // 创建时间
        if (Objects.isNull(baseDO.getCreateTime())) {
            baseDO.setCreateTime(now);
        }
        // 更新时间
        if (Objects.isNull(baseDO.getUpdateTime())) {
            baseDO.setUpdateTime(now);
        }

        Long userId = securityHolder.getLoginUserId();
        // 创建人
        if (Objects.nonNull(userId) && Objects.isNull(baseDO.getCreator())) {
            baseDO.setCreator(userId.toString());
        }
        // 更新人
        if (Objects.nonNull(userId) && Objects.isNull(baseDO.getUpdater())) {
            baseDO.setUpdater(userId.toString());
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
        Long userId = securityHolder.getLoginUserId();
        if (Objects.nonNull(userId) && Objects.isNull(baseDO.getUpdater())) {
            baseDO.setUpdater(userId.toString());
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
