package com.orion.visor.module.infra.api;

import com.orion.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupMoveDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupRenameDTO;
import com.orion.visor.module.infra.enums.DataGroupTypeEnum;

import java.util.List;

/**
 * 数据分组 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupApi {

    /**
     * 创建数据分组
     *
     * @param type type
     * @param dto  dto
     * @return id
     */
    Long createDataGroup(DataGroupTypeEnum type, DataGroupCreateDTO dto);

    /**
     * 重命名分组
     *
     * @param dto dto
     * @return effect
     */
    Integer renameDataGroup(DataGroupRenameDTO dto);

    /**
     * 移动分组
     *
     * @param dto dto
     * @return effect
     */
    Integer moveDataGroup(DataGroupMoveDTO dto);

    /**
     * 通过缓存查询数据分组
     *
     * @param type type
     * @return rows
     */
    List<DataGroupDTO> getDataGroupList(DataGroupTypeEnum type);

    /**
     * 通过缓存查询数据分组
     *
     * @param type type
     * @return rows
     */
    List<DataGroupDTO> getDataGroupTree(DataGroupTypeEnum type);

    /**
     * 通过 id 查询
     *
     * @param idList idList
     * @return rows
     */
    List<DataGroupDTO> getByIdList(List<Long> idList);

    /**
     * 删除数据分组
     *
     * @param id id
     * @return effect
     */
    Integer deleteDataGroupById(Long id);

    /**
     * 删除数据分组
     *
     * @param type   type
     * @param idList idList
     * @return effect
     */
    Integer deleteDataGroupByIdList(DataGroupTypeEnum type, List<Long> idList);

}
