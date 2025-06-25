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
package org.dromara.visor.module.asset.api.impl;

import cn.orionsec.kit.lang.utils.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.asset.api.HostApi;
import org.dromara.visor.module.asset.convert.HostProviderConvert;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.entity.dto.host.HostDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 主机 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/12 18:27
 */
@Slf4j
@Service
public class HostApiImpl implements HostApi {

    @Resource
    private HostDAO hostDAO;

    @Override
    public HostDTO selectById(Long id) {
        return HostProviderConvert.MAPPER.to(hostDAO.selectById(id));
    }

    @Override
    public List<HostDTO> selectByIdList(List<Long> idList) {
        if (Lists.isEmpty(idList)) {
            return Lists.empty();
        }
        return hostDAO.selectBatchIds(idList)
                .stream()
                .map(HostProviderConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

}
