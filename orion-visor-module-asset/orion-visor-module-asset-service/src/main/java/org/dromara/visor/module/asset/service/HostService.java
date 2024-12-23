/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.request.host.*;
import org.dromara.visor.module.asset.entity.vo.HostConfigVO;
import org.dromara.visor.module.asset.entity.vo.HostVO;

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
     * 查询主机数量
     *
     * @param request request
     * @return count
     */
    Long getHostCount(HostQueryRequest request);

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

    /**
     * 清除缓存
     */
    void clearCache();

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    LambdaQueryWrapper<HostDO> buildQueryWrapper(HostQueryRequest request);

}
