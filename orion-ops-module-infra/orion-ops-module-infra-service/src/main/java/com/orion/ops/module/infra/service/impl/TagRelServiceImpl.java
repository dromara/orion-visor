package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.collect.Maps;
import com.orion.ops.framework.redis.core.utils.RedisUtils;
import com.orion.ops.module.infra.convert.TagRelConvert;
import com.orion.ops.module.infra.dao.TagDAO;
import com.orion.ops.module.infra.dao.TagRelDAO;
import com.orion.ops.module.infra.define.TagCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.TagDO;
import com.orion.ops.module.infra.entity.domain.TagRelDO;
import com.orion.ops.module.infra.entity.dto.TagCacheDTO;
import com.orion.ops.module.infra.entity.request.tag.TagRelQueryRequest;
import com.orion.ops.module.infra.service.TagRelService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 标签引用 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-6 16:54
 */
@Service
public class TagRelServiceImpl implements TagRelService {

    @Resource
    private TagDAO tagDAO;

    @Resource
    private TagRelDAO tagRelDAO;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void addTagRel(String type, Long relId, List<Long> tagIdList) {
        // 删除引用
        TagRelQueryRequest deleteRequest = new TagRelQueryRequest();
        deleteRequest.setTagType(type);
        deleteRequest.setRelId(relId);
        LambdaQueryWrapper<TagRelDO> deleteWrapper = this.buildQueryWrapper(deleteRequest);
        tagRelDAO.delete(deleteWrapper);
        // 查询 tag
        List<TagDO> tagList = tagDAO.selectBatchIds(tagIdList);
        // 插入引用
        List<TagRelDO> tagRelList = tagList.stream()
                .map(s -> TagRelDO.builder()
                        .tagId(s.getId())
                        .tagName(s.getName())
                        .tagType(type)
                        .relId(relId)
                        .build())
                .collect(Collectors.toList());
        tagRelDAO.insertBatch(tagRelList);
        // 设置缓存
        String cacheKey = TagCacheKeyDefine.TAG_REL.format(type, relId);
        RedisUtils.setJson(cacheKey, TagCacheKeyDefine.TAG_REL, TagRelConvert.MAPPER.toCacheList(tagRelList));
    }

    @Override
    public void setTagRel(String type, Long relId, List<Long> tagIdList) {
        // 删除
        this.deleteRelId(type, relId);
        // 添加
        if (!Lists.isEmpty(tagIdList)) {
            this.addTagRel(type, relId, tagIdList);
        }
    }

    @Override
    public List<TagCacheDTO> getRelTags(String type, Long relId) {
        // 查询缓存
        String cacheKey = TagCacheKeyDefine.TAG_REL.format(type, relId);
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        if (cacheValue == null) {
            // 查询数据库
            TagRelQueryRequest queryRequest = new TagRelQueryRequest();
            queryRequest.setTagType(type);
            queryRequest.setRelId(relId);
            LambdaQueryWrapper<TagRelDO> wrapper = this.buildQueryWrapper(queryRequest);
            List<TagRelDO> relList = tagRelDAO.selectList(wrapper);
            // 设置缓存
            List<TagCacheDTO> relCacheList = TagRelConvert.MAPPER.toCacheList(relList);
            RedisUtils.setJson(cacheKey, TagCacheKeyDefine.TAG_REL, relCacheList);
            return relCacheList;
        } else {
            // 返回缓存数据
            return JSON.parseArray(cacheValue, TagCacheDTO.class);
        }
    }

