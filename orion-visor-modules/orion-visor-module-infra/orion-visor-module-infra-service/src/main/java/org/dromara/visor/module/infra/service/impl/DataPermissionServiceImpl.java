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

import cn.orionsec.kit.lang.utils.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.framework.redis.core.utils.RedisLists;
import org.dromara.visor.framework.redis.core.utils.RedisUtils;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.module.infra.dao.DataPermissionDAO;
import org.dromara.visor.module.infra.dao.SystemUserRoleDAO;
import org.dromara.visor.module.infra.define.cache.DataPermissionCacheKeyDefine;
import org.dromara.visor.module.infra.entity.domain.DataPermissionDO;
import org.dromara.visor.module.infra.entity.request.data.DataPermissionUpdateRequest;
import org.dromara.visor.module.infra.enums.DataPermissionTypeEnum;
import org.dromara.visor.module.infra.service.DataPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据权限 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
@Slf4j
@Service
public class DataPermissionServiceImpl implements DataPermissionService {

    @Resource
    private DataPermissionDAO dataPermissionDAO;

    @Resource
    private SystemUserRoleDAO systemUserRoleDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDataPermission(DataPermissionUpdateRequest request) {
        Long userId = request.getUserId();
        Long roleId = request.getRoleId();
        String type = request.getType();
        // 查询
        LambdaQueryWrapper<DataPermissionDO> wrapper = dataPermissionDAO.wrapper()
                .eq(DataPermissionDO::getUserId, userId)
                .eq(DataPermissionDO::getRoleId, roleId)
                .eq(DataPermissionDO::getType, type);
        List<Long> beforeRelIdList = dataPermissionDAO.selectList(wrapper)
                .stream()
                .map(DataPermissionDO::getRelId)
                .distinct()
                .collect(Collectors.toList());
        // 新增
        List<DataPermissionDO> records = request.getRelIdList()
                .stream()
                .distinct()
                .filter(s -> !beforeRelIdList.contains(s))
                .map(s -> DataPermissionDO.builder()
                        .type(type)
                        .userId(userId)
                        .roleId(roleId)
                        .relId(s)
                        .build())
                .collect(Collectors.toList());
        dataPermissionDAO.insertBatch(records);
        // 删除缓存
        this.deleteCache(type, userId, roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDataPermission(DataPermissionUpdateRequest request) {
        Long userId = request.getUserId();
        Long roleId = request.getRoleId();
        String type = request.getType();
        // 删除
        LambdaQueryWrapper<DataPermissionDO> wrapper = dataPermissionDAO.wrapper()
                .eq(DataPermissionDO::getUserId, userId)
                .eq(DataPermissionDO::getRoleId, roleId)
                .eq(DataPermissionDO::getType, type);
        dataPermissionDAO.delete(wrapper);
        // 删除缓存
        this.deleteCache(type, userId, roleId);
        if (Lists.isEmpty(request.getRelIdList())) {
            return;
        }
        // 新增
        List<DataPermissionDO> records = request.getRelIdList()
                .stream()
                .distinct()
                .map(s -> DataPermissionDO.builder()
                        .type(type)
                        .userId(userId)
                        .roleId(roleId)
                        .relId(s)
                        .build())
                .collect(Collectors.toList());
        dataPermissionDAO.insertBatch(records);
    }

    @Override
    public boolean hasPermission(String type, Long userId, Long relId) {
        // 查询用户授权列表
        List<Long> relIdList = this.getUserAuthorizedRelIdList(type, userId);
        if (relIdList.isEmpty()) {
            return false;
        }
        return relIdList.contains(relId);
    }

    @Override
    public List<Long> getRelIdListByUserId(String type, Long userId) {
        return dataPermissionDAO.of()
                .createWrapper()
                .eq(DataPermissionDO::getType, type)
                .eq(DataPermissionDO::getUserId, userId)
                .then()
                .stream()
                .map(DataPermissionDO::getRelId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getRelIdListByRoleId(String type, Long roleId) {
        // 查询数据库
        return dataPermissionDAO.of()
                .createWrapper()
                .eq(DataPermissionDO::getType, type)
                .eq(DataPermissionDO::getRoleId, roleId)
                .then()
                .stream()
                .map(DataPermissionDO::getRelId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getUserAuthorizedRelIdList(String type, Long userId) {
        DataPermissionTypeEnum dataType = Valid.valid(DataPermissionTypeEnum::of, type);
        String cacheKey = DataPermissionCacheKeyDefine.DATA_PERMISSION_USER.format(type, userId);
        // 获取缓存
        List<Long> list = RedisLists.range(cacheKey, Long::valueOf);
        if (list.isEmpty()) {
            LambdaQueryWrapper<DataPermissionDO> wrapper = dataPermissionDAO.lambda()
                    .eq(DataPermissionDO::getType, type);
            if (dataType.isToRole()) {
                // 查询用户角色
                List<Long> roleIdList = systemUserRoleDAO.selectRoleIdByUserId(userId);
                wrapper.and(s -> s.eq(DataPermissionDO::getUserId, userId)
                        .or()
                        .in(!roleIdList.isEmpty(), DataPermissionDO::getRoleId, roleIdList)
                );
            } else {
                // 单用户
                wrapper.eq(DataPermissionDO::getUserId, userId);
            }
            // 查询数据库
            list = dataPermissionDAO.of()
                    .wrapper(wrapper)
                    .stream()
                    .map(DataPermissionDO::getRelId)
                    .distinct()
                    .collect(Collectors.toList());
            // 设置屏障 防止穿透
            CacheBarriers.LIST.check(list);
            // 设置缓存
            RedisLists.pushAll(cacheKey, DataPermissionCacheKeyDefine.DATA_PERMISSION_USER, list, String::valueOf);
        }
        // 删除屏障
        CacheBarriers.LIST.remove(list);
        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public int deleteByRelId(String type, Long relId) {
        if (relId == null) {
            return 0;
        }
        // 删除
        return this.deleteByRelIdList(type, Lists.singleton(relId));
    }

    @Override
    public int deleteByRelIdList(String type, List<Long> relIdList) {
        if (Lists.isEmpty(relIdList)) {
            return 0;
        }
        LambdaQueryWrapper<DataPermissionDO> wrapper = dataPermissionDAO.wrapper()
                .eq(DataPermissionDO::getType, type)
                .in(DataPermissionDO::getRelId, relIdList);
        // 查询
        List<DataPermissionDO> rows = dataPermissionDAO.selectList(wrapper);
        // 删除
        int effect = dataPermissionDAO.delete(wrapper);
        // 删除缓存
        Function<Function<DataPermissionDO, Long>, List<Long>> mapper =
                f -> rows.stream()
                        .map(f)
                        .distinct()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        List<Long> userIdList = mapper.apply(DataPermissionDO::getUserId);
        List<Long> roleIdList = mapper.apply(DataPermissionDO::getRoleId);
        this.deleteCache(type, userIdList, roleIdList);
        return effect;
    }

    @Override
    public int deleteByUserId(Long userId) {
        if (userId == null) {
            return 0;
        }
        // 删除
        return this.deleteByUserIdList(Lists.singleton(userId));
    }

    @Override
    public int deleteByUserIdList(List<Long> userIdList) {
        if (Lists.isEmpty(userIdList)) {
            return 0;
        }
        LambdaQueryWrapper<DataPermissionDO> wrapper = Conditions.in(DataPermissionDO::getUserId, userIdList);
        // 删除
        return dataPermissionDAO.delete(wrapper);
        // 删除缓存 让其自动过期
        // this.deleteCache(null, userIdList, null);
    }

    @Override
    public int deleteByRoleId(Long roleId) {
        LambdaQueryWrapper<DataPermissionDO> wrapper = Conditions.eq(DataPermissionDO::getRoleId, roleId);
        // 删除
        int effect = dataPermissionDAO.delete(wrapper);
        // 删除缓存
        this.deleteCache(null, null, Lists.singleton(roleId));
        return effect;
    }

    @Override
    public void clearRoleCache(Long roleId) {
        // 查询角色下的用户
        List<Long> userIdList = systemUserRoleDAO.selectUserIdByRoleId(roleId);
        if (userIdList.isEmpty()) {
            return;
        }
        this.clearUserCache(userIdList);
    }

    @Override
    public void clearUserCache(Long userId) {
        this.clearUserCache(Lists.singleton(userId));
    }

    @Override
    public void clearUserCache(List<Long> userIdList) {
        // 扫描的 key
        List<String> keyMatches = userIdList.stream()
                .distinct()
                .map(s -> DataPermissionCacheKeyDefine.DATA_PERMISSION_USER.format("*", s))
                .collect(Collectors.toList());
        // 扫描并删除
        RedisUtils.scanKeysDelete(keyMatches);
    }

    /**
     * 删除缓存
     *
     * @param type   type
     * @param userId userId
     * @param roleId roleId
     */
    private void deleteCache(String type, Long userId, Long roleId) {
        List<Long> userIdList = new ArrayList<>();
        if (userId != null) {
            userIdList.add(userId);
        }
        // 查询角色的权限
        if (roleId != null) {
            List<Long> roleUserIdList = systemUserRoleDAO.selectUserIdByRoleId(roleId);
            userIdList.addAll(roleUserIdList);
        }
        // 删除缓存
        if (!userIdList.isEmpty()) {
            List<String> keys = userIdList.stream()
                    .map(s -> DataPermissionCacheKeyDefine.DATA_PERMISSION_USER.format(type, s))
                    .collect(Collectors.toList());
            RedisUtils.delete(keys);
        }
    }

    /**
     * 删除缓存
     *
     * @param type       type
     * @param userIdList userIdList
     * @param roleIdList roleIdList
     */
    private void deleteCache(String type, List<Long> userIdList, List<Long> roleIdList) {
        Set<Long> deleteUserIdList = new HashSet<>(4);
        if (!Lists.isEmpty(userIdList)) {
            deleteUserIdList.addAll(userIdList);
        }
        // 查询角色的用户列表
        if (!Lists.isEmpty(roleIdList)) {
            List<Long> roleUserIdList = systemUserRoleDAO.selectUserIdByRoleId(roleIdList);
            deleteUserIdList.addAll(roleUserIdList);
        }
        if (deleteUserIdList.isEmpty()) {
            return;
        }
        // 删除的类型
        List<String> types;
        if (type == null) {
            types = Arrays.stream(DataPermissionTypeEnum.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
        } else {
            types = Lists.singleton(type);
        }
        // 删除缓存
        List<String> keys = new ArrayList<>();
        for (String value : types) {
            deleteUserIdList.stream()
                    .filter(Objects::nonNull)
                    .map(s -> DataPermissionCacheKeyDefine.DATA_PERMISSION_USER.format(value, s))
                    .forEach(keys::add);
        }
        RedisLists.delete(keys);
    }

}
