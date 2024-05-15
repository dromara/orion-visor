package com.orion.visor.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.module.asset.entity.request.host.HostIdentityCreateRequest;
import com.orion.visor.module.asset.entity.request.host.HostIdentityQueryRequest;
import com.orion.visor.module.asset.entity.request.host.HostIdentityUpdateRequest;
import com.orion.visor.module.asset.entity.vo.HostIdentityVO;

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
     * 通过 id 查询主机身份
     *
     * @param id id
     * @return row
     */
    HostIdentityVO getHostIdentityById(Long id);

    /**
     * 查询主机身份
     *
     * @return rows
     */
    List<HostIdentityVO> getHostIdentityList();

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

}
