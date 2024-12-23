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
package org.dromara.visor.module.infra.api;

import org.dromara.visor.module.infra.define.SystemMessageDefine;
import org.dromara.visor.module.infra.entity.dto.message.SystemMessageCreateDTO;
import org.dromara.visor.module.infra.entity.dto.message.SystemMessageDTO;
import org.dromara.visor.module.infra.enums.MessageClassifyEnum;

/**
 * 系统消息 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
public interface SystemMessageApi {

    /**
     * 创建系统消息
     *
     * @param define define
     * @param dto    dto
     * @return id
     */
    Long create(SystemMessageDefine define, SystemMessageDTO dto);

    /**
     * 创建系统消息
     *
     * @param classify classify
     * @param dto      dto
     * @return id
     */
    Long create(MessageClassifyEnum classify, SystemMessageCreateDTO dto);

}
