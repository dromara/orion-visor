/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.common.constant.ErrorMessage;
import org.dromara.visor.framework.common.handler.data.model.GenericsDataModel;
import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.framework.redis.core.utils.RedisMaps;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.module.asset.convert.HostConvert;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.define.cache.HostCacheKeyDefine;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.dto.HostCacheDTO;
import org.dromara.visor.module.asset.entity.request.host.*;
import org.dromara.visor.module.asset.entity.vo.HostConfigVO;
import org.dromara.visor.module.asset.entity.vo.HostVO;
import org.dromara.visor.module.asset.enums.HostStatusEnum;
import org.dromara.visor.module.asset.enums.HostTypeEnum;
import org.dromara.visor.module.asset.service.ExecJobHostService;
import org.dromara.visor.module.asset.service.ExecTemplateHostService;
import org.dromara.visor.module.asset.service.HostService;
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

    private static final ThreadLocal<Long> CURRENT_UPDATE_CONFIG_ID = new ThreadLocal<>();

    @Resource
    private HostDAO hostDAO;

    @Resource
    private ExecJobHostService execJobHostService;

    @Resource
    private ExecTemplateHostService execTemplateHostService;

    @Resource
    private TagRelApi tagRelApi;

    @Resource
    private FavoriteApi favoriteApi;

    @Resource
    private DataGroupRelApi dataGroupRelApi;

    @Resource
    private DataExtraApi dataExtraApi;

    @Override
    public Long createHost(HostCreateRequest request) {
        HostTypeEnum type = Valid.valid(HostTypeEnum::of, request.getType());
        log.info("HostService-createHost request: {}", JSON.toJSONString(request));
        // 转换
        HostDO record = HostConvert.MAPPER.to(request);
        record.setStatus(HostStatusEnum.ENABLED.name());
        // 查询数据是否冲突
        this.checkHostNamePresent(record);
        this.checkHostCodePresent(record);
        // 设置主机配置
        record.setConfig(type.getStrategy().getDefault().serial());
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
    public Integer updateHostById(HostUpdateRequest request) {
        log.info("HostService-updateHostById request: {}", JSON.toJSONString(request));
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        HostDO record = hostDAO.selectBaseById(id);
        Valid.notNull(record, ErrorMessage.HOST_ABSENT);
        // 转换
        HostDO updateRecord = HostConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkHostNamePresent(updateRecord);
        this.checkHostCodePresent(updateRecord);
        // 更新
        int effect = hostDAO.updateById(updateRecord);
        log.info("HostService-updateHostById effect: {}", effect);
        // 引用分组
        dataGroupRelApi.updateRelGroup(DataGroupTypeEnum.HOST, request.getGroupIdList(), id);
        // 更新 tag
        tagRelApi.setTagRel(TagTypeEnum.HOST, id, request.getTags());
        // 删除缓存
        this.clearCache();
        return effect;
    }

    @Override
    public Integer updateHostStatus(HostUpdateStatusRequest request) {
        log.info("HostService-updateHostStatus request: {}", JSON.toJSONString(request));
        Long id = request.getId();
        HostStatusEnum status = Valid.valid(HostStatusEnum::of, request.getStatus());
        // 查询主机
        HostDO record = hostDAO.selectBaseById(id);
        Valid.notNull(record, ErrorMessage.HOST_ABSENT);
        // 更新
        HostDO update = HostDO.builder()
                .id(id)
                .status(status.name())
                .build();
        int effect = hostDAO.updateById(update);
        log.info("HostService-updateHostStatus effect: {}", effect);
        // 删除缓存
        this.clearCache();
        return effect;
    }

    @Override
    public Integer updateHostConfig(HostUpdateConfigRequest request) {
        log.info("HostService-updateHostConfig request: {}", JSON.toJSONString(request));
        Long id = request.getId();
        try {
            CURRENT_UPDATE_CONFIG_ID.set(id);
            // 查询主机信息
            HostDO host = hostDAO.selectById(id);
            Valid.notNull(host, ErrorMessage.HOST_ABSENT);
            HostTypeEnum type = Valid.valid(HostTypeEnum::of, host.getType());
            GenericsDataModel beforeConfig = type.parse(host.getConfig());
            GenericsDataModel newConfig = type.parse(request.getConfig());
            // 添加日志参数
            OperatorLogs.add(OperatorLogs.ID, id);
            OperatorLogs.add(OperatorLogs.NAME, host.getName());
            // 更新前校验
            type.getStrategy().doValid(beforeConfig, newConfig);
            // 修改配置
            HostDO updateHost = HostDO.builder()
                    .id(id)
                    .config(newConfig.serial())
                    .build();
            int effect = hostDAO.updateById(updateHost);
            log.info("HostService-updateHostConfig effect: {}", effect);
            return effect;
        } finally {
            CURRENT_UPDATE_CONFIG_ID.remove();
        }
    }

    @Override
    @SneakyThrows
    public HostVO getHostById(Long id) {
        // 查询 tag 信息
        Future<List<TagDTO>> tagFuture = tagRelApi.getRelTagsAsync(TagTypeEnum.HOST, id);
        // 查询分组信息
        Future<Set<Long>> groupIdFuture = dataGroupRelApi.getGroupIdByRelIdAsync(DataGroupTypeEnum.HOST, id);
        // 查询主机
        HostDO record = hostDAO.selectBaseById(id);
        Valid.notNull(record, ErrorMessage.HOST_ABSENT);
        // 转换
        HostVO vo = HostConvert.MAPPER.to(record);
        vo.setTags(tagFuture.get());
        vo.setGroupIdList(groupIdFuture.get());
        return vo;
    }

    @Override
    public HostConfigVO getHostConfig(Long id) {
        // 查询主机
        HostDO host = hostDAO.selectById(id);
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 获取主机类型
        String type = host.getType();
        HostTypeEnum hostType = HostTypeEnum.of(type);
        // 获取主机配置
        Map<String, Object> config = hostType.toView(host.getConfig()).toMap();
        // 返回
        return HostConfigVO.builder()
                .id(id)
                .type(type)
                .config(config)
                .build();
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
                    .select(HostDAO.BASE_COLUMN)
                    .eq(HostDO::getType, type)
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
        // 数量条件
        LambdaQueryWrapper<HostDO> countWrapper = wrapper.clone();
        // 基础条件
        wrapper.select(HostDAO.BASE_COLUMN)
                .orderByAsc(HostDO::getId);
        // 查询
        DataGrid<HostVO> hosts = hostDAO.of(wrapper)
                .page(request)
                .dataGrid(countWrapper, HostConvert.MAPPER::to);
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
        // 删除计划任务主机
        execJobHostService.deleteByHostIdList(idList);
        // 删除执行模板主机
        execTemplateHostService.deleteByHostIdList(idList);
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
    public Long getCurrentUpdateConfigHostId() {
        return CURRENT_UPDATE_CONFIG_ID.get();
    }

    @Override
    public void clearCache() {
        RedisMaps.scanKeysDelete(HostCacheKeyDefine.HOST_INFO.format("*"));
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
                .like(HostDO::getName, request.getName())
                .like(HostDO::getCode, request.getCode())
                .like(HostDO::getAddress, request.getAddress())
                .eq(HostDO::getStatus, request.getStatus())
                .eq(HostDO::getType, request.getType())
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
    }

}
