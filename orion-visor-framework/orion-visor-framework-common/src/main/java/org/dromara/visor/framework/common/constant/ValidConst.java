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
package org.dromara.visor.framework.common.constant;

/**
 * 验证常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/20 11:45
 */
public interface ValidConst {

    String USERNAME_4_32_PATTERN = "^[a-zA-Z0-9_]{4,32}$";

    String USERNAME_4_32_MESSAGE = "只能为 4-32 位的数字,字母或下滑线";

    String DICT_1_32_PATTERN = "^[a-zA-Z0-9_]{1,32}$";

    String DICT_1_32_MESSAGE = "只能为 1-32 位的数字,字母或下滑线";

}
