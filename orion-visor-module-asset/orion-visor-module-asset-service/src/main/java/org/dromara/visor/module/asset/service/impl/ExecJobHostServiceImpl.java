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
package org.dromara.visor.module.asset.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.asset.dao.ExecJobHostDAO;
import org.dromara.visor.module.asset.entity.domain.ExecJobHostDO;
import org.dromara.visor.module.asset.service.ExecJobHostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 计划任务主机 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Slf4j
@Service
public class ExecJobHostServiceImpl implements ExecJobHostService {

    @Resource
    private ExecJobHostDAO execJobHostDAO;

    @Override
    public void setHostIdByJobId(Long jobId, List<Long> hostIdList) {
        log.info("ExecJobHostService.setHostIdByJobId jobId: {}, hostIdList: {}", jobId, hostIdList);
        // 删除
        execJobHostDAO.deleteByJobId(jobId);
        // 重新插入
        List<ExecJobHostDO> rows = hostIdList.stream()
                .map(s -> ExecJobHostDO.builder()
                        .hostId(s)
                        .jobId(jobId)
                        .build())
                .collect(Collectors.toList());
        execJobHostDAO.insertBatch(rows);
    }

    @Override
    public List<Long> getHostIdByJobId(Long jobId) {
        return execJobHostDAO.selectHostIdByJobId(jobId);
    }

    @Override
    public Integer deleteByJobId(Long jobId) {
        return execJobHostDAO.deleteByJobId(jobId);
    }

    @Override
    public Integer deleteByJobIdList(List<Long> jobIdList) {
        return execJobHostDAO.deleteByJobIdList(jobIdList);
    }

    @Override
    public Integer deleteByHostId(Long hostId) {
        return execJobHostDAO.deleteByHostId(hostId);
    }

    @Override
    public Integer deleteByHostIdList(List<Long> hostIdList) {
        return execJobHostDAO.deleteByHostIdList(hostIdList);
    }

}
