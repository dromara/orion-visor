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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.visor.module.infra.entity.domain.NotifyTemplateDO;
import org.dromara.visor.module.infra.entity.dto.NotifyTemplateDetailCacheDTO;
import org.dromara.visor.module.infra.entity.request.notify.NotifyTemplateCreateRequest;
import org.dromara.visor.module.infra.entity.request.notify.NotifyTemplateQueryRequest;
import org.dromara.visor.module.infra.entity.request.notify.NotifyTemplateUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.NotifyTemplateVO;

import java.util.List;

/**
 * 通知模板 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-13 21:05
 */
public interface NotifyTemplateService {

    /**
     * 创建通知模板
     *
     * @param request request
     * @return id
     */
    Long createNotifyTemplate(NotifyTemplateCreateRequest request);

    /**
     * 更新通知模板
     *
     * @param request request
     * @return effect
     */
    Integer updateNotifyTemplateById(NotifyTemplateUpdateRequest request);

    /**
     * 查询通知模板
     *
     * @param id id
     * @return row
     */
    NotifyTemplateVO getNotifyTemplateById(Long id);

    /**
     * 通过缓存查询通知模板列表
     *
     * @param bizType bizType
     * @return rows
     */
    List<NotifyTemplateVO> getNotifyTemplateListByCache(String bizType);

    /**
     * 通过缓存查询通知模板详情
     *
     * @param id id
     * @return row
     */
    NotifyTemplateDetailCacheDTO getNotifyTemplateDetailByCache(Long id);

    /**
     * 分页查询通知模板
     *
     * @param request request
     * @return rows
     */
    DataGrid<NotifyTemplateVO> getNotifyTemplatePage(NotifyTemplateQueryRequest request);

    /**
     * 删除通知模板
     *
     * @param id id
     * @return effect
     */
    Integer deleteNotifyTemplateById(Long id);

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    LambdaQueryWrapper<NotifyTemplateDO> buildQueryWrapper(NotifyTemplateQueryRequest request);

}
