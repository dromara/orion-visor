package com.orion.visor.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.module.asset.entity.request.host.HostCreateRequest;
import com.orion.visor.module.asset.entity.request.host.HostQueryRequest;
import com.orion.visor.module.asset.entity.request.host.HostUpdateRequest;
import com.orion.visor.module.asset.entity.vo.HostVO;

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
     * @param id id
     * @return row
     */
    HostVO getHostById(Long id);

    /**
     * 查询主机
     *
     * @return rows
     */
    List<HostVO> getHostListByCache();

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

    /**
     * 通过 id 批量删除主机
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteHostByIdList(List<Long> idList);

    /**
     * 通过 id 删除主机引用
     *
     * @param idList idList
     */
    void deleteHostRelByIdListAsync(List<Long> idList);

}
