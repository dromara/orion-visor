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

import cn.orionsec.kit.lang.annotation.Keep;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.module.infra.convert.TagRelConvert;
import org.dromara.visor.module.infra.dao.TagDAO;
import org.dromara.visor.module.infra.dao.TagRelDAO;
import org.dromara.visor.module.infra.define.cache.TagCacheKeyDefine;
import org.dromara.visor.module.infra.entity.domain.TagDO;
import org.dromara.visor.module.infra.entity.domain.TagRelDO;
import org.dromara.visor.module.infra.entity.dto.TagCacheDTO;
import org.dromara.visor.module.infra.entity.request.tag.TagRelQueryRequest;
import org.dromara.visor.module.infra.service.TagRelService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
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

    @Keep
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void addTagRel(String type, Long relId, List<Long> tagIdList) {
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
    }

    @Override
    public void addTagRel(String type, Map<Long, List<Long>> relTagIdList) {
        // 查询 tag
        List<Long> allTagIdList = relTagIdList.values()
                .stream()
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        List<TagDO> tagList = tagDAO.selectBatchIds(allTagIdList);
        Map<Long, String> tagNameMap = tagList.stream()
                .collect(Collectors.toMap(TagDO::getId, TagDO::getName));
        // 设置新增的引用
        List<TagRelDO> tagRelList = new ArrayList<>();
        relTagIdList.forEach((relId, tagIdList) -> {
            for (Long tagId : tagIdList) {
                tagRelList.add(TagRelDO.builder()
                        .tagId(tagId)
                        .tagName(tagNameMap.get(tagId))
                        .tagType(type)
                        .relId(relId)
                        .build());
            }
        });
        // 新增
        tagRelDAO.insertBatch(tagRelList);
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
            RedisStrings.setJson(cacheKey, TagCacheKeyDefine.TAG_REL, relCacheList);
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
                RedisStrings.setJson(cacheKey, TagCacheKeyDefine.TAG_REL, cacheValue);
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
