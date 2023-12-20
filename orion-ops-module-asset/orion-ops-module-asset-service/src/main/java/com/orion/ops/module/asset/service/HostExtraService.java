package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.request.host.HostAliasUpdateRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

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
    Integer updateHostAlias(@Validated @RequestBody HostAliasUpdateRequest request);

}
