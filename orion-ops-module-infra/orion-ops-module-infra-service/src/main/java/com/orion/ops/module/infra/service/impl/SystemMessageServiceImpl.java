package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.function.Functions;
import com.orion.lang.utils.Booleans;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.infra.convert.SystemMessageConvert;
import com.orion.ops.module.infra.dao.SystemMessageDAO;
import com.orion.ops.module.infra.dao.SystemUserDAO;
import com.orion.ops.module.infra.entity.domain.SystemMessageDO;
import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import com.orion.ops.module.infra.entity.dto.SystemMessageCountDTO;
import com.orion.ops.module.infra.entity.request.message.SystemMessageCreateRequest;
import com.orion.ops.module.infra.entity.request.message.SystemMessageQueryRequest;
import com.orion.ops.module.infra.entity.vo.SystemMessageVO;
import com.orion.ops.module.infra.enums.MessageStatusEnum;
import com.orion.ops.module.infra.service.SystemMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 系统消息 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Slf4j
@Service
public class SystemMessageServiceImpl implements SystemMessageService {

    @Resource
    private SystemMessageDAO systemMessageDAO;

    @Resource
    private SystemUserDAO systemUserDAO;

    @Override
    public Long createSystemMessage(SystemMessageCreateRequest request) {
        log.info("SystemMessageService.createSystemMessage request: {}", JSON.toJSONString(request));
        // 设置接收人用户名
        if (request.getReceiverUsername() == null) {
            Optional.ofNullable(request.getReceiverId())
                    .map(systemUserDAO::selectById)
                    .map(SystemUserDO::getUsername)
                    .ifPresent(request::setReceiverUsername);
        }
        // 转换
        SystemMessageDO record = SystemMessageConvert.MAPPER.to(request);
        record.setStatus(MessageStatusEnum.UNREAD.getStatus());
        // 插入
        int effect = systemMessageDAO.insert(record);
        Long id = record.getId();
        log.info("SystemMessageService-createSystemMessage id: {}, effect: {}", id, effect);
        return id;
    }

    @Override
    public List<SystemMessageVO> getSystemMessageList(SystemMessageQueryRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        Integer status = Booleans.isTrue(request.getQueryUnread()) ? MessageStatusEnum.UNREAD.getStatus() : null;
        // 查询列表
        return systemMessageDAO.of()
                .createValidateWrapper()
                .eq(SystemMessageDO::getReceiverId, userId)
                .eq(SystemMessageDO::getClassify, request.getClassify())
                .lt(SystemMessageDO::getId, request.getMaxId())
                .eq(SystemMessageDO::getStatus, status)
                .last(Const.LIMIT + Const.SPACE + request.getLimit())
                .orderByDesc(SystemMessageDO::getId)
                .then()
                .list(SystemMessageConvert.MAPPER::to);
    }

    @Override
    public Map<String, Integer> getSystemMessageCount(Boolean queryUnread) {
        Long userId = SecurityUtils.getLoginUserId();
        Integer status = queryUnread ? MessageStatusEnum.UNREAD.getStatus() : null;
        // 查询数量
        List<SystemMessageCountDTO> countList = systemMessageDAO.selectSystemMessageCount(userId, status);
        // 返回
        return countList.stream()
                .collect(Collectors.toMap(SystemMessageCountDTO::getClassify,
                        SystemMessageCountDTO::getCount,
                        Functions.right()));
    }

    @Override
    public Boolean checkHasUnreadMessage() {
        // 查询
        return systemMessageDAO.of()
                .createWrapper()
                .select(SystemMessageDO::getId)
                .eq(SystemMessageDO::getReceiverId, SecurityUtils.getLoginUserId())
                .eq(SystemMessageDO::getStatus, MessageStatusEnum.UNREAD.getStatus())
                .then()
                .only()
                .optional()
                .isPresent();
    }

    @Override
    public Integer readSystemMessage(Long id) {
        Long userId = SecurityUtils.getLoginUserId();
        log.info("SystemMessageService-readSystemMessage id: {}, userId: {}", id, userId);
        // 修改状态
        SystemMessageDO update = new SystemMessageDO();
        update.setStatus(MessageStatusEnum.READ.getStatus());
        LambdaQueryWrapper<SystemMessageDO> wrapper = systemMessageDAO.wrapper()
                .eq(SystemMessageDO::getId, id)
                .eq(SystemMessageDO::getReceiverId, userId);
        int effect = systemMessageDAO.update(update, wrapper);
        log.info("SystemMessageService-readSystemMessage id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer readAllSystemMessage(String classify) {
        Long userId = SecurityUtils.getLoginUserId();
        log.info("SystemMessageService-readAllSystemMessage classify: {}, userId: {}", classify, userId);
        // 修改状态
        SystemMessageDO update = new SystemMessageDO();
        update.setStatus(MessageStatusEnum.READ.getStatus());
        LambdaQueryWrapper<SystemMessageDO> wrapper = systemMessageDAO.wrapper()
                .eq(SystemMessageDO::getReceiverId, userId)
                .eq(SystemMessageDO::getClassify, classify)
                .eq(SystemMessageDO::getStatus, MessageStatusEnum.UNREAD.getStatus());
        int effect = systemMessageDAO.update(update, wrapper);
        log.info("SystemMessageService-readAllSystemMessage classify: {}, effect: {}", classify, effect);
        return effect;
    }

    @Override
    public Integer deleteSystemMessageById(Long id) {
        log.info("SystemMessageService-deleteSystemMessageById id: {}", id);
        // 删除
        LambdaQueryWrapper<SystemMessageDO> wrapper = systemMessageDAO.wrapper()
                .eq(SystemMessageDO::getId, id)
                .eq(SystemMessageDO::getReceiverId, SecurityUtils.getLoginUserId());
        int effect = systemMessageDAO.delete(wrapper);
        log.info("SystemMessageService-deleteSystemMessageById id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer clearSystemMessage(String classify) {
        Long userId = SecurityUtils.getLoginUserId();
        log.info("SystemMessageService-clearSystemMessage classify: {}, userId: {}", classify, userId);
        // 删除
        LambdaQueryWrapper<SystemMessageDO> wrapper = systemMessageDAO.wrapper()
                .eq(SystemMessageDO::getReceiverId, userId)
                .eq(SystemMessageDO::getClassify, classify)
                .eq(SystemMessageDO::getStatus, MessageStatusEnum.READ.getStatus());
        int effect = systemMessageDAO.delete(wrapper);
        log.info("SystemMessageService-clearSystemMessage classify: {}, effect: {}", classify, effect);
        return effect;
    }

}
