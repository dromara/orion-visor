package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.dto.DataGroupRelCacheDTO;
import com.orion.ops.module.infra.entity.request.data.DataGroupRelCreateRequest;
import com.orion.ops.module.infra.entity.request.data.DataGroupRelUpdateRequest;

import java.util.List;

/**
 * 数据分组关联 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupRelService {

    /**
     * 设置关联
     *
     * @param request request
     */
    void updateGroupRel(DataGroupRelUpdateRequest request);

    /**
     * 添加关联
     *
     * @param request request
     */
    void addGroupRel(DataGroupRelCreateRequest request);

    /**
     * 添加关联
     *
     * @param list list
     */
    void addGroupRel(List<DataGroupRelCreateRequest> list);

    /**
     * 通过缓存查询数据分组关联
     *
     * @param type type
     * @return rows
     */
    List<DataGroupRelCacheDTO> getGroupRelListByCache(String type);

    /**
     * 通过缓存查询数据分组关联
     *
     * @param type    type
     * @param groupId groupId
     * @return rows
     */
    List<DataGroupRelCacheDTO> getGroupRelListByCache(String type, Long groupId);

    /**
     * 删除数据分组关联
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    Integer deleteByRelId(String type, Long relId);

    /**
     * 批量删除数据分组关联
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    Integer deleteByRelIdList(String type, List<Long> relIdList);

    /**
     * 批量删除数据分组关联
     *
     * @param type        type
     * @param groupIdList groupIdList
     * @return effect
     */
    Integer deleteByGroupIdList(String type, List<Long> groupIdList);

}
