package com.orion.visor.module.infra.api.impl;

import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.module.infra.api.SystemMessageApi;
import com.orion.visor.module.infra.convert.SystemMessageProviderConvert;
import com.orion.visor.module.infra.define.SystemMessageDefine;
import com.orion.visor.module.infra.entity.dto.message.SystemMessageCreateDTO;
import com.orion.visor.module.infra.entity.dto.message.SystemMessageDTO;
import com.orion.visor.module.infra.entity.request.message.SystemMessageCreateRequest;
import com.orion.visor.module.infra.enums.MessageClassifyEnum;
import com.orion.visor.module.infra.service.SystemMessageService;
import lombok.extern.slf4j.Slf4j;
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
