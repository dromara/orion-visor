package com.orion.ops.module.infra.api;

import com.orion.ops.module.infra.entity.dto.tag.TagDTO;

import java.util.List;
import java.util.concurrent.Future;

/**
 * 标签引用 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-6 16:54
 */
public interface TagRelApi {

    /**
     * 创建标签引用
     *
     * @param type      type
     * @param relId     relId
     * @param tagIdList tagIdList
     */
    void addTagRel(String type, Long relId, List<Long> tagIdList);

    /**
     * 获取引用 tag
     *
     * @param type  type
     * @param relId relId
     * @return tag
     */
    Future<List<TagDTO>> getRelTags(String type, Long relId);

    /**
     * 获取引用 tag
     *
     * @param type      type
     * @param relIdList relIdList
     * @return tag
     */
    Future<List<List<TagDTO>>> getRelTags(String type, List<Long> relIdList);

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    Integer deleteRelId(String type, Long relId);

    /**
     * 通过 relIdList 删除
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    Integer deleteRelIdList(String type, List<Long> relIdList);

}
