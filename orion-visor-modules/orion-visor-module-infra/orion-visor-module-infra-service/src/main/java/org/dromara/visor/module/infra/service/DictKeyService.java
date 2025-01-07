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
package org.dromara.visor.module.infra.service;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import org.dromara.visor.module.infra.entity.request.dict.DictKeyCreateRequest;
import org.dromara.visor.module.infra.entity.request.dict.DictKeyQueryRequest;
import org.dromara.visor.module.infra.entity.request.dict.DictKeyUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.DictKeyVO;

import java.util.List;
import java.util.Map;

/**
 * 字典配置项 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
public interface DictKeyService {

    /**
     * 创建字典配置项
     *
     * @param request request
     * @return id
     */
    Long createDictKey(DictKeyCreateRequest request);

    /**
     * 更新字典配置项
     *
     * @param request request
     * @return effect
     */
    Integer updateDictKeyById(DictKeyUpdateRequest request);

    /**
     * 查询全部字典配置项
     *
     * @return rows
     */
    List<DictKeyVO> getDictKeyList();

    /**
     * 分页 查询字典配置项
     *
     * @param request request
     * @return rows
     */
    DataGrid<DictKeyVO> getDictKeyPage(DictKeyQueryRequest request);

    /**
     * 查询字典配置项 schema
     *
     * @param key key
     * @return schema
     */
    Map<String, String> getDictSchema(String key);

    /**
     * 刷新字典缓存
     */
    void refreshCache();

    /**
     * 删除字典配置项
     *
     * @param id id
     * @return effect
     */
    Integer deleteDictKeyById(Long id);

    /**
     * 批量删除字典配置项
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteDictKeyByIdList(List<Long> idList);

}