    @Override
    @SuppressWarnings("ALL")
    public List<List<TagCacheDTO>> getRelTags(String type, List<Long> relIdList) {
        // 查询缓存
        List<String> cacheKeyList = relIdList.stream()
                .map(relId -> TagCacheKeyDefine.TAG_REL.format(type, relId))
                .collect(Collectors.toList());
        List<List<TagCacheDTO>> cacheValueList = redisTemplate.opsForValue()
                .multiGet(cacheKeyList)
                .stream()
                .map(s -> JSON.parseArray(s, TagCacheDTO.class))
                .collect(Collectors.toList());
        // 查询为空的缓存
        Map<Long, List<TagCacheDTO>> emptyCacheMap = Maps.newMap();
        for (int i = 0; i < relIdList.size(); i++) {
            if (cacheValueList.get(i) == null) {
                emptyCacheMap.put(relIdList.get(i), null);
            }
        }
        // 填充为空的数据
        if (!emptyCacheMap.isEmpty()) {
            // 查询数据库
            TagRelQueryRequest queryRequest = new TagRelQueryRequest();
            queryRequest.setTagType(type);
            queryRequest.setRelIdList(emptyCacheMap.keySet());
            LambdaQueryWrapper<TagRelDO> wrapper = this.buildQueryWrapper(queryRequest);
            List<TagRelDO> relList = tagRelDAO.selectList(wrapper);
            // 分组
            Map<Long, List<TagRelDO>> relGrouping = relList.stream()
                    .collect(Collectors.groupingBy(TagRelDO::getRelId));
            // 设置缓存
            emptyCacheMap.keySet().forEach(relId -> {
                String cacheKey = TagCacheKeyDefine.TAG_REL.format(type, relId);
                List<TagCacheDTO> cacheValue = TagRelConvert.MAPPER.toCacheList(relGrouping.get(relId));
                if (cacheValue == null) {
                    cacheValue = Lists.empty();
                }
                RedisUtils.setJson(cacheKey, TagCacheKeyDefine.TAG_REL, cacheValue);
                emptyCacheMap.put(relId, cacheValue);
            });
            // 设置返回
            for (int i = 0; i < relIdList.size(); i++) {
                if (cacheValueList.get(i) == null) {
                    cacheValueList.set(i, emptyCacheMap.get(relIdList.get(i)));
                }
            }
        }
        return cacheValueList;
    }

    @Override
    public List<Long> getRelIdByTagId(Long tagId) {
        return tagRelDAO.selectRelIdByTagId(tagId);
    }

    @Override
    public List<Long> getRelIdByTagId(List<Long> tagIdList) {
        return tagRelDAO.selectRelIdByTagId(tagIdList);
    }

    @Override
    public Integer deleteRelId(String type, Long relId) {
        // 删除数据库
        TagRelQueryRequest queryRequest = new TagRelQueryRequest();
        queryRequest.setTagType(type);
        queryRequest.setRelId(relId);
        LambdaQueryWrapper<TagRelDO> wrapper = this.buildQueryWrapper(queryRequest);
        int effect = tagRelDAO.delete(wrapper);
        // 删除缓存
        String cacheKey = TagCacheKeyDefine.TAG_REL.format(type, relId);
        redisTemplate.delete(cacheKey);
        return effect;
    }

    @Override
    public Integer deleteRelIdList(String type, List<Long> relIdList) {
        // 删除数据库
        TagRelQueryRequest queryRequest = new TagRelQueryRequest();
        queryRequest.setTagType(type);
        queryRequest.setRelIdList(relIdList);
        LambdaQueryWrapper<TagRelDO> wrapper = this.buildQueryWrapper(queryRequest);
        int effect = tagRelDAO.delete(wrapper);
        // 删除缓存
        List<String> cacheKeyList = relIdList.stream()
                .map(relId -> TagCacheKeyDefine.TAG_REL.format(type, relId))
                .collect(Collectors.toList());
        redisTemplate.delete(cacheKeyList);
        return effect;
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<TagRelDO> buildQueryWrapper(TagRelQueryRequest request) {
        return tagRelDAO.wrapper()
                .eq(TagRelDO::getId, request.getId())
                .eq(TagRelDO::getTagId, request.getTagId())
                .eq(TagRelDO::getTagName, request.getTagName())
                .eq(TagRelDO::getTagType, request.getTagType())
                .eq(TagRelDO::getRelId, request.getRelId())
                .in(TagRelDO::getRelId, request.getRelIdList());
    }

}
