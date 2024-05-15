package com.orion.visor.module.infra.api;

import com.orion.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.visor.module.infra.enums.DataGroupTypeEnum;

import java.util.List;

/**
 * 数据分组用户 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupUserApi {

    /**
     * 创建数据分组
     *
     * @param type   type
     * @param userId userId
     * @param dto    dto
     * @return id
     */
    Long createDataGroup(DataGroupTypeEnum type, Long userId, DataGroupCreateDTO dto);

    /**
     * 通过缓存查询数据分组
     *
     * @param type   type
     * @param userId userId
     * @return rows
     */
    List<DataGroupDTO> getDataGroupList(DataGroupTypeEnum type, Long userId);

    /**
     * 通过缓存查询数据分组
     *
     * @param type   type
     * @param userId userId
     * @return rows
     */
    List<DataGroupDTO> getDataGroupTree(DataGroupTypeEnum type, Long userId);

    /**
     * 删除数据分组
     *
     * @param type   type
     * @param userId userId
     * @param idList idList
     * @return effect
     */
    Integer deleteDataGroupByIdList(DataGroupTypeEnum type, Long userId, List<Long> idList);

}
