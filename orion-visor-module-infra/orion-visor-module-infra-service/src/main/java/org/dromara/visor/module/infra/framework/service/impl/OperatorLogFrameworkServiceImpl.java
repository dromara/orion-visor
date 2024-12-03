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
package org.dromara.visor.module.infra.framework.service.impl;

import org.dromara.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import org.dromara.visor.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import org.dromara.visor.module.infra.service.OperatorLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 操作日志包 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 16:53
 */
@Service
public class OperatorLogFrameworkServiceImpl implements OperatorLogFrameworkService {

    @Resource
    private OperatorLogService operatorLogService;

    @Override
    public void insert(OperatorLogModel log) {
        operatorLogService.addOperatorLog(log);
    }

}