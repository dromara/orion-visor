package com.orion.ops.module.infra.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.request.data.*;
import com.orion.ops.module.infra.convert.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.define.cache.*;
import com.orion.ops.module.infra.define.operator.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 数据分组 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupService {

    /**
     * 创建数据分组
     *
     * @param request request
     * @return id
     */
    Long createDataGroup(DataGroupCreateRequest request);

    /**
     * 更新数据分组
     *
     * @param request request
     * @return effect
     */
    Integer updateDataGroupById(DataGroupUpdateRequest request);

    /**
     * 根据条件更新数据分组
     *
     * @param query  query
     * @param update update
     * @return effect
     */
    Integer updateDataGroup(DataGroupQueryRequest query, DataGroupUpdateRequest update);

    /**
     * 查询数据分组
     *
     * @param id id
     * @return row
     */
    DataGroupVO getDataGroupById(Long id);

    /**
     * 批量查询数据分组
     *
     * @param idList idList
     * @return rows
     */
    List<DataGroupVO> getDataGroupByIdList(List<Long> idList);

    /**
     * 查询全部数据分组
     *
     * @param request request
     * @return rows
     */
    List<DataGroupVO> getDataGroupList(DataGroupQueryRequest request);

    /**
     * 通过缓存查询数据分组
     *
     * @return rows
     */
    List<DataGroupVO> getDataGroupListByCache();

    /**
     * 查询数据分组数量
     *
     * @param request request
     * @return count
     */
    Long getDataGroupCount(DataGroupQueryRequest request);

    /**
     * 分页查询数据分组
     *
     * @param request request
     * @return rows
     */
    DataGrid<DataGroupVO> getDataGroupPage(DataGroupQueryRequest request);

    /**
     * 删除数据分组
     *
     * @param id id
     * @return effect
     */
    Integer deleteDataGroupById(Long id);

    /**
     * 批量删除数据分组
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteDataGroupByIdList(List<Long> idList);

    /**
     * 根据条件删除数据分组
     *
     * @param request request
     * @return effect
     */
    Integer deleteDataGroup(DataGroupQueryRequest request);

}
