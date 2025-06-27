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
package org.dromara.visor.module.exec.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.exec.api.ExecTemplateApi;
import org.dromara.visor.module.exec.dao.ExecTemplateHostDAO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 执行模板对外服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/14 13:10
 */
@Slf4j
@Service
public class ExecTemplateApiImpl implements ExecTemplateApi {

    @Resource
    private ExecTemplateHostDAO execTemplateHostDAO;

    @Override
    public Integer deleteByHostIdList(List<Long> hostIdList) {
        return execTemplateHostDAO.deleteByHostIdList(hostIdList);
    }

}
