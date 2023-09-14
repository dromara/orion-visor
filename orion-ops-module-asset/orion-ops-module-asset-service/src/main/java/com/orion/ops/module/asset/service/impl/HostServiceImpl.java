package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Booleans;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.convert.HostConfigConvert;
import com.orion.ops.module.asset.convert.HostConvert;
import com.orion.ops.module.asset.dao.HostConfigDAO;
import com.orion.ops.module.asset.dao.HostDAO;
import com.orion.ops.module.asset.entity.domain.HostConfigDO;
import com.orion.ops.module.asset.entity.domain.HostDO;
import com.orion.ops.module.asset.entity.request.host.HostCreateRequest;
import com.orion.ops.module.asset.entity.request.host.HostQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostConfigVO;
import com.orion.ops.module.asset.entity.vo.HostVO;
import com.orion.ops.module.asset.service.HostService;
import com.orion.ops.module.infra.api.FavoriteApi;
import com.orion.ops.module.infra.api.TagRelApi;
import com.orion.ops.module.infra.entity.dto.tag.TagDTO;
import com.orion.ops.module.infra.enums.FavoriteTypeEnum;
import com.orion.ops.module.infra.enums.TagTypeEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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

    private static final ThreadLocal<List<Long>> FAVORITE_HOLDER = new ThreadLocal<>();

    @Resource
    private HostDAO hostDAO;

    @Resource
    private HostConfigDAO hostConfigDAO;

    @Resource
    private TagRelApi tagRelApi;

    @Resource
    private FavoriteApi favoriteApi;

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
        List<Long> tags = request.getTags();
        if (!Lists.isEmpty(tags)) {
            tagRelApi.addTagRel(TagTypeEnum.HOST, id, tags);
        }
        return id;
    }

    @Override
    public Integer updateHostById(HostUpdateRequest request) {
        log.info("HostService-updateHostById request: {}", JSON.toJSONString(request));
        // 查询
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        HostDO record = hostDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        HostDO updateRecord = HostConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkHostNamePresent(updateRecord);
        this.checkHostCodePresent(updateRecord);
        // 更新
        int effect = hostDAO.updateById(updateRecord);
        log.info("HostService-updateHostById effect: {}", effect);
        // 更新 tag
        tagRelApi.setTagRel(TagTypeEnum.HOST, id, request.getTags());
        return effect;
    }

    @Override
    public HostVO getHostById(HostQueryRequest request) {
        // 查询
        HostDO record = hostDAO.selectById(request.getId());
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        HostVO vo = HostConvert.MAPPER.to(record);
        // 查询拓展信息
        this.setExtraInfo(request, Lists.singleton(vo));
        return vo;
    }

    @Override
    public List<HostVO> getHostList(HostQueryRequest request) {
        try {
            // 条件
            LambdaQueryWrapper<HostDO> wrapper = this.buildQueryWrapper(request);
            if (wrapper == null) {
                return Lists.empty();
            }
            // 查询
            List<HostVO> hosts = hostDAO.of(wrapper).list(HostConvert.MAPPER::to);
            // 查询拓展信息
            this.setExtraInfo(request, hosts);
            return hosts;
        } finally {
            FAVORITE_HOLDER.remove();
        }
    }

    @Override
    public DataGrid<HostVO> getHostPage(HostQueryRequest request) {
        try {
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
        } finally {
            FAVORITE_HOLDER.remove();
        }
    }

    @Override
    public Integer deleteHostById(Long id) {
        log.info("HostService-deleteHostById id: {}", id);
        int effect = hostDAO.deleteById(id);
        log.info("HostService-deleteHostById effect: {}", effect);
        // 删除配置
        hostConfigDAO.deleteByHostId(id);
        // 删除 tag 引用
        tagRelApi.deleteRelId(TagTypeEnum.HOST, id);
        // 删除收藏引用
        favoriteApi.deleteByRelId(FavoriteTypeEnum.HOST, id);
        return effect;
    }

    /**
     * 检测 name 是否存在
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
     * 检测 code 是否存在
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
    @SneakyThrows
    private LambdaQueryWrapper<HostDO> buildQueryWrapper(HostQueryRequest request) {
        boolean setIdList = Booleans.isTrue(request.getFavorite()) || Lists.isNotEmpty(request.getTags());
        List<List<Long>> idListGrouping = new ArrayList<>();
        // 查询收藏
        Future<List<Long>> favoriteFuture = null;
        if (Booleans.isTrue(request.getFavorite())) {
            favoriteFuture = favoriteApi.getFavoriteRelIdList(FavoriteTypeEnum.HOST, SecurityUtils.getLoginUserId());
        }
        // tag 条件
        if (Lists.isNotEmpty(request.getTags())) {
            List<Long> tagRelIdList = tagRelApi.getRelIdByTagId(request.getTags());
            if (tagRelIdList.isEmpty()) {
                return null;
            }
            idListGrouping.add(tagRelIdList);
        }
        // 获取收藏结果
        if (favoriteFuture != null) {
            List<Long> favorites = favoriteFuture.get();
            // 无收藏
            if (Lists.isEmpty(favorites)) {
                return null;
            }
            idListGrouping.add(favorites);
        }
        // flat
        List<Long> idList = null;
        if (setIdList && !idListGrouping.isEmpty()) {
            idList = idListGrouping.get(0);
            // 交集
            for (int i = 1; i < idListGrouping.size(); i++) {
                idList.retainAll(idListGrouping.get(i));
            }
            if (idList.isEmpty()) {
                return null;
            }
        }
        // 基础条件
        LambdaQueryWrapper<HostDO> wrapper = hostDAO.wrapper()
                .eq(HostDO::getId, request.getId())
                .like(HostDO::getName, request.getName())
                .like(HostDO::getCode, request.getCode())
                .like(HostDO::getAddress, request.getAddress());
        if (setIdList) {
            wrapper.in(HostDO::getId, idList);
        }
        return wrapper;
    }

    /**
     * 设置额外信息
     *
     * @param request request
     * @param hosts   hosts
     */
    @SneakyThrows
    private void setExtraInfo(HostQueryRequest request, List<HostVO> hosts) {
        if (hosts.isEmpty()) {
            return;
        }
        List<Long> idList = hosts.stream().map(HostVO::getId).collect(Collectors.toList());
        // 查询额外信息
        Future<List<List<TagDTO>>> tagsFuture = null;
        Future<List<Long>> favoriteFuture = null;
        if (Booleans.isTrue(request.getExtra())) {
            // tag
            tagsFuture = tagRelApi.getRelTags(TagTypeEnum.HOST, idList);
            // 从缓存中读取 收藏
            List<Long> favorites = FAVORITE_HOLDER.get();
            if (favorites != null) {
                favoriteFuture = CompletableFuture.completedFuture(favorites);
            } else {
                favoriteFuture = favoriteApi.getFavoriteRelIdList(FavoriteTypeEnum.HOST, SecurityUtils.getLoginUserId());
            }
        }
        // 查询配置
        if (Booleans.isTrue(request.getConfig())) {
            // 配置分组
            Map<Long, List<HostConfigDO>> hostConfigGrouping = hostConfigDAO.getHostConfigByHostIdList(idList)
                    .stream()
                    .collect(Collectors.groupingBy(HostConfigDO::getHostId));
            // 设置配置
            hosts.forEach(s -> {
                List<HostConfigDO> configs = hostConfigGrouping.get(s.getId());
                if (Lists.isEmpty(configs)) {
                    return;
                }
                Map<String, HostConfigVO> configMap = configs.stream()
                        .collect(Collectors.toMap(
                                HostConfigDO::getType,
                                HostConfigConvert.MAPPER::to,
                                (v1, v2) -> v2
                        ));
                s.setConfigs(configMap);
            });
        }
        // 设置 tag 信息
        List<List<TagDTO>> tagList = null;
        if (tagsFuture != null && (tagList = tagsFuture.get()) != null) {
            for (int i = 0; i < hosts.size(); i++) {
                hosts.get(i).setTags(tagList.get(i));
            }
        }
        // 设置收藏信息
        List<Long> favoriteIdList = null;
        if (favoriteFuture != null && (favoriteIdList = favoriteFuture.get()) != null) {
            for (HostVO host : hosts) {
                host.setFavorite(favoriteIdList.contains(host.getId()));
            }
        }
    }

}
