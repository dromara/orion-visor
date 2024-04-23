package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.convert.PathBookmarkConvert;
import com.orion.ops.module.asset.dao.PathBookmarkDAO;
import com.orion.ops.module.asset.define.cache.PathBookmarkCacheKeyDefine;
import com.orion.ops.module.asset.entity.domain.PathBookmarkDO;
import com.orion.ops.module.asset.entity.dto.PathBookmarkCacheDTO;
import com.orion.ops.module.asset.entity.request.path.PathBookmarkCreateRequest;
import com.orion.ops.module.asset.entity.request.path.PathBookmarkUpdateRequest;
import com.orion.ops.module.asset.entity.vo.PathBookmarkVO;
import com.orion.ops.module.asset.service.PathBookmarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
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
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        Long userId = SecurityUtils.getLoginUserId();
        log.info("PathBookmarkService-updatePathBookmarkById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        PathBookmarkDO record = pathBookmarkDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
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
            RedisMaps.putAllJson(cacheKey, s -> s.getId().toString(), list);
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
    public Integer deletePathBookmarkById(Long id) {
        Long userId = SecurityUtils.getLoginUserId();
        log.info("PathBookmarkService-deletePathBookmarkById id: {}", id);
        // 检查数据是否存在
        PathBookmarkDO record = pathBookmarkDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = pathBookmarkDAO.deleteById(id);
        log.info("PathBookmarkService-deletePathBookmarkById id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisMaps.delete(PathBookmarkCacheKeyDefine.PATH_BOOKMARK.format(userId), id);
        return effect;
    }

    @Override
    public Integer setGroupNull(Long userId, Long groupId) {
        int effect = pathBookmarkDAO.setGroupIdWithNull(groupId);
        // 删除缓存
        RedisMaps.delete(PathBookmarkCacheKeyDefine.PATH_BOOKMARK.format(userId));
        return effect;
    }

    @Override
    public Integer deleteByGroupId(Long userId, Long groupId) {
        int effect = pathBookmarkDAO.deleteByGroupId(groupId);
        // 删除缓存
        RedisMaps.delete(PathBookmarkCacheKeyDefine.PATH_BOOKMARK.format(userId));
        return effect;
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
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

}
