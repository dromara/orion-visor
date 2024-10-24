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
package com.orion.visor.framework.common.handler.data.strategy;

import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;

/**
 * 标准数据处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:09
 */
public interface GenericsDataStrategy<M extends GenericsDataModel> {

    /**
     * 获取默认值
     *
     * @return 默认值
     */
    M getDefault();

    /**
     * 执行完整验证链
     * <p>
     * preValid > updateFill > valid
     *
     * @param beforeModel beforeModel
     * @param afterModel  afterModel
     */
    void doValid(M beforeModel, M afterModel);

    /**
     * 解析数据
     *
     * @param serialModel serialModel
     * @return model
     */
    M parse(String serialModel);

    /**
     * 转为视图配置
     *
     * @param model model
     */
    void toView(M model);

}
