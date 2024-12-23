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
package org.dromara.visor.module.infra.handler.preference.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.visor.framework.common.handler.data.model.GenericsDataModel;

/**
 * 系统偏好模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 13:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemPreferenceModel implements GenericsDataModel {

    /**
     * 是否使用侧边菜单
     */
    private Boolean menu;

    /**
     * 是否使用顶部菜单
     */
    private Boolean topMenu;

    /**
     * 是否显示导航栏
     */
    private Boolean navbar;

    /**
     * 是否显示页脚
     */
    private Boolean footer;

    /**
     * 是否开启多页签
     */
    private Boolean tabBar;

    /**
     * 是否开启色弱模式
     */
    private Boolean colorWeak;

    /**
     * 菜单宽度
     */
    private Integer menuWidth;

    /**
     * 表格默认页数
     */
    private Integer defaultTablePageSize;

    /**
     * 卡片默认页数
     */
    private Integer defaultCardPageSize;

}
