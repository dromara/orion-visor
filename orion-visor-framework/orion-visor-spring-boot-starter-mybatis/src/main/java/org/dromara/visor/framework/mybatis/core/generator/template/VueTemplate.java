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
package org.dromara.visor.framework.mybatis.core.generator.template;

/**
 * 前端代码模板
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/26 1:17
 */
public class VueTemplate extends Template {

    public VueTemplate(Table table) {
        super(table);
        table.enableVue = true;
    }

    public VueTemplate(Table table, String module, String feature) {
        super(table);
        table.enableVue = true;
        table.enableRowSelection = true;
        table.module = module;
        table.feature = feature;
    }

    /**
     * 设置模块 用于文件名称生成
     *
     * @param module module
     * @return this
     */
    public VueTemplate module(String module) {
        table.module = module;
        return this;
    }

    /**
     * 设置功能 用于文件名称生成
     *
     * @param feature feature
     * @return this
     */
    public VueTemplate feature(String feature) {
        table.feature = feature;
        return this;
    }

    /**
     * 使用抽屉表单
     *
     * @return this
     */
    public VueTemplate enableDrawerForm() {
        table.enableDrawerForm = true;
        return this;
    }

    /**
     * 关闭列表可多选
     *
     * @return this
     */
    public VueTemplate disableRowSelection() {
        table.enableRowSelection = false;
        return this;
    }

    /**
     * 启用卡片列表
     *
     * @return this
     */
    public VueTemplate enableCardView() {
        table.enableCardView = true;
        return this;
    }

}
