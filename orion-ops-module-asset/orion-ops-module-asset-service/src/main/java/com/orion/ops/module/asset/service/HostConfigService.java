package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.dto.host.HostConfigContent;
import com.orion.ops.module.asset.entity.request.host.HostConfigUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostConfigVO;

import java.util.List;

/**
 * 主机配置 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
public interface HostConfigService {

    /**
     * 获取配置
     *
     * @param hostId hostId
     * @param type   type
     * @return 配置
     */
    HostConfigVO getHostConfig(Long hostId, String type);

    /**
     * 获取配置
     *
     * @param hostId hostId
     * @param type   type
     * @param clazz  class
     * @return 配置
     */
    <T extends HostConfigContent> T getHostConfig(Long hostId, String type, Class<T> clazz);

    /**
     * 获取配置
     *
     * @param hostId hostId
     * @return 配置
     */
    List<HostConfigVO> getHostConfig(Long hostId);

    /**
     * 更新配置
     *
     * @param request request
     */
    void updateHostConfig(HostConfigUpdateRequest request);

}
