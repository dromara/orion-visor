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
package org.dromara.visor.module.infra.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.module.infra.api.SystemMessageApi;
import org.dromara.visor.module.infra.convert.SystemMessageProviderConvert;
import org.dromara.visor.module.infra.define.SystemMessageDefine;
import org.dromara.visor.module.infra.entity.dto.message.SystemMessageCreateDTO;
import org.dromara.visor.module.infra.entity.dto.message.SystemMessageDTO;
import org.dromara.visor.module.infra.entity.request.message.SystemMessageCreateRequest;
import org.dromara.visor.module.infra.enums.MessageClassifyEnum;
import org.dromara.visor.module.infra.service.SystemMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 系统消息 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Slf4j
@Service
public class SystemMessageApiImpl implements SystemMessageApi {

    @Resource
    private SystemMessageService systemMessageService;

    @Override
    public Long create(SystemMessageDefine define, SystemMessageDTO dto) {
        Valid.valid(dto);
        // 转换
        SystemMessageCreateRequest request = SystemMessageCreateRequest.builder()
                .classify(define.getClassify().name())
                .type(define.getType())
                .title(define.getTitle())
                .content(define.formatContent(dto.getParams()))
                .relKey(dto.getRelKey())
                .receiverId(dto.getReceiverId())
                .receiverUsername(dto.getReceiverUsername())
                .build();
        // 创建
        return systemMessageService.createSystemMessage(request);
    }

    @Override
    public Long create(MessageClassifyEnum classify, SystemMessageCreateDTO dto) {
        dto.setClassify(classify.name());
        Valid.valid(dto);
        // 转换
        SystemMessageCreateRequest request = SystemMessageProviderConvert.MAPPER.toRequest(dto);
        // 创建
        return systemMessageService.createSystemMessage(request);
    }

}
