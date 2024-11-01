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
package org.dromara.visor.module.asset.service.impl;

import cn.orionsec.kit.lang.function.Functions;
import cn.orionsec.kit.lang.utils.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.asset.dao.ExecTemplateHostDAO;
import org.dromara.visor.module.asset.entity.domain.ExecTemplateHostDO;
import org.dromara.visor.module.asset.service.ExecTemplateHostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 执行模板主机 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-22 12:13
 */
@Slf4j
@Service
public class ExecTemplateHostServiceImpl implements ExecTemplateHostService {

    @Resource
    private ExecTemplateHostDAO execTemplateHostDAO;

    @Override
    public Set<Long> getHostByTemplateId(Long templateId) {
        return execTemplateHostDAO.of()
                .createWrapper()
                .select(ExecTemplateHostDO::getHostId)
                .eq(ExecTemplateHostDO::getTemplateId, templateId)
                .then()
                .stream()
                .map(ExecTemplateHostDO::getHostId)
                .collect(Collectors.toSet());
    }

    @Override
    public Map<Long, Set<Long>> getHostByTemplateIdList(List<Long> templateIdList) {
        return execTemplateHostDAO.of()
                .createWrapper()
                .select(ExecTemplateHostDO::getTemplateId, ExecTemplateHostDO::getHostId)
                .in(ExecTemplateHostDO::getTemplateId, templateIdList)
                .then()
                .stream()
                .collect(Collectors.groupingBy(
                        ExecTemplateHostDO::getTemplateId,
                        Collectors.mapping(ExecTemplateHostDO::getHostId, Collectors.toSet())
                ));
    }

    @Override
    public void setTemplateHost(Long templateId, List<Long> hostList) {
        LambdaQueryWrapper<ExecTemplateHostDO> wrapper = execTemplateHostDAO.wrapper()
                .eq(ExecTemplateHostDO::getTemplateId, templateId);
        if (Lists.isEmpty(hostList)) {
            // 为空移除
            execTemplateHostDAO.delete(wrapper);
        } else {
            // 查询主机
            wrapper.select(ExecTemplateHostDO::getId, ExecTemplateHostDO::getHostId);
            Map<Long, Long> hostMap = execTemplateHostDAO.of(wrapper)
                    .stream()
                    .collect(Collectors.toMap(ExecTemplateHostDO::getHostId,
                            ExecTemplateHostDO::getId,
                            Functions.right()));
            // 插入主机
            List<ExecTemplateHostDO> insertRecords = hostList.stream()
                    .filter(s -> !hostMap.containsKey(s))
                    .map(s -> ExecTemplateHostDO.builder()
                            .templateId(templateId)
                            .hostId(s)
                            .build())
                    .collect(Collectors.toList());
            if (!insertRecords.isEmpty()) {
                execTemplateHostDAO.insertBatch(insertRecords);
            }
            // 删除主机
            List<Long> deleteIdList = hostMap.keySet()
                    .stream()
                    .filter(s -> !hostList.contains(s))
                    .map(hostMap::get)
                    .collect(Collectors.toList());
            if (!deleteIdList.isEmpty()) {
                execTemplateHostDAO.deleteBatchIds(deleteIdList);
            }
        }
    }

    @Override
    public Integer deleteByTemplateId(Long templateId) {
        LambdaQueryWrapper<ExecTemplateHostDO> wrapper = execTemplateHostDAO.lambda()
                .eq(ExecTemplateHostDO::getTemplateId, templateId);
        log.info("ExecTemplateHostService-deleteByTemplateId id: {}", templateId);
        int effect = execTemplateHostDAO.delete(wrapper);
        log.info("ExecTemplateHostService-deleteByTemplateId effect: {}", effect);
        return effect;
    }

    @Override
    public Integer deleteByTemplateIdList(List<Long> templateIdList) {
        LambdaQueryWrapper<ExecTemplateHostDO> wrapper = execTemplateHostDAO.lambda()
                .in(ExecTemplateHostDO::getTemplateId, templateIdList);
        log.info("ExecTemplateHostService-deleteByTemplateIdList idList: {}", templateIdList);
        int effect = execTemplateHostDAO.delete(wrapper);
        log.info("ExecTemplateHostService-deleteByTemplateIdList effect: {}", effect);
        return effect;
    }

    @Override
    public Integer deleteByHostId(Long hostId) {
        LambdaQueryWrapper<ExecTemplateHostDO> wrapper = execTemplateHostDAO.lambda()
                .eq(ExecTemplateHostDO::getHostId, hostId);
        log.info("ExecTemplateHostService-deleteByHostId id: {}", hostId);
        int effect = execTemplateHostDAO.delete(wrapper);
        log.info("ExecTemplateHostService-deleteByHostId effect: {}", effect);
        return effect;
    }

    @Override
    public Integer deleteByHostIdList(List<Long> hostIdList) {
        LambdaQueryWrapper<ExecTemplateHostDO> wrapper = execTemplateHostDAO.lambda()
                .in(ExecTemplateHostDO::getHostId, hostIdList);
        log.info("ExecTemplateHostService-deleteByHostIdList id: {}", hostIdList);
        int effect = execTemplateHostDAO.delete(wrapper);
        log.info("ExecTemplateHostService-deleteByHostIdList effect: {}", effect);
        return effect;
    }

}
