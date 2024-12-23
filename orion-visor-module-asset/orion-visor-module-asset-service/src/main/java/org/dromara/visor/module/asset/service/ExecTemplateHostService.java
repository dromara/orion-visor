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

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 执行模板主机 服务类
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-22 12:13
 */
public interface ExecTemplateHostService {

    /**
     * 查询模板主机
     *
     * @param templateId templateId
     * @return hostId
     */
    Set<Long> getHostByTemplateId(Long templateId);

    /**
     * 查询模板主机
     *
     * @param templateIdList templateIdList
     * @return hostIdMap
     */
    Map<Long, Set<Long>> getHostByTemplateIdList(List<Long> templateIdList);

    /**
     * 设置模板主机
     *
     * @param templateId templateId
     * @param hostList   hostList
     */
    void setTemplateHost(Long templateId, List<Long> hostList);

    /**
     * 通过 templateId 删除
     *
     * @param templateId templateId
     * @return effect
     */
    Integer deleteByTemplateId(Long templateId);

    /**
     * 通过 templateId 删除
     *
     * @param templateIdList templateIdList
     * @return effect
     */
    Integer deleteByTemplateIdList(List<Long> templateIdList);

    /**
     * 通过 hostId 删除
     *
     * @param hostId hostId
     * @return effect
     */
    Integer deleteByHostId(Long hostId);

    /**
     * 通过 hostId 删除
     *
     * @param hostIdList hostIdList
     * @return effect
     */
    Integer deleteByHostIdList(List<Long> hostIdList);

}
