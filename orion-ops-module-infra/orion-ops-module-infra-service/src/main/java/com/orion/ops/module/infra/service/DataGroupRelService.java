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
 * 数据分组关联 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupRelService {

    /**
     * 创建数据分组关联
     *
     * @param request request
     * @return id
     */
    Long createDataGroupRel(DataGroupRelCreateRequest request);

    /**
     * 更新数据分组关联
     *
     * @param request request
     * @return effect
     */
    Integer updateDataGroupRelById(DataGroupRelUpdateRequest request);

    /**
     * 根据条件更新数据分组关联
     *
     * @param query  query
     * @param update update
     * @return effect
     */
    Integer updateDataGroupRel(DataGroupRelQueryRequest query, DataGroupRelUpdateRequest update);

    /**
     * 查询数据分组关联
     *
     * @param id id
     * @return row
     */
    DataGroupRelVO getDataGroupRelById(Long id);

    /**
     * 批量查询数据分组关联
     *
     * @param idList idList
     * @return rows
     */
    List<DataGroupRelVO> getDataGroupRelByIdList(List<Long> idList);

    /**
     * 查询全部数据分组关联
     *
     * @param request request
     * @return rows
     */
    List<DataGroupRelVO> getDataGroupRelList(DataGroupRelQueryRequest request);

    /**
     * 通过缓存查询数据分组关联
     *
     * @return rows
     */
    List<DataGroupRelVO> getDataGroupRelListByCache();

    /**
     * 查询数据分组关联数量
     *
     * @param request request
     * @return count
     */
    Long getDataGroupRelCount(DataGroupRelQueryRequest request);

    /**
     * 分页查询数据分组关联
     *
     * @param request request
     * @return rows
     */
    DataGrid<DataGroupRelVO> getDataGroupRelPage(DataGroupRelQueryRequest request);

    /**
     * 删除数据分组关联
     *
     * @param id id
     * @return effect
     */
    Integer deleteDataGroupRelById(Long id);

    /**
     * 批量删除数据分组关联
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteDataGroupRelByIdList(List<Long> idList);

    /**
     * 根据条件删除数据分组关联
     *
     * @param request request
     * @return effect
     */
    Integer deleteDataGroupRel(DataGroupRelQueryRequest request);

}
