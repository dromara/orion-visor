package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.request.host.HostAliasUpdateRequest;
import com.orion.ops.module.asset.entity.request.host.HostExtraQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostExtraUpdateRequest;

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
     * 修改主机别名
     *
     * @param request request
     * @return effect
     */
    Integer updateHostAlias(HostAliasUpdateRequest request);

    /**
     * 获取主机额外配置
     *
     * @param hostId hostId
     * @param item   item
     * @return extra
     */
    Map<String, Object> getHostExtra(Long hostId, String item);

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

    /**
     * 删除主机秘钥回调
     *
     * @param id id
     */
    void deleteHostKeyCallback(Long id);

    /**
     * 删除主机身份回调
     *
     * @param id id
     */
    void deleteHostIdentityCallback(Long id);

}
