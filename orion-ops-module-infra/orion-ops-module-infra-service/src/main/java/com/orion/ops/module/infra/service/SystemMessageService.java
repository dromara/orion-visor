package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.request.message.SystemMessageCreateRequest;
import com.orion.ops.module.infra.entity.request.message.SystemMessageQueryRequest;
import com.orion.ops.module.infra.entity.vo.SystemMessageVO;

import java.util.List;
import java.util.Map;

/**
 * 系统消息 服务类
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
public interface SystemMessageService {

    /**
     * 创建系统消息
     *
     * @param request request
     * @return id
     */
    Long createSystemMessage(SystemMessageCreateRequest request);

    /**
     * 查询系统消息列表
     *
     * @param request request
     * @return rows
     */
    List<SystemMessageVO> getSystemMessageList(SystemMessageQueryRequest request);

    /**
     * 查询系统消息数量
     *
     * @param queryUnread queryUnread
     * @return rows
     */
    Map<String, Integer> getSystemMessageCount(Boolean queryUnread);

    /**
     * 查询是否有未读消息
     *
     * @return has
     */
    Boolean checkHasUnreadMessage();

    /**
     * 更新系统消息为已读
     *
     * @param id id
     * @return effect
     */
    Integer readSystemMessage(Long id);

    /**
     * 更新全部系统消息为已读
     *
     * @param classify classify
     * @return effect
     */
    Integer readAllSystemMessage(String classify);

    /**
     * 删除系统消息
     *
     * @param id id
     * @return effect
     */
    Integer deleteSystemMessageById(Long id);

    /**
     * 清理已读的系统消息
     *
     * @param classify classify
     * @return effect
     */
    Integer clearSystemMessage(String classify);

}
