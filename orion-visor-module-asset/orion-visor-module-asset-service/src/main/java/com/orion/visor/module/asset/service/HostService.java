package com.orion.visor.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.module.asset.entity.request.host.*;
import com.orion.visor.module.asset.entity.vo.HostConfigVO;
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
     * 更新主机状态
     *
     * @param request request
     * @return effect
     */
    Integer updateHostStatus(HostUpdateStatusRequest request);

    /**
     * 更新主机配置
     *
     * @param request request
     * @return effect
     */
    Integer updateHostConfig(HostUpdateConfigRequest request);

    /**
     * 通过 id 查询主机
     *
     * @param id id
     * @return row
     */
    HostVO getHostById(Long id);

    /**
     * 查询主机配置
     *
     * @param id id
     * @return config
     */
    HostConfigVO getHostConfig(Long id);

    /**
     * 查询主机
     *
     * @param type type
     * @return rows
     */
    List<HostVO> getHostList(String type);

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

    /**
     * 获取当前更新配置的 hostId
     *
     * @return hostId
     */
    Long getCurrentUpdateConfigHostId();

}
