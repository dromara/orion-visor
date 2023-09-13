package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.dto.TagCacheDTO;

import java.util.List;

/**
 * 标签引用 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-6 16:54
 */
public interface TagRelService {

    /**
     * 创建标签引用
     *
     * @param type      type
     * @param relId     relId
     * @param tagIdList tagIdList
     */
    void addTagRel(String type, Long relId, List<Long> tagIdList);

    /**
     * 设置标签引用
     *
     * @param type      type
     * @param relId     relId
     * @param tagIdList tagIdList
     */
    void setTagRel(String type, Long relId, List<Long> tagIdList);

    /**
     * 获取引用 tag
     *
     * @param type  type
     * @param relId relId
     * @return tag
     */
    List<TagCacheDTO> getRelTags(String type, Long relId);

    /**
     * 获取引用 tag
     *
     * @param type      type
     * @param relIdList relIdList
     * @return tag
     */
    List<List<TagCacheDTO>> getRelTags(String type, List<Long> relIdList);

    /**
     * 查询 tag 关联的所有 id
     *
     * @param tagId tagId
     * @return rel
     */
    List<Long> getRelIdByTagId(Long tagId);

    /**
     * 查询 tag 关联的所有 id
     *
     * @param tagIdList tagIdList
     * @return rel
     */
    List<Long> getRelIdByTagId(List<Long> tagIdList);

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
