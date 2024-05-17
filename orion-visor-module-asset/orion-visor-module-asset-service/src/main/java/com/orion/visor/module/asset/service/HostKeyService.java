package com.orion.visor.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.module.asset.entity.domain.HostKeyDO;
import com.orion.visor.module.asset.entity.request.host.HostKeyCreateRequest;
import com.orion.visor.module.asset.entity.request.host.HostKeyQueryRequest;
import com.orion.visor.module.asset.entity.request.host.HostKeyUpdateRequest;
import com.orion.visor.module.asset.entity.vo.HostKeyVO;

import java.util.List;

/**
 * 主机密钥 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
public interface HostKeyService {

    /**
     * 创建主机密钥
     *
     * @param request request
     * @return id
     */
    Long createHostKey(HostKeyCreateRequest request);

    /**
     * 通过 id 更新主机密钥
     *
     * @param request request
     * @return effect
     */
    Integer updateHostKeyById(HostKeyUpdateRequest request);

    /**
     * 通过 id 查询主机密钥
     *
     * @param id id
     * @return row
     */
    HostKeyVO getHostKeyById(Long id);

    /**
     * 通过 id 查询主机密钥
     *
     * @param id id
     * @return row
     */
    HostKeyDO getHostKey(Long id);

    /**
     * 查询主机密钥
     *
     * @return rows
     */
    List<HostKeyVO> getHostKeyList();

    /**
     * 分页查询主机密钥
     *
     * @param request request
     * @return rows
     */
    DataGrid<HostKeyVO> getHostKeyPage(HostKeyQueryRequest request);

    /**
     * 通过 id 删除主机密钥
     *
     * @param id id
     * @return effect
     */
    Integer deleteHostKeyById(Long id);

}
