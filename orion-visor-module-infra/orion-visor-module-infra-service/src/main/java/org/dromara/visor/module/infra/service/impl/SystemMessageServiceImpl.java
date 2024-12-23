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
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.function.Functions;
import cn.orionsec.kit.lang.utils.Booleans;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.infra.convert.SystemMessageConvert;
import org.dromara.visor.module.infra.dao.SystemMessageDAO;
import org.dromara.visor.module.infra.dao.SystemUserDAO;
import org.dromara.visor.module.infra.entity.domain.SystemMessageDO;
import org.dromara.visor.module.infra.entity.domain.SystemUserDO;
import org.dromara.visor.module.infra.entity.po.SystemMessageCountPO;
import org.dromara.visor.module.infra.entity.request.message.SystemMessageCreateRequest;
import org.dromara.visor.module.infra.entity.request.message.SystemMessageQueryRequest;
import org.dromara.visor.module.infra.entity.vo.SystemMessageVO;
import org.dromara.visor.module.infra.enums.MessageStatusEnum;
import org.dromara.visor.module.infra.service.SystemMessageService;
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
                .orderByDesc(SystemMessageDO::getId)
                .then()
                .limit(request.getLimit())
                .list(SystemMessageConvert.MAPPER::to);
    }

    @Override
    public Map<String, Integer> getSystemMessageCount(Boolean queryUnread) {
        Long userId = SecurityUtils.getLoginUserId();
        Integer status = queryUnread ? MessageStatusEnum.UNREAD.getStatus() : null;
        // 查询数量
        List<SystemMessageCountPO> countList = systemMessageDAO.selectSystemMessageCount(userId, status);
        // 返回
        return countList.stream()
                .collect(Collectors.toMap(SystemMessageCountPO::getClassify,
                        SystemMessageCountPO::getCount,
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
