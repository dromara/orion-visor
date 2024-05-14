package com.orion.ops.module.infra.api;

import com.orion.ops.module.infra.define.SystemMessageDefine;
import com.orion.ops.module.infra.entity.dto.message.SystemMessageCreateDTO;
import com.orion.ops.module.infra.entity.dto.message.SystemMessageDTO;
import com.orion.ops.module.infra.enums.MessageClassifyEnum;

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
