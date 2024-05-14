package com.orion.ops.module.infra.api.impl;

import com.alibaba.fastjson.JSON;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.infra.api.SystemMessageApi;
import com.orion.ops.module.infra.convert.SystemMessageProviderConvert;
import com.orion.ops.module.infra.entity.dto.message.SystemMessageCreateDTO;
import com.orion.ops.module.infra.entity.request.message.SystemMessageCreateRequest;
import com.orion.ops.module.infra.enums.MessageClassifyEnum;
import com.orion.ops.module.infra.service.SystemMessageService;
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
    public Long createSystemMessage(MessageClassifyEnum classify, SystemMessageCreateDTO dto) {
        log.info("SystemMessageApi.createSystemMessage dto: {}", JSON.toJSONString(dto));
        Valid.valid(dto);
        // 转换
        SystemMessageCreateRequest request = SystemMessageProviderConvert.MAPPER.toRequest(dto);
        request.setClassify(classify.name());
        // 创建
        return systemMessageService.createSystemMessage(request);
    }

}
