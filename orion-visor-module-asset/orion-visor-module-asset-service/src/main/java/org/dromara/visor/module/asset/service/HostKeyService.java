/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
import org.dromara.visor.module.asset.entity.domain.HostKeyDO;
import org.dromara.visor.module.asset.entity.request.host.HostKeyCreateRequest;
import org.dromara.visor.module.asset.entity.request.host.HostKeyQueryRequest;
import org.dromara.visor.module.asset.entity.request.host.HostKeyUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.HostKeyVO;

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
     * 通过 id 批量删除主机密钥
     *
     * @param id id
     * @return effect
     */
    Integer deleteHostKeyById(Long id);

    /**
     * 通过 id 删除主机密钥
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteHostKeyByIdList(List<Long> idList);

}
