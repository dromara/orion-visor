package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Booleans;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogs;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.ops.module.asset.convert.HostConvert;
import com.orion.ops.module.asset.dao.HostConfigDAO;
import com.orion.ops.module.asset.dao.HostDAO;
import com.orion.ops.module.asset.define.cache.HostCacheKeyDefine;
import com.orion.ops.module.asset.entity.domain.HostDO;
import com.orion.ops.module.asset.entity.dto.HostCacheDTO;
import com.orion.ops.module.asset.entity.request.host.HostCreateRequest;
import com.orion.ops.module.asset.entity.request.host.HostQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostVO;
import com.orion.ops.module.asset.service.HostConfigService;
import com.orion.ops.module.asset.service.HostService;
import com.orion.ops.module.infra.api.DataExtraApi;
import com.orion.ops.module.infra.api.DataGroupRelApi;
import com.orion.ops.module.infra.api.FavoriteApi;
import com.orion.ops.module.infra.api.TagRelApi;
import com.orion.ops.module.infra.entity.dto.data.DataGroupRelCreateDTO;
import com.orion.ops.module.infra.entity.dto.tag.TagDTO;
import com.orion.ops.module.infra.enums.DataExtraTypeEnum;
import com.orion.ops.module.infra.enums.DataGroupTypeEnum;
import com.orion.ops.module.infra.enums.FavoriteTypeEnum;
import com.orion.ops.module.infra.enums.TagTypeEnum;
import com.orion.spring.SpringHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
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
    private TagRelApi tagRelApi;

    @Resource
    private FavoriteApi favoriteApi;

    @Resource
    private DataGroupRelApi dataGroupRelApi;

    @Resource
    private DataExtraApi dataExtraApi;

    @Override
    public Long createHost(HostCreateRequest request) {
        log.info("HostService-createHost request: {}", JSON.toJSONString(request));
        // 转换
        HostDO record = HostConvert.MAPPER.to(request);
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
            List<DataGroupRelCreateDTO> groupRelList = groupIdList.stream()
                    .map(s -> DataGroupRelCreateDTO.builder()
                            .groupId(s)
                            .relId(id)
                            .build())
                    .collect(Collectors.toList());
            dataGroupRelApi.addGroupRel(groupRelList);
        }
        // 创建配置
        hostConfigService.initHostConfig(id);
        // 删除缓存
        RedisMaps.delete(HostCacheKeyDefine.HOST_INFO);
        return id;
    }

    @Override
    public Integer updateHostById(HostUpdateRequest request) {
        log.info("HostService-updateHostById request: {}", JSON.toJSONString(request));
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        HostDO record = hostDAO.selectById(id);
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
        dataGroupRelApi.updateGroupRel(DataGroupTypeEnum.HOST, request.getGroupIdList(), id);
        // 更新 tag
        tagRelApi.setTagRel(TagTypeEnum.HOST, id, request.getTags());
        // 删除缓存
        RedisMaps.delete(HostCacheKeyDefine.HOST_INFO);
        return effect;
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
        // 设置 tag 信息
        List<TagDTO> tags = tagFuture.get();
        // 设置分组信息
        vo.setTags(tags);
        Set<Long> groupIdList = groupIdFuture.get();
        vo.setGroupIdList(groupIdList);
        return vo;
    }

    @Override
    public List<HostVO> getHostListByCache() {
        // 查询缓存
        List<HostCacheDTO> list = RedisMaps.valuesJson(HostCacheKeyDefine.HOST_INFO);
        if (list.isEmpty()) {
            // 查询数据库
            list = hostDAO.of().list(HostConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, HostCacheDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(HostCacheKeyDefine.HOST_INFO, s -> s.getId().toString(), list);
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
        DataGrid<HostVO> hosts = hostDAO.of(wrapper)
                .page(request)
                .dataGrid(HostConvert.MAPPER::to);
        // 查询拓展信息
        this.setExtraInfo(request, hosts.getRows());
        return hosts;
    }

    @Override
    public Integer deleteHostById(Long id) {
        log.info("HostService-deleteHostById id: {}", id);
        // 查询
        HostDO record = hostDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.HOST_ABSENT);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.NAME, record.getName());
        // 删除
        int effect = hostDAO.deleteById(id);
        log.info("HostService-deleteHostById effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(HostCacheKeyDefine.HOST_INFO, id);
        // 删除主机引用
        SpringHolder.getBean(HostService.class)
                .deleteHostRelByIdAsync(id);
        return effect;
    }

    @Override
    @Async("asyncExecutor")
    public void deleteHostRelByIdAsync(Long id) {
        // 删除配置
        hostConfigDAO.deleteByHostId(id);
        // 删除分组
        dataGroupRelApi.deleteByRelId(DataGroupTypeEnum.HOST, id);
        // 删除 tag 引用
        tagRelApi.deleteRelId(TagTypeEnum.HOST, id);
        // 删除收藏引用
        favoriteApi.deleteByRelId(FavoriteTypeEnum.HOST, id);
        // 删除额外配置
        dataExtraApi.deleteByRelId(DataExtraTypeEnum.HOST, id);
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

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<HostDO> buildQueryWrapper(HostQueryRequest request) {
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
