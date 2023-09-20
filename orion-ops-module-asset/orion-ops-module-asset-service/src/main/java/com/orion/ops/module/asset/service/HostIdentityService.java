package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.vo.*;
import com.orion.ops.module.asset.entity.request.host.*;
import com.orion.ops.module.asset.entity.export.*;
import com.orion.ops.module.asset.convert.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 主机身份 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
public interface HostIdentityService {

    /**
     * 创建主机身份
     *
     * @param request request
     * @return id
     */
    Long createHostIdentity(HostIdentityCreateRequest request);

    /**
     * 通过 id 更新主机身份
     *
     * @param request request
     * @return effect
     */
    Integer updateHostIdentityById(HostIdentityUpdateRequest request);

    /**
     * 更新主机身份
     *
     * @param query  query
     * @param update update
     * @return effect
     */
    Integer updateHostIdentity(HostIdentityQueryRequest query, HostIdentityUpdateRequest update);

    /**
     * 通过 id 查询主机身份
     *
     * @param id id
     * @return row
     */
    HostIdentityVO getHostIdentityById(Long id);

    /**
     * 通过 id 批量查询主机身份
     *
     * @param idList idList
     * @return rows
     */
    List<HostIdentityVO> getHostIdentityByIdList(List<Long> idList);

    /**
     * 查询主机身份
     *
     * @param request request
     * @return rows
     */
    List<HostIdentityVO> getHostIdentityList(HostIdentityQueryRequest request);

    /**
     * 查询主机身份数量
     *
     * @param request request
     * @return count
     */
    Long getHostIdentityCount(HostIdentityQueryRequest request);

    /**
     * 分页查询主机身份
     *
     * @param request request
     * @return rows
     */
    DataGrid<HostIdentityVO> getHostIdentityPage(HostIdentityQueryRequest request);

    /**
     * 通过 id 删除主机身份
     *
     * @param id id
     * @return effect
     */
    Integer deleteHostIdentityById(Long id);

    /**
     * 通过 id 批量删除主机身份
     *
     * @param idList idList
     * @return effect
     */
    Integer batchDeleteHostIdentityByIdList(List<Long> idList);

    /**
     * 删除主机身份
     *
     * @param request request
     * @return effect
     */
    Integer deleteHostIdentity(HostIdentityQueryRequest request);

    /**
     * 导出主机身份
     *
     * @param request  request
     * @param response response
     * @throws IOException IOException
     */
    void exportHostIdentity(HostIdentityQueryRequest request, HttpServletResponse response) throws IOException;

}
