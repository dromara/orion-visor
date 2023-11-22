package com.orion.ops.module.infra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.mybatis.core.query.Conditions;
import com.orion.ops.framework.redis.core.utils.RedisLists;
import com.orion.ops.framework.redis.core.utils.RedisUtils;
import com.orion.ops.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.ops.module.infra.dao.DataPermissionDAO;
import com.orion.ops.module.infra.dao.SystemUserRoleDAO;
import com.orion.ops.module.infra.define.cache.DataPermissionCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.DataPermissionDO;
import com.orion.ops.module.infra.entity.request.data.DataPermissionCreateRequest;
import com.orion.ops.module.infra.entity.request.data.DataPermissionUpdateRequest;
import com.orion.ops.module.infra.service.DataPermissionService;
import lombok.extern.slf4j.Slf4j;
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
    public void addDataPermission(DataPermissionCreateRequest request) {
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
        if (Lists.isEmpty(request.getRelIdList())) {
            LambdaQueryWrapper<DataPermissionDO> wrapper = dataPermissionDAO.wrapper()
                    .eq(DataPermissionDO::getUserId, userId)
                    .eq(DataPermissionDO::getRoleId, roleId)
                    .eq(DataPermissionDO::getType, type);
            dataPermissionDAO.delete(wrapper);
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
        // 删除缓存
        this.deleteCache(type, userId, roleId);
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
    public List<Long> getAllowRelIdList(String type, Long userId) {
        String cacheKey = DataPermissionCacheKeyDefine.DATA_PERMISSION_USER.format(type, userId);
        // 获取缓存
        List<Long> list = RedisLists.range(cacheKey, Long::valueOf);
        if (list.isEmpty()) {
            LambdaQueryWrapper<DataPermissionDO> wrapper = dataPermissionDAO.lambda()
                    .eq(DataPermissionDO::getType, type)
                    .eq(DataPermissionDO::getUserId, userId);
            // 查询用户角色
            List<Long> roleIdList = systemUserRoleDAO.selectRoleIdByUserId(userId);
            if (!roleIdList.isEmpty()) {
                wrapper.or().in(DataPermissionDO::getRoleId, roleIdList);
            }
            // 查询数据库
            list = dataPermissionDAO.of()
                    .wrapper(wrapper)
                    .stream()
                    .map(DataPermissionDO::getRelId)
                    .distinct()
                    .collect(Collectors.toList());
            // 设置屏障 防止穿透
            CacheBarriers.LONG.check(list);
            // 设置缓存
            RedisLists.pushAll(cacheKey, DataPermissionCacheKeyDefine.DATA_PERMISSION_USER, list, String::valueOf);
        }
        // 删除屏障
        CacheBarriers.LONG.remove(list);
        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public int deleteByRelId(String type, Long relId) {
        LambdaQueryWrapper<DataPermissionDO> wrapper = dataPermissionDAO.wrapper()
                .eq(DataPermissionDO::getType, type)
                .eq(DataPermissionDO::getRelId, relId);
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
        this.deleteCache(Lists.singleton(type), userIdList, roleIdList);
        return effect;
    }

    @Override
    public int deleteByUserId(Long userId) {
        LambdaQueryWrapper<DataPermissionDO> wrapper = Conditions.eq(DataPermissionDO::getUserId, userId);
        // 查询
        List<String> typeList = dataPermissionDAO.of()
                .wrapper(wrapper)
                .stream()
                .map(DataPermissionDO::getType)
                .distinct()
                .collect(Collectors.toList());
        // 删除
        int effect = dataPermissionDAO.delete(wrapper);
        // 删除缓存
        this.deleteCache(typeList, Lists.singleton(userId), null);
        return effect;
    }

    @Override
    public int deleteByRoleId(Long roleId) {
        LambdaQueryWrapper<DataPermissionDO> wrapper = Conditions.eq(DataPermissionDO::getRoleId, roleId);
        // 查询
        List<String> typeList = dataPermissionDAO.of()
                .wrapper(wrapper)
                .stream()
                .map(DataPermissionDO::getType)
                .distinct()
                .collect(Collectors.toList());
        // 删除
        int effect = dataPermissionDAO.delete(wrapper);
        // 删除缓存
        this.deleteCache(typeList, null, Lists.singleton(roleId));
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
        List<String> keyMatchs = userIdList.stream()
                .distinct()
                .map(s -> DataPermissionCacheKeyDefine.DATA_PERMISSION_USER.format("*", s))
                .collect(Collectors.toList());
        // 扫描并删除
        RedisUtils.scanKeysDelete(keyMatchs);
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
        List<Long> roleUserIdList = systemUserRoleDAO.selectUserIdByRoleId(roleId);
        userIdList.addAll(roleUserIdList);
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
     * @param typeList   typeList
     * @param userIdList userIdList
     * @param roleIdList roleIdList
     */
    private void deleteCache(List<String> typeList, List<Long> userIdList, List<Long> roleIdList) {
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
        // 删除缓存
        List<String> keys = new ArrayList<>();
        for (String type : typeList) {
            userIdList.stream()
                    .filter(Objects::nonNull)
                    .map(s -> DataPermissionCacheKeyDefine.DATA_PERMISSION_USER.format(type, s))
                    .forEach(keys::add);
        }
        RedisLists.delete(keys);
    }

}
