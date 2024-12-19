/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.framework.common.handler.data;

import cn.orionsec.kit.spring.SpringHolder;
import org.dromara.visor.framework.common.handler.data.model.GenericsDataModel;
import org.dromara.visor.framework.common.handler.data.strategy.GenericsDataStrategy;

/**
 * 标准数据定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/21 0:07
 */
@SuppressWarnings("unchecked")
public interface GenericsDataDefinition {

    /**
     * 获取数据处理策略
     *
     * @return class
     */
    Class<? extends GenericsDataStrategy<? extends GenericsDataModel>> getStrategyClass();

    /**
     * 获取数据模型策略处理器
     *
     * @param <M> Model
     * @param <S> Strategy
     * @return Strategy Bean
     */
    default <M extends GenericsDataModel, S extends GenericsDataStrategy<M>> S getStrategy() {
        return (S) SpringHolder.getBean(this.getStrategyClass());
    }

    /**
     * 反序列化对象
     *
     * @param serialModel serialModel
     * @param <M>         Model
     * @return object
     */
    default <M extends GenericsDataModel> M parse(String serialModel) {
        return (M) this.getStrategy().parse(serialModel);
    }

    /**
     * 转为视图对象
     *
     * @param serialModel serialModel
     * @param <M>         Model
     * @return viewModel
     */
    default <M extends GenericsDataModel> M toView(String serialModel) {
        GenericsDataStrategy<GenericsDataModel> strategy = this.getStrategy();
        GenericsDataModel model = strategy.parse(serialModel);
        strategy.toView(model);
        return (M) model;
    }

}
