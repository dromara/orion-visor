package com.orion.ops.module.infra.api.impl;

import com.orion.ops.module.infra.api.TagRelApi;
import com.orion.ops.module.infra.convert.TagProviderConvert;
import com.orion.ops.module.infra.entity.dto.TagCacheDTO;
import com.orion.ops.module.infra.entity.dto.tag.TagDTO;
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
    @Async("asyncExecutor")
    public void addTagRel(String type, Long relId, List<Long> tagIdList) {
        tagRelService.addTagRel(type, relId, tagIdList);
    }

    @Override
    @Async("asyncExecutor")
    public Future<List<TagDTO>> getRelTags(String type, Long relId) {
        List<TagCacheDTO> values = tagRelService.getRelTags(type, relId);
        return CompletableFuture.completedFuture(TagProviderConvert.MAPPER.toList(values));
    }

    @Override
    @Async("asyncExecutor")
    public Future<List<List<TagDTO>>> getRelTags(String type, List<Long> relIdList) {
        List<List<TagDTO>> values = tagRelService.getRelTags(type, relIdList)
                .stream()
                .map(TagProviderConvert.MAPPER::toList)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(values);
    }

    @Override
    public Integer deleteRelId(String type, Long relId) {
        return tagRelService.deleteRelId(type, relId);
    }

    @Override
    public Integer deleteRelIdList(String type, List<Long> relIdList) {
        return tagRelService.deleteRelIdList(type, relIdList);
    }

}
