/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.framework.common.handler.data.strategy;

import com.alibaba.fastjson.JSON;
import org.dromara.visor.framework.common.handler.data.model.GenericsDataModel;

/**
 * 标准数据处理策略 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/11 21:44
 */
public abstract class AbstractGenericsDataStrategy<M extends GenericsDataModel> implements GenericsDataStrategy<M> {

    protected final Class<M> modelClass;

    public AbstractGenericsDataStrategy(Class<M> modelClass) {
        this.modelClass = modelClass;
    }

    /**
     * 更新填充
     *
     * @param beforeModel 修改前的配置
     * @param afterModel  修改后的配置
     */
    protected void updateFill(M beforeModel, M afterModel) {
    }

    /**
     * 预校验参数
     *
     * @param model model
     */
    protected void preValid(M model) {
    }

    /**
     * 校验参数
     *
     * @param model model
     */
    protected void valid(M model) {
    }

    @Override
    public void doValid(M beforeModel, M afterModel) {
        // 预校验参数
        this.preValid(afterModel);
        // 更新填充
        this.updateFill(beforeModel, afterModel);
        // 校验参数
        this.valid(afterModel);
    }

    @Override
    public M parse(String serialModel) {
        return JSON.parseObject(serialModel, modelClass);
    }

    @Override
    public void toView(M model) {
    }

}
