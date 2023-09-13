package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.request.host.HostCreateRequest;
import com.orion.ops.module.asset.entity.request.host.HostQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostVO;

import java.util.List;

/**
 * 主机 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
public interface HostService {

    /**
     * 创建主机
     *
     * @param request request
     * @return id
     */
    Long createHost(HostCreateRequest request);

    /**
     * 通过 id 更新主机
     *
     * @param request request
     * @return effect
     */
    Integer updateHostById(HostUpdateRequest request);

    /**
     * 通过 id 查询主机
     *
     * @param request request
     * @return row
     */
    HostVO getHostById(HostQueryRequest request);

    /**
     * 查询主机
     *
     * @param request request
     * @return rows
     */
    List<HostVO> getHostList(HostQueryRequest request);

    /**
     * 分页查询主机
     *
     * @param request request
     * @return rows
     */
    DataGrid<HostVO> getHostPage(HostQueryRequest request);

    /**
     * 通过 id 删除主机
     *
     * @param id id
     * @return effect
     */
    Integer deleteHostById(Long id);

}
