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
package org.dromara.visor.module.infra.api.impl;

import cn.orionsec.kit.lang.utils.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.infra.api.NotifyTemplateApi;
import org.dromara.visor.module.infra.convert.NotifyTemplateProviderConvert;
import org.dromara.visor.module.infra.dao.NotifyTemplateDAO;
import org.dromara.visor.module.infra.entity.domain.NotifyTemplateDO;
import org.dromara.visor.module.infra.entity.dto.notify.NotifyTemplateDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通知模板 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-13 21:05
 */
@Slf4j
@Service
public class NotifyTemplateApiImpl implements NotifyTemplateApi {

    @Resource
    private NotifyTemplateDAO notifyTemplateDAO;

    @Override
    public List<NotifyTemplateDTO> getNotifyTemplateByIdList(List<Long> idList) {
        log.info("NotifyTemplateApi.getNotifyTemplateByIdList idList: {}", idList);
        if (Lists.isEmpty(idList)) {
            return Lists.empty();
        }
        // 查询
        List<NotifyTemplateDO> rows = notifyTemplateDAO.selectBatchIds(idList);
        // 转换
        return NotifyTemplateProviderConvert.MAPPER.toList(rows);
    }

}
