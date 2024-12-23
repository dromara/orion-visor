/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.framework.redis.core.utils.RedisLists;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.module.infra.convert.TagConvert;
import org.dromara.visor.module.infra.dao.TagDAO;
import org.dromara.visor.module.infra.define.cache.TagCacheKeyDefine;
import org.dromara.visor.module.infra.entity.domain.TagDO;
import org.dromara.visor.module.infra.entity.dto.TagCacheDTO;
import org.dromara.visor.module.infra.entity.request.tag.TagCreateRequest;
import org.dromara.visor.module.infra.entity.vo.TagVO;
import org.dromara.visor.module.infra.service.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签枚举 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 11:58
 */
@Slf4j
@Service
public class TagServiceImpl implements TagService {

    /**
     * 未使用的天数
     */
    private static final Integer UN_USED_DAYS = 3;

    @Resource
    private TagDAO tagDAO;

    @Override
    public Long createTag(TagCreateRequest request) {
        // 转换
        TagDO record = TagConvert.MAPPER.to(request);
        // 查询 tag 是否存在
        String type = record.getType();
        LambdaQueryWrapper<TagDO> wrapper = tagDAO.wrapper()
                .eq(TagDO::getName, record.getName())
                .eq(TagDO::getType, type);
        TagDO checkTag = tagDAO.of(wrapper).getOne();
        if (checkTag != null) {
            return checkTag.getId();
        }
        // 插入
        int effect = tagDAO.insert(record);
        log.info("TagService-createTag effect: {}, record: {}", effect, JSON.toJSONString(record));
        // 设置缓存
        String cacheKey = TagCacheKeyDefine.TAG_NAME.format(type);
        TagCacheDTO cache = TagConvert.MAPPER.toCache(record);
        RedisLists.pushJson(cacheKey, cache);
        RedisLists.setExpire(cacheKey, TagCacheKeyDefine.TAG_NAME);
        return record.getId();
    }

    @Override
    public List<TagVO> getTagList(String type) {
        // 查询缓存
        String cacheKey = TagCacheKeyDefine.TAG_NAME.format(type);
        List<TagCacheDTO> list = RedisLists.rangeJson(cacheKey, TagCacheKeyDefine.TAG_NAME);
        if (list.isEmpty()) {
            // 为空则需要查询缓存
            LambdaQueryWrapper<TagDO> wrapper = Conditions.eq(TagDO::getType, type);
            list = tagDAO.of(wrapper).list(TagConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, TagCacheDTO::new);
            // 设置到缓存
            RedisLists.pushAllJson(cacheKey, TagCacheKeyDefine.TAG_NAME, list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(TagConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    public Integer deleteTagById(Long id) {
        TagDO tag = tagDAO.selectById(id);
        if (tag == null) {
            return Const.N_0;
        }
        // 删除数据库
        int effect = tagDAO.deleteById(id);
        log.info("TagService-deleteTagById id: {}, effect: {}", id, effect);
        // 删除缓存
        String cacheKey = TagCacheKeyDefine.TAG_NAME.format(tag.getType());
        RedisLists.removeJson(cacheKey, TagConvert.MAPPER.toCache(tag));
        return effect;
    }

    @Override
    public Integer deleteTagByIdList(List<Long> idList) {
        List<TagDO> tagList = tagDAO.selectBatchIds(idList);
        if (tagList.isEmpty()) {
            return Const.N_0;
        }
        // 删除数据库
        int effect = tagDAO.deleteBatchIds(idList);
        log.info("TagService-deleteTagByIdList idList: {}, effect: {}", idList, effect);
        // 删除缓存
        for (TagDO tag : tagList) {
            String cacheKey = TagCacheKeyDefine.TAG_NAME.format(tag.getType());
            RedisLists.removeJson(cacheKey, TagConvert.MAPPER.toCache(tag));
        }
        return effect;
    }

    @Override
    public void clearUnusedTag() {
        // 查询
        List<TagDO> tagList = tagDAO.selectUnusedTag(UN_USED_DAYS);
        if (tagList.isEmpty()) {
            log.info("TagService.clearUnusedTag isEmpty");
            return;
        }
        // 删除数据
        List<Long> tagIdList = tagList.stream()
                .map(TagDO::getId)
                .collect(Collectors.toList());
        int effect = tagDAO.deleteBatchIds(tagIdList);
        log.info("TagService.clearUnusedTag deleted count: {}, deleted: {}, tags: {}", tagIdList.size(), effect, tagIdList);
        // 删除缓存
        List<String> cacheKeys = tagList.stream()
                .map(TagDO::getType)
                .distinct()
                .map(TagCacheKeyDefine.TAG_NAME::format)
                .collect(Collectors.toList());
        RedisLists.delete(cacheKeys);
    }

}
