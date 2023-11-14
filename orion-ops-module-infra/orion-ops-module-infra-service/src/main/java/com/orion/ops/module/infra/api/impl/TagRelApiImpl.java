package com.orion.ops.module.infra.api.impl;

import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.infra.api.TagRelApi;
import com.orion.ops.module.infra.convert.TagProviderConvert;
import com.orion.ops.module.infra.entity.dto.TagCacheDTO;
import com.orion.ops.module.infra.entity.dto.tag.TagDTO;
import com.orion.ops.module.infra.enums.TagTypeEnum;
import com.orion.ops.module.infra.service.TagRelService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 标签引用 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-6 16:54
 */
@Service
public class TagRelApiImpl implements TagRelApi {

    @Resource
    private TagRelService tagRelService;

    @Override
    public void addTagRel(TagTypeEnum type, Long relId, List<Long> tagIdList) {
        Valid.notNull(relId);
        if (Lists.isEmpty(tagIdList)) {
            return;
        }
        tagRelService.addTagRel(type.name(), relId, tagIdList);
    }

    @Override
    public void setTagRel(TagTypeEnum type, Long relId, List<Long> tagIdList) {
        Valid.notNull(relId);
        tagRelService.setTagRel(type.name(), relId, tagIdList);
    }

    @Override
    @Async("asyncExecutor")
    public Future<List<TagDTO>> getRelTagsAsync(TagTypeEnum type, Long relId) {
        List<TagCacheDTO> values = tagRelService.getRelTags(type.name(), relId);
        return CompletableFuture.completedFuture(TagProviderConvert.MAPPER.toList(values));
    }

    @Override
    public List<List<TagDTO>> getRelTags(TagTypeEnum type, List<Long> relIdList) {
        return tagRelService.getRelTags(type.name(), relIdList)
                .stream()
                .map(TagProviderConvert.MAPPER::toList)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getRelIdByTagId(Long tagId) {
        return tagRelService.getRelIdByTagId(tagId);
    }

    @Override
    public List<Long> getRelIdByTagId(List<Long> tagIdList) {
        return tagRelService.getRelIdByTagId(tagIdList);
    }

    @Override
    public void deleteRelId(TagTypeEnum type, Long relId) {
        tagRelService.deleteRelId(type.name(), relId);
    }

    @Override
    public void deleteRelIdList(TagTypeEnum type, List<Long> relIdList) {
        tagRelService.deleteRelIdList(type.name(), relIdList);
    }

}
