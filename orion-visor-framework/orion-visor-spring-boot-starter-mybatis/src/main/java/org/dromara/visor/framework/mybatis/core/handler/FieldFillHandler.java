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
package org.dromara.visor.framework.mybatis.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.dromara.visor.framework.mybatis.core.domain.BaseDO;
import org.dromara.visor.framework.mybatis.core.utils.DomainFillUtils;

import java.util.Objects;

/**
 * 字段填充处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/23 19:01
 */
public class FieldFillHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO) {
            // 填充插入
            DomainFillUtils.fillInsert((BaseDO) metaObject.getOriginalObject());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO) {
            // 填充更新
            DomainFillUtils.fillUpdate((BaseDO) metaObject.getOriginalObject());
        }
    }

}
