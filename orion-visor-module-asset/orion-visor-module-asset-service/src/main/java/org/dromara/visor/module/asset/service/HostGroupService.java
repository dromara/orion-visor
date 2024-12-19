/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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

import org.dromara.visor.module.asset.entity.request.host.HostGroupRelUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.HostGroupTreeVO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupMoveDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupRenameDTO;

import java.util.List;
import java.util.Set;

/**
 * 主机分组服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 10:59
 */
public interface HostGroupService {

    /**
     * 创建主机分组
     *
     * @param request request
     * @return id
     */
    Long createHostGroup(DataGroupCreateDTO request);

    /**
     * 查询主机分组树
     *
     * @return tree
     */
    List<HostGroupTreeVO> queryHostGroupTree();

    /**
     * 更新主机分组名称
     *
     * @param request request
     * @return effect
     */
    Integer updateHostGroupName(DataGroupRenameDTO request);

    /**
     * 移动主机分组
     *
     * @param request request
     * @return effect
     */
    Integer moveHostGroup(DataGroupMoveDTO request);

    /**
     * 删除主机分组
     *
     * @param id id
     * @return effect
     */
    Integer deleteHostGroup(Long id);

    /**
     * 查询主机分组内主机
     *
     * @param groupId groupId
     * @return hostId
     */
    Set<Long> queryHostGroupRel(Long groupId);

    /**
     * 修改主机分组内主机
     *
     * @param request request
     */
    void updateHostGroupRel(HostGroupRelUpdateRequest request);

}
