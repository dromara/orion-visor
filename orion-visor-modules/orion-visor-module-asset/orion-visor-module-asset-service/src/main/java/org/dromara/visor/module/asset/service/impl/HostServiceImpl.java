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
package org.dromara.visor.module.asset.service.impl;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.utils.Booleans;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.spring.SpringHolder;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.enums.EnableStatus;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.redis.core.utils.RedisMaps;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.module.asset.convert.HostConvert;
import org.dromara.visor.module.asset.dao.HostConfigDAO;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.define.cache.AssetStatisticsCacheKeyDefine;
import org.dromara.visor.module.asset.define.cache.HostCacheKeyDefine;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.dto.HostCacheDTO;
import org.dromara.visor.module.asset.entity.request.host.*;
import org.dromara.visor.module.asset.entity.vo.HostVO;
import org.dromara.visor.module.asset.enums.HostStatusEnum;
import org.dromara.visor.module.asset.handler.host.extra.HostExtraItemEnum;
import org.dromara.visor.module.asset.handler.host.extra.model.HostSpecExtraModel;
import org.dromara.visor.module.asset.service.HostConfigService;
import org.dromara.visor.module.asset.service.HostExtraService;
import org.dromara.visor.module.asset.service.HostService;
import org.dromara.visor.module.exec.api.ExecJobApi;
import org.dromara.visor.module.exec.api.ExecTemplateApi;
import org.dromara.visor.module.infra.api.DataExtraApi;
import org.dromara.visor.module.infra.api.DataGroupRelApi;
import org.dromara.visor.module.infra.api.FavoriteApi;
import org.dromara.visor.module.infra.api.TagRelApi;
import org.dromara.visor.module.infra.entity.dto.tag.TagDTO;
import org.dromara.visor.module.infra.enums.DataExtraTypeEnum;
import org.dromara.visor.module.infra.enums.DataGroupTypeEnum;
import org.dromara.visor.module.infra.enums.FavoriteTypeEnum;
import org.dromara.visor.module.infra.enums.TagTypeEnum;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 主机 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Slf4j
@Service
public class HostServiceImpl implements HostService {

    @Resource
    private HostDAO hostDAO;

    @Resource
    private HostConfigDAO hostConfigDAO;

    @Resource
    private HostConfigService hostConfigService;

    @Resource
    private HostExtraService hostExtraService;

    @Resource
    private ExecJobApi execJobApi;

    @Resource
    private ExecTemplateApi execTemplateApi;

    @Resource
    private TagRelApi tagRelApi;

    @Resource
    private FavoriteApi favoriteApi;

    @Resource
    private DataGroupRelApi dataGroupRelApi;

    @Resource
    private DataExtraApi dataExtraApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createHost(HostCreateRequest request) {
        log.info("HostService-createHost request: {}", JSON.toJSONString(request));
        // 转换
        HostDO record = HostConvert.MAPPER.to(request);
        record.setStatus(HostStatusEnum.ENABLED.name());
        // 查询数据是否冲突
        this.checkHostNamePresent(record);
        this.checkHostCodePresent(record);
        // 插入主机
        int effect = hostDAO.insert(record);
        log.info("HostService-createHost effect: {}", effect);
        Long id = record.getId();
        // 插入 tag
        tagRelApi.addTagRel(TagTypeEnum.HOST, id, request.getTags());
        // 引用分组
        List<Long> groupIdList = request.getGroupIdList();
        if (!Lists.isEmpty(groupIdList)) {
            dataGroupRelApi.updateRelGroup(DataGroupTypeEnum.HOST, request.getGroupIdList(), id);
        }
        // 删除缓存
        this.clearCache();
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyHost(Long originId, HostUpdateRequest request) {
        log.info("HostService-copyHost originId: {}, request: {}", originId, JSON.toJSONString(request));
        // 查询原始主机
        HostDO originHost = hostDAO.selectById(originId);
        Valid.notNull(originHost, ErrorMessage.HOST_ABSENT);
        // 创建主机
        Long newId = SpringHolder.getBean(HostService.class)
                .createHost(HostConvert.MAPPER.toCreate(request));
        // 复制主机额外信息
        hostExtraService.copyHostExtra(originId, newId);
        // 复制主机配置信息
        hostConfigService.copyHostConfig(originId, newId, request.getTypes());
        return newId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateHostById(HostUpdateRequest request) {
        log.info("HostService-updateHostById request: {}", JSON.toJSONString(request));
        List<String> types = request.getTypes();
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        HostDO record = hostDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.HOST_ABSENT);
        // 转换
        HostDO updateRecord = HostConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkHostNamePresent(updateRecord);
        this.checkHostCodePresent(updateRecord);
        // 更新主机
        int effect = hostDAO.updateById(updateRecord);
        log.info("HostService-updateHostById effect: {}", effect);
        // 引用分组
        dataGroupRelApi.updateRelGroup(DataGroupTypeEnum.HOST, request.getGroupIdList(), id);
        // 更新 tag
        tagRelApi.setTagRel(TagTypeEnum.HOST, id, request.getTags());
        // 修改 config 状态
        hostConfigDAO.updateConfigStatus(id, types, EnableStatus.ENABLED.name());
        hostConfigDAO.updateConfigInvertStatus(id, types, EnableStatus.DISABLED.name());
        // 删除缓存
        this.clearCache();
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateHostStatus(HostUpdateStatusRequest request) {
        log.info("HostService-updateHostStatus request: {}", JSON.toJSONString(request));
        Long id = request.getId();
        String status = Valid.valid(HostStatusEnum::of, request.getStatus()).name();
        // 查询主机
        HostDO record = hostDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.HOST_ABSENT);
        // 更新
        HostDO update = HostDO.builder()
                .id(id)
                .status(status)
                .build();
        int effect = hostDAO.updateById(update);
        log.info("HostService-updateHostStatus effect: {}", effect);
        // 更新主机配置
        hostConfigDAO.updateConfigStatus(id, null, status);
        // 删除缓存
        this.clearCache();
        return effect;
    }

    @Override
    public Integer updateHostSpec(HostExtraUpdateRequest request) {
        log.info("HostService-updateHostSpec request: {}", JSON.toJSONString(request));
        // 查询主机
        HostDO record = hostDAO.selectById(request.getHostId());
        Valid.notNull(record, ErrorMessage.HOST_ABSENT);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.NAME, record.getName());
        // 更新
        request.setItem(HostExtraItemEnum.SPEC.name());
        return hostExtraService.updateHostExtra(request);
    }

