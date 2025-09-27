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
package org.dromara.visor.module.terminal.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.framework.redis.core.utils.RedisMaps;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.terminal.convert.PathBookmarkConvert;
import org.dromara.visor.module.terminal.dao.PathBookmarkDAO;
import org.dromara.visor.module.terminal.define.cache.PathBookmarkCacheKeyDefine;
import org.dromara.visor.module.terminal.entity.domain.PathBookmarkDO;
import org.dromara.visor.module.terminal.entity.dto.PathBookmarkCacheDTO;
import org.dromara.visor.module.terminal.entity.request.path.PathBookmarkCreateRequest;
import org.dromara.visor.module.terminal.entity.request.path.PathBookmarkUpdateRequest;
import org.dromara.visor.module.terminal.entity.vo.PathBookmarkGroupVO;
import org.dromara.visor.module.terminal.entity.vo.PathBookmarkVO;
import org.dromara.visor.module.terminal.entity.vo.PathBookmarkWrapperVO;
import org.dromara.visor.module.terminal.service.PathBookmarkGroupService;
import org.dromara.visor.module.terminal.service.PathBookmarkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 路径标签 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-23 23:15
 */
@Slf4j
@Service
public class PathBookmarkServiceImpl implements PathBookmarkService {

    @Resource
    private PathBookmarkDAO pathBookmarkDAO;

    @Resource
    private PathBookmarkGroupService pathBookmarkGroupService;

    @Override
    public Long createPathBookmark(PathBookmarkCreateRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        log.info("PathBookmarkService-createPathBookmark request: {}", JSON.toJSONString(request));
        // 转换
        PathBookmarkDO record = PathBookmarkConvert.MAPPER.to(request);
        record.setUserId(userId);
        // 查询数据是否冲突
        this.checkPathBookmarkPresent(record);
        // 插入
        int effect = pathBookmarkDAO.insert(record);
        Long id = record.getId();
        log.info("PathBookmarkService-createPathBookmark id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisMaps.delete(PathBookmarkCacheKeyDefine.PATH_BOOKMARK.format(userId));
        return id;
    }

    @Override
    public Integer updatePathBookmarkById(PathBookmarkUpdateRequest request) {
        Long id = Assert.notNull(request.getId(), ErrorMessage.ID_MISSING);
        Long userId = SecurityUtils.getLoginUserId();
        log.info("PathBookmarkService-updatePathBookmarkById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        PathBookmarkDO record = pathBookmarkDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 查询数据是否冲突
        PathBookmarkDO updateRecord = PathBookmarkConvert.MAPPER.to(request);
        this.checkPathBookmarkPresent(updateRecord);
        // 更新
        LambdaUpdateWrapper<PathBookmarkDO> update = Wrappers.<PathBookmarkDO>lambdaUpdate()
                .set(PathBookmarkDO::getGroupId, request.getGroupId())
                .set(PathBookmarkDO::getName, request.getName())
                .set(PathBookmarkDO::getPath, request.getPath())
                .eq(PathBookmarkDO::getId, id)
                .eq(PathBookmarkDO::getUserId, userId);
        int effect = pathBookmarkDAO.update(null, update);
        log.info("PathBookmarkService-updatePathBookmarkById effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(PathBookmarkCacheKeyDefine.PATH_BOOKMARK.format(userId));
        return effect;
    }

    @Override
    public PathBookmarkWrapperVO getPathBookmark() {
        // 查询分组
        List<PathBookmarkGroupVO> groups = pathBookmarkGroupService.getPathBookmarkGroupList();
        // 查询命令片段
        List<PathBookmarkVO> items = this.getPathBookmarkList();
        // 设置组内数据
        Map<Long, PathBookmarkGroupVO> groupMap = groups.stream()
                .collect(Collectors.toMap(PathBookmarkGroupVO::getId, Function.identity()));
        groupMap.forEach((groupId, group) -> {
            List<PathBookmarkVO> groupedItems = items.stream()
                    .filter(s -> groupId.equals(s.getGroupId()))
                    .collect(Collectors.toList());
            group.setItems(groupedItems);
        });
        // 未分组数据
        List<PathBookmarkVO> ungroupedItems = items.stream()
                .filter(s -> s.getGroupId() == null)
                .collect(Collectors.toList());
        return PathBookmarkWrapperVO.builder()
                .groups(groups)
                .ungroupedItems(ungroupedItems)
                .build();
    }

    @Override
    public List<PathBookmarkVO> getPathBookmarkList() {
        Long userId = SecurityUtils.getLoginUserId();
        String cacheKey = PathBookmarkCacheKeyDefine.PATH_BOOKMARK.format(userId);
        // 查询缓存
        List<PathBookmarkCacheDTO> list = RedisMaps.valuesJson(cacheKey, PathBookmarkCacheKeyDefine.PATH_BOOKMARK);
        if (list.isEmpty()) {
            // 查询数据库
            list = pathBookmarkDAO.of()
                    .createWrapper()
                    .eq(PathBookmarkDO::getUserId, userId)
                    .then()
                    .list(PathBookmarkConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, PathBookmarkCacheDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(cacheKey, PathBookmarkCacheKeyDefine.PATH_BOOKMARK, s -> s.getId().toString(), list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(PathBookmarkConvert.MAPPER::to)
                .sorted(Comparator.comparing(PathBookmarkVO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Integer setGroupNull(Long userId, Long groupId) {
        int effect = pathBookmarkDAO.setGroupIdWithNull(groupId);
        // 删除缓存
        RedisMaps.delete(PathBookmarkCacheKeyDefine.PATH_BOOKMARK.format(userId));
        return effect;
    }

    @Override
    public Integer deletePathBookmarkById(Long id) {
        Long userId = SecurityUtils.getLoginUserId();
        log.info("PathBookmarkService-deletePathBookmarkById id: {}", id);
        // 检查数据是否存在
        PathBookmarkDO record = pathBookmarkDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = pathBookmarkDAO.deleteById(id);
        log.info("PathBookmarkService-deletePathBookmarkById id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisMaps.delete(PathBookmarkCacheKeyDefine.PATH_BOOKMARK.format(userId), id);
        return effect;
    }

    @Override
    public Integer deleteByGroupId(Long userId, Long groupId) {
        int effect = pathBookmarkDAO.deleteByGroupId(groupId);
        // 删除缓存
        RedisMaps.delete(PathBookmarkCacheKeyDefine.PATH_BOOKMARK.format(userId));
        return effect;
    }

    @Override
    public Integer deleteByUserIdList(List<Long> userIdList) {
        return pathBookmarkDAO.deleteByUserIdList(userIdList);
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkPathBookmarkPresent(PathBookmarkDO domain) {
        // 构造条件
        LambdaQueryWrapper<PathBookmarkDO> wrapper = pathBookmarkDAO.wrapper()
                // 更新时忽略当前记录
                .ne(PathBookmarkDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(PathBookmarkDO::getUserId, domain.getUserId())
                .eq(PathBookmarkDO::getGroupId, domain.getGroupId())
                .eq(PathBookmarkDO::getName, domain.getName());
        // 检查是否存在
        boolean present = pathBookmarkDAO.of(wrapper).present();
        Assert.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

}
