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
 * 主机秘钥 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
public interface HostKeyService {

    /**
     * 创建主机秘钥
     *
     * @param request request
     * @return id
     */
    Long createHostKey(HostKeyCreateRequest request);

    /**
     * 通过 id 更新主机秘钥
     *
     * @param request request
     * @return effect
     */
    Integer updateHostKeyById(HostKeyUpdateRequest request);

    /**
     * 更新主机秘钥
     *
     * @param query  query
     * @param update update
     * @return effect
     */
    Integer updateHostKey(HostKeyQueryRequest query, HostKeyUpdateRequest update);

    /**
     * 通过 id 查询主机秘钥
     *
     * @param id id
     * @return row
     */
    HostKeyVO getHostKeyById(Long id);

    /**
     * 通过 id 批量查询主机秘钥
     *
     * @param idList idList
     * @return rows
     */
    List<HostKeyVO> getHostKeyByIdList(List<Long> idList);

    /**
     * 查询主机秘钥
     *
     * @param request request
     * @return rows
     */
    List<HostKeyVO> getHostKeyList(HostKeyQueryRequest request);

    /**
     * 查询主机秘钥数量
     *
     * @param request request
     * @return count
     */
    Long getHostKeyCount(HostKeyQueryRequest request);

    /**
     * 分页查询主机秘钥
     *
     * @param request request
     * @return rows
     */
    DataGrid<HostKeyVO> getHostKeyPage(HostKeyQueryRequest request);

    /**
     * 通过 id 删除主机秘钥
     *
     * @param id id
     * @return effect
     */
    Integer deleteHostKeyById(Long id);

    /**
     * 通过 id 批量删除主机秘钥
     *
     * @param idList idList
     * @return effect
     */
    Integer batchDeleteHostKeyByIdList(List<Long> idList);

    /**
     * 删除主机秘钥
     *
     * @param request request
     * @return effect
     */
    Integer deleteHostKey(HostKeyQueryRequest request);

    /**
     * 导出主机秘钥
     *
     * @param request  request
     * @param response response
     * @throws IOException IOException
     */
    void exportHostKey(HostKeyQueryRequest request, HttpServletResponse response) throws IOException;

}
