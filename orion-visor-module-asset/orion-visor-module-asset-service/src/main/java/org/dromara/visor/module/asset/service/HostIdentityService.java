/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.asset.service;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import org.dromara.visor.module.asset.entity.request.host.HostIdentityCreateRequest;
import org.dromara.visor.module.asset.entity.request.host.HostIdentityQueryRequest;
import org.dromara.visor.module.asset.entity.request.host.HostIdentityUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.HostIdentityVO;

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

    /**
     * 通过 id 批量删除主机身份
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteHostIdentityByIdList(List<Long> idList);

}
