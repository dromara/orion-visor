package com.orion.ops.module.infra.api;

import com.orion.ops.module.infra.entity.dto.data.DataGroupRelCreateDTO;
import com.orion.ops.module.infra.enums.DataGroupTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据分组关联 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupRelApi {

    /**
     * 添加关联
     *
     * @param dto dto
     */
    void addGroupRel(DataGroupRelCreateDTO dto);

    /**
     * 添加关联
     *
     * @param list list
     */
    void addGroupRel(List<DataGroupRelCreateDTO> list);

    /**
     * 通过缓存查询数据分组关联
     * <p>
     * groupId - relId
     *
     * @param type type
     * @return rows
     */
    Map<Long, Set<Long>> getGroupRelList(DataGroupTypeEnum type);

    /**
     * 通过缓存查询数据分组关联
     *
     * @param type    type
     * @param groupId groupId
     * @return rows
     */
    Set<Long> getGroupRelList(DataGroupTypeEnum type, Long groupId);

    /**
     * 删除数据分组关联
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    Integer deleteByRelId(DataGroupTypeEnum type, Long relId);

    /**
     * 批量删除数据分组关联
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    Integer deleteByRelIdList(DataGroupTypeEnum type, List<Long> relIdList);

    /**
     * 批量删除数据分组关联
     *
     * @param type        type
     * @param groupIdList groupIdList
     * @return effect
     */
    Integer deleteByGroupIdList(DataGroupTypeEnum type, List<Long> groupIdList);

}
