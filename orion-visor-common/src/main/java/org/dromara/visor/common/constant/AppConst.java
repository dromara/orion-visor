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
package org.dromara.visor.common.constant;

import cn.orionsec.kit.lang.constant.OrionConst;

/**
 * 项目常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/19 18:56
 */
public interface AppConst extends OrionConst {

    /**
     * 同 ${orion.version} 迭代时候需要手动更改
     */
    String VERSION = "2.5.4";

    /**
     * 同 ${spring.application.name}
     */
    String APP_NAME = "orion-visor";

    String GITHUB = "https://github.com/dromara/orion-visor";

    String GITEE = "https://gitee.com/dromara/orion-visor";

    String ISSUES = "https://github.com/dromara/orion-visor/issues";

}
