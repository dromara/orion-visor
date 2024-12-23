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
package org.dromara.visor.framework.common.constant;

/**
 * 常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/23 18:49
 */
public interface Const extends cn.orionsec.kit.lang.constant.Const, FieldConst, CnConst {

    Integer NOT_DELETE = 0;

    Integer IS_DELETED = 1;

    int BEARER_PREFIX_LEN = 7;

    int MD5_LEN = 32;

    Long ROOT_PARENT_ID = 0L;

    Integer DEFAULT_SORT = 10;

    Long NONE_ID = -1L;

    Integer DEFAULT_VERSION = 1;

    Long SYSTEM_USER_ID = 0L;

    String SYSTEM_USERNAME = "system";

    int BATCH_COUNT = 500;

}
