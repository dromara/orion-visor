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

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.redis.core.utils.RedisMaps;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.redis.core.utils.RedisUtils;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.module.infra.convert.NotifyTemplateConvert;
import org.dromara.visor.module.infra.dao.NotifyTemplateDAO;
import org.dromara.visor.module.infra.define.cache.NotifyTemplateCacheKeyDefine;
import org.dromara.visor.module.infra.entity.domain.NotifyTemplateDO;
import org.dromara.visor.module.infra.entity.dto.NotifyTemplateCacheDTO;
import org.dromara.visor.module.infra.entity.dto.NotifyTemplateDetailCacheDTO;
import org.dromara.visor.module.infra.entity.request.notify.NotifyTemplateCreateRequest;
import org.dromara.visor.module.infra.entity.request.notify.NotifyTemplateQueryRequest;
import org.dromara.visor.module.infra.entity.request.notify.NotifyTemplateUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.NotifyTemplateVO;
import org.dromara.visor.module.infra.service.NotifyTemplateService;
import org.dromara.visor.module.monitor.api.AlarmPolicyNotifyApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知模板 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-13 21:05
 */
@Slf4j
@Service
public class NotifyTemplateServiceImpl implements NotifyTemplateService {

    @Resource
    private NotifyTemplateDAO notifyTemplateDAO;

    @Resource
    private AlarmPolicyNotifyApi alarmPolicyNotifyApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createNotifyTemplate(NotifyTemplateCreateRequest request) {
        log.info("NotifyTemplateService-createNotifyTemplate request: {}", JSON.toJSONString(request));
        // 转换
        NotifyTemplateDO record = NotifyTemplateConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkNotifyTemplatePresent(record);
        // 插入
        int effect = notifyTemplateDAO.insert(record);
        Long id = record.getId();
        // 删除缓存
        RedisUtils.delete(NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_LIST.format(record.getBizType()),
                NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_DETAIL.format(id));
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.ID, id);
        log.info("NotifyTemplateService-createNotifyTemplate id: {}, effect: {}", id, effect);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateNotifyTemplateById(NotifyTemplateUpdateRequest request) {
        Long id = Assert.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("NotifyTemplateService-updateNotifyTemplateById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        NotifyTemplateDO record = notifyTemplateDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        NotifyTemplateDO updateRecord = NotifyTemplateConvert.MAPPER.to(request);
        updateRecord.setBizType(record.getBizType());
        // 查询数据是否冲突
        this.checkNotifyTemplatePresent(updateRecord);
        // 更新
        int effect = notifyTemplateDAO.updateById(updateRecord);
        log.info("NotifyTemplateService-updateNotifyTemplateById effect: {}", effect);
        // 删除缓存
        RedisUtils.delete(NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_LIST.format(record.getBizType()),
                NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_DETAIL.format(id));
        return effect;
    }

    @Override
    public NotifyTemplateVO getNotifyTemplateById(Long id) {
        // 查询
        NotifyTemplateDO record = notifyTemplateDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return NotifyTemplateConvert.MAPPER.to(record);
    }

    @Override
    public List<NotifyTemplateVO> getNotifyTemplateListByCache(String bizType) {
        // 查询缓存
        String key = NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_LIST.format(bizType);
        List<NotifyTemplateCacheDTO> list = RedisMaps.valuesJson(key, NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_LIST);
        if (list.isEmpty()) {
            // 查询数据库
            list = notifyTemplateDAO.of()
                    .createWrapper()
                    .eq(NotifyTemplateDO::getBizType, bizType)
                    .then()
                    .list(NotifyTemplateConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, NotifyTemplateCacheDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(key, NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_LIST, s -> s.getId().toString(), list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(NotifyTemplateConvert.MAPPER::to)
                .sorted(Comparator.comparing(NotifyTemplateVO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public NotifyTemplateDetailCacheDTO getNotifyTemplateDetailByCache(Long id) {
        // 查询缓存
        String key = NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_DETAIL.format(id);
        NotifyTemplateDetailCacheDTO detail = RedisStrings.getJson(key, NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_DETAIL);
        if (detail == null) {
            // 查询数据库
            NotifyTemplateDO template = notifyTemplateDAO.selectById(id);
            if (template != null) {
                // 设置缓存
                detail = NotifyTemplateConvert.MAPPER.toDetailCache(template);
                RedisStrings.setJson(key, NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_DETAIL, detail);
            } else {
                // 设置空缓存
                detail = new NotifyTemplateDetailCacheDTO();
                RedisStrings.set(key, NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_DETAIL, Const.EMPTY_OBJECT);
            }
        }
        return detail;
    }

    @Override
    public DataGrid<NotifyTemplateVO> getNotifyTemplatePage(NotifyTemplateQueryRequest request) {
        // 条件
        LambdaQueryWrapper<NotifyTemplateDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return notifyTemplateDAO.of()
                .wrapper(wrapper)
                .page(request)
                .order(request, NotifyTemplateDO::getId)
                .dataGrid(NotifyTemplateConvert.MAPPER::to);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteNotifyTemplateById(Long id) {
        log.info("NotifyTemplateService-deleteNotifyTemplateById id: {}", id);
        // 查询数据
        NotifyTemplateDO record = notifyTemplateDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.NAME, record.getName());
        // 删除数据
        int effect = notifyTemplateDAO.deleteById(id);
        // 删除策略通知
        alarmPolicyNotifyApi.deleteByNotifyId(id);
        // 删除缓存
        RedisUtils.delete(NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_LIST.format(record.getBizType()),
                NotifyTemplateCacheKeyDefine.NOTIFY_TEMPLATE_DETAIL.format(id));
        log.info("NotifyTemplateService-deleteNotifyTemplateById effect: {}", effect);
        return effect;
    }

    @Override
    public LambdaQueryWrapper<NotifyTemplateDO> buildQueryWrapper(NotifyTemplateQueryRequest request) {
        return notifyTemplateDAO.wrapper()
                .eq(NotifyTemplateDO::getId, request.getId())
                .like(NotifyTemplateDO::getName, request.getName())
                .eq(NotifyTemplateDO::getBizType, request.getBizType())
                .eq(NotifyTemplateDO::getChannelType, request.getChannelType())
                .like(NotifyTemplateDO::getDescription, request.getDescription());
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkNotifyTemplatePresent(NotifyTemplateDO domain) {
        // 构造条件
        LambdaQueryWrapper<NotifyTemplateDO> wrapper = notifyTemplateDAO.wrapper()
                // 更新时忽略当前记录
                .ne(NotifyTemplateDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(NotifyTemplateDO::getName, domain.getName())
                .eq(NotifyTemplateDO::getBizType, domain.getBizType());
        // 检查是否存在
        boolean present = notifyTemplateDAO.of(wrapper).present();
        Assert.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

}