    @Override
    @SneakyThrows
    public HostVO getHostById(Long id) {
        // 查询 tag 信息
        Future<List<TagDTO>> tagFuture = tagRelApi.getRelTagsAsync(TagTypeEnum.HOST, id);
        // 查询分组信息
        Future<Set<Long>> groupIdFuture = dataGroupRelApi.getGroupIdByRelIdAsync(DataGroupTypeEnum.HOST, id);
        // 查询主机
        HostDO record = hostDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.HOST_ABSENT);
        // 转换
        HostVO vo = HostConvert.MAPPER.to(record);
        vo.setTags(tagFuture.get());
        vo.setGroupIdList(groupIdFuture.get());
        return vo;
    }

    @Override
    public List<HostVO> getHostList(String type) {
        // 查询缓存
        String cacheKey;
        if (Strings.isBlank(type)) {
            cacheKey = HostCacheKeyDefine.HOST_INFO.format(Const.ALL);
        } else {
            cacheKey = HostCacheKeyDefine.HOST_INFO.format(type);
        }
        List<HostCacheDTO> list = RedisMaps.valuesJson(cacheKey, HostCacheKeyDefine.HOST_INFO);
        if (list.isEmpty()) {
            // 查询数据库
            list = hostDAO.of()
                    .createWrapper(true)
                    .like(HostDO::getTypes, type)
                    .then()
                    .list(HostConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, HostCacheDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(cacheKey, HostCacheKeyDefine.HOST_INFO, s -> s.getId().toString(), list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(HostConvert.MAPPER::to)
                .sorted(Comparator.comparing(HostVO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public DataGrid<HostVO> getHostPage(HostQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostDO> wrapper = this.buildQueryWrapper(request);
        if (wrapper == null) {
            return DataGrid.of(Lists.empty());
        }
        // 查询
        DataGrid<HostVO> hosts = hostDAO.of()
                .wrapper(wrapper)
                .page(request)
                .order(request, HostDO::getId)
                .dataGrid(HostConvert.MAPPER::to);
        // 查询拓展信息
        this.setExtraInfo(request, hosts.getRows());
        return hosts;
    }

    @Override
    public Long getHostCount(HostQueryRequest request) {
        // 条件
        LambdaQueryWrapper<HostDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return hostDAO.of()
                .wrapper(wrapper)
                .countMax(request.getLimit());
    }

    @Override
    public Integer deleteHostById(Long id) {
        return this.deleteHostByIdList(Lists.singleton(id));
    }

    @Override
    public Integer deleteHostByIdList(List<Long> idList) {
        log.info("HostService-deleteHostByIdList idList: {}", idList);
        // 删除
        int effect = hostDAO.deleteBatchIds(idList);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.COUNT, idList.size());
        log.info("HostService-deleteHostByIdList effect: {}", effect);
        // 删除缓存
        this.clearCache();
        // 删除主机引用
        SpringHolder.getBean(HostService.class)
                .deleteHostRelByIdListAsync(idList);
        return effect;
    }

    @Override
    @Async("asyncExecutor")
    public void deleteHostRelByIdListAsync(List<Long> idList) {
        log.info("HostService-deleteHostRelByIdListAsync idList: {}", idList);
        // 删除主机配置
        hostConfigDAO.deleteByHostIdList(idList);
        // 删除计划任务主机
        execJobApi.deleteByHostIdList(idList);
        // 删除执行模板主机
        execTemplateApi.deleteByHostIdList(idList);
        // 删除分组
        dataGroupRelApi.deleteByRelIdList(DataGroupTypeEnum.HOST, idList);
        // 删除 tag 引用
        tagRelApi.deleteRelIdList(TagTypeEnum.HOST, idList);
        // 删除收藏引用
        favoriteApi.deleteByRelIdList(FavoriteTypeEnum.HOST, idList);
        // 删除额外配置
        dataExtraApi.deleteByRelIdList(DataExtraTypeEnum.HOST, idList);
    }

    @Override
    public void clearCache() {
        RedisMaps.scanKeysDelete(HostCacheKeyDefine.HOST_INFO.format("*"));
        RedisStrings.delete(AssetStatisticsCacheKeyDefine.HOST_TYPE_COUNT.getKey());
    }

    /**
     * 检查 name 是否存在
     *
     * @param domain domain
     */
    private void checkHostNamePresent(HostDO domain) {
        // 构造条件
        LambdaQueryWrapper<HostDO> wrapper = hostDAO.wrapper()
                // 更新时忽略当前记录
                .ne(HostDO::getId, domain.getId())
                .eq(HostDO::getName, domain.getName());
        // 检查是否存在
        boolean present = hostDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.NAME_PRESENT);
    }

    /**
     * 检查 code 是否存在
     *
     * @param domain domain
     */
    private void checkHostCodePresent(HostDO domain) {
        // 构造条件
        LambdaQueryWrapper<HostDO> wrapper = hostDAO.wrapper()
                // 更新时忽略当前记录
                .ne(HostDO::getId, domain.getId())
                .eq(HostDO::getCode, domain.getCode());
        // 检查是否存在
        boolean present = hostDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.CODE_PRESENT);
    }

    @Override
    public LambdaQueryWrapper<HostDO> buildQueryWrapper(HostQueryRequest request) {
        String searchValue = request.getSearchValue();
        LambdaQueryWrapper<HostDO> wrapper = hostDAO.wrapper();
        // tag 条件
        if (Lists.isNotEmpty(request.getTags())) {
            List<Long> tagRelIdList = tagRelApi.getRelIdByTagId(request.getTags());
            if (tagRelIdList.isEmpty()) {
                return null;
            }
            wrapper.in(HostDO::getId, tagRelIdList);
        }
        // 基础条件
        wrapper.eq(HostDO::getId, request.getId())
                .eq(HostDO::getOsType, request.getOsType())
                .eq(HostDO::getArchType, request.getArchType())
                .eq(HostDO::getStatus, request.getStatus())
                .like(HostDO::getName, request.getName())
                .like(HostDO::getCode, request.getCode())
                .like(HostDO::getAddress, request.getAddress())
                .like(HostDO::getTypes, request.getType())
                .like(HostDO::getDescription, request.getDescription())
                .and(Strings.isNotEmpty(searchValue), c -> c
                        .eq(HostDO::getId, searchValue).or()
                        .like(HostDO::getName, searchValue).or()
                        .like(HostDO::getCode, searchValue).or()
                        .like(HostDO::getAddress, searchValue)
                );
        return wrapper;
    }

    /**
     * 设置额外信息
     *
     * @param request request
     * @param hosts   hosts
     */
    private void setExtraInfo(HostQueryRequest request, List<HostVO> hosts) {
        if (hosts.isEmpty()) {
            return;
        }
        List<Long> idList = hosts.stream().map(HostVO::getId).collect(Collectors.toList());
        // 查询 tag 信息
        if (Booleans.isTrue(request.getQueryTag())) {
            // 设置 tag 信息
            List<List<TagDTO>> tagList = tagRelApi.getRelTags(TagTypeEnum.HOST, idList);
            for (int i = 0; i < hosts.size(); i++) {
                hosts.get(i).setTags(tagList.get(i));
            }
        }
        // 查询分组信息
        if (Booleans.isTrue(request.getQueryGroup())) {
            Map<Long, Set<Long>> groupRelList = dataGroupRelApi.getGroupRelByRelIdList(DataGroupTypeEnum.HOST, idList);
            for (HostVO host : hosts) {
                host.setGroupIdList(groupRelList.get(host.getId()));
            }
        }
        // 查询规格信息
        if (Booleans.isTrue(request.getQuerySpec())) {
            Map<Long, HostSpecExtraModel> specMap = hostExtraService.getHostSpecMap(idList);
            for (HostVO host : hosts) {
                host.setSpec(specMap.get(host.getId()));
            }
        }
    }

}
