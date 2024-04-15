package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.request.tag.TagCreateRequest;
import com.orion.ops.module.infra.entity.vo.TagVO;

import java.util.List;

/**
 * 标签枚举 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 11:58
 */
public interface TagService {

    /**
     * 创建标签枚举
     *
     * @param request request
     * @return id
     */
    Long createTag(TagCreateRequest request);

    /**
     * 查询标签枚举
     *
     * @param type type
     * @return rows
     */
    List<TagVO> getTagList(String type);

    /**
     * 通过 id 删除标签枚举
     *
     * @param id id
     * @return effect
     */
    Integer deleteTagById(Long id);

    /**
     * 通过 id 删除标签枚举
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteTagByIdList(List<Long> idList);

    /**
     * 清理未使用的 tag
     */
    void clearUnusedTag();

}
