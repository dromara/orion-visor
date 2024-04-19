package com.orion.ops.module.asset.service;

import com.orion.ops.framework.common.handler.data.model.GenericsDataModel;
import com.orion.ops.module.asset.entity.request.host.HostExtraQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostExtraUpdateRequest;
import com.orion.ops.module.asset.enums.HostExtraItemEnum;

import java.util.Map;

/**
 * 主机拓展信息 服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 12:04
 */
public interface HostExtraService {

    /**
     * 获取主机额外配置
     *
     * @param hostId hostId
     * @param item   item
     * @return extra
     */
    Map<String, Object> getHostExtra(Long hostId, String item);

    /**
     * 获取主机额外配置
     *
     * @param userId userId
     * @param hostId hostId
     * @param item   item
     * @param <T>    T
     * @return extra
     */
    <T extends GenericsDataModel> T getHostExtra(Long userId, Long hostId, HostExtraItemEnum item);

    /**
     * 获取多个主机拓展信息
     *
     * @param request request
     * @return type:extra
     */
    Map<String, Map<String, Object>> getHostExtraList(HostExtraQueryRequest request);

    /**
     * 修改主机拓展信息
     *
     * @param request request
     * @return effect
     */
    Integer updateHostExtra(HostExtraUpdateRequest request);

}
