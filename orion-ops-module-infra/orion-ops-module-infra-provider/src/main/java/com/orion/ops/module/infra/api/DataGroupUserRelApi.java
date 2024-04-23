package com.orion.ops.module.infra.api;

import com.orion.ops.module.infra.entity.dto.data.DataGroupRelCreateDTO;
import com.orion.ops.module.infra.enums.DataGroupTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * 数据分组用户关联 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupUserRelApi {

    /**
     * 设置关联
     *
     * @param type        type
     * @param userId      userId
     * @param groupIdList groupIdList
     * @param relId       relId
     */
    void updateGroupRel(DataGroupTypeEnum type, Long userId, List<Long> groupIdList, Long relId);

    /**
     * 添加关联
     *
     * @param type    type
     * @param userId  userId
     * @param groupId groupId
     * @param relId   relId
     */
    void addGroupRel(DataGroupTypeEnum type, Long userId, Long groupId, Long relId);

    /**
     * 批量添加关联
     *
     * @param type   type
     * @param userId userId
     * @param list   list
     */
    void addGroupRel(DataGroupTypeEnum type, Long userId, List<DataGroupRelCreateDTO> list);

    /**
     * 通过 type 查询 relId 缓存
     * <p>
     * groupId - relId
     *
     * @param type   type
     * @param userId userId
     * @return rows
     */
    Map<Long, Set<Long>> getGroupRelList(DataGroupTypeEnum type, Long userId);

    /**
     * 通过 groupId 查询 relId 缓存
     *
     * @param type    type
     * @param userId  userId
     * @param groupId groupId
     * @return relId
     */
    Set<Long> getGroupRelIdByGroupId(DataGroupTypeEnum type, Long userId, Long groupId);

    /**
     * 通过 relId 查询 groupId
     *
     * @param type  type
     * @param relId relId
     * @return groupId
     */
    Future<Set<Long>> getGroupIdByRelIdAsync(DataGroupTypeEnum type, Long userId, Long relId);

    /**
     * 删除数据分组关联
     *
     * @param type   type
     * @param userId userId
     * @param relId  relId
     * @return effect
     */
    Integer deleteByRelId(DataGroupTypeEnum type, Long userId, Long relId);

    /**
     * 批量删除数据分组关联
     *
     * @param type      type
     * @param userId    userId
     * @param relIdList relIdList
     * @return effect
     */
    Integer deleteByRelIdList(DataGroupTypeEnum type, Long userId, List<Long> relIdList);

    /**
     * 批量删除数据分组关联
     *
     * @param type        type
     * @param userId      userId
     * @param groupIdList groupIdList
     * @return effect
     */
    Integer deleteByGroupIdList(DataGroupTypeEnum type, Long userId, List<Long> groupIdList);

}
