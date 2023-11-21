package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.mybatis.core.query.Conditions;
import com.orion.ops.framework.redis.core.utils.RedisLists;
import com.orion.ops.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.ops.module.infra.convert.TagConvert;
import com.orion.ops.module.infra.dao.TagDAO;
import com.orion.ops.module.infra.define.cache.TagCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.TagDO;
import com.orion.ops.module.infra.entity.dto.TagCacheDTO;
import com.orion.ops.module.infra.entity.request.tag.TagCreateRequest;
import com.orion.ops.module.infra.entity.vo.TagVO;
import com.orion.ops.module.infra.service.TagService;
import lombok.extern.slf4j.Slf4j;
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

}
