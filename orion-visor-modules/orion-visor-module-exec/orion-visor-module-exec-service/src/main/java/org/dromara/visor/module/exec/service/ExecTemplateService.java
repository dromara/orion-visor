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
package org.dromara.visor.module.exec.service;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.visor.module.exec.entity.domain.ExecTemplateDO;
import org.dromara.visor.module.exec.entity.request.exec.ExecTemplateCreateRequest;
import org.dromara.visor.module.exec.entity.request.exec.ExecTemplateQueryRequest;
import org.dromara.visor.module.exec.entity.request.exec.ExecTemplateUpdateRequest;
import org.dromara.visor.module.exec.entity.vo.ExecTemplateVO;

import java.util.List;

/**
 * 执行模板 服务类
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-7 18:08
 */
public interface ExecTemplateService {

    /**
     * 创建执行模板
     *
     * @param request request
     * @return id
     */
    Long createExecTemplate(ExecTemplateCreateRequest request);

    /**
     * 更新执行模板
     *
     * @param request request
     * @return effect
     */
    Integer updateExecTemplateById(ExecTemplateUpdateRequest request);

    /**
     * 查询执行模板
     *
     * @param id id
     * @return row
     */
    ExecTemplateVO getExecTemplateById(Long id);

    /**
     * 查询执行模板 (查询认证的主机)
     *
     * @param id id
     * @return row
     */
    ExecTemplateVO getExecTemplateWithAuthorized(Long id);

    /**
     * 分页查询执行模板
     *
     * @param request request
     * @return rows
     */
    DataGrid<ExecTemplateVO> getExecTemplatePage(ExecTemplateQueryRequest request);

    /**
     * 查询执行模板数量
     *
     * @param request request
     * @return count
     */
    Long getExecTemplateCount(ExecTemplateQueryRequest request);

    /**
     * 删除执行模板
     *
     * @param id id
     * @return effect
     */
    Integer deleteExecTemplateById(Long id);

    /**
     * 批量删除执行模板
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteExecTemplateByIdList(List<Long> idList);

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    LambdaQueryWrapper<ExecTemplateDO> buildQueryWrapper(ExecTemplateQueryRequest request);

}
