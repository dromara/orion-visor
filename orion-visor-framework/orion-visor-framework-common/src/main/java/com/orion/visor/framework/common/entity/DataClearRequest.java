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
package com.orion.visor.framework.common.entity;

/**
 * 数据清理请求 定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/8/29 11:26
 */
public interface DataClearRequest {

    /**
     * 获取清理数量限制
     *
     * @return 清理限制
     */
    Integer getLimit();

    /**
     * 设置清理数量限制
     *
     * @param limit limit
     */
    void setLimit(Integer limit);

}
