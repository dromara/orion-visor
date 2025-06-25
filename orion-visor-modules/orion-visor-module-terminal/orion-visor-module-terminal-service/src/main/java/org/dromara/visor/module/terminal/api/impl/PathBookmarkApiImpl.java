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
package org.dromara.visor.module.terminal.api.impl;

import org.dromara.visor.module.terminal.api.PathBookmarkApi;
import org.dromara.visor.module.terminal.service.PathBookmarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 路径标签 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/3 11:11
 */
@Slf4j
@Service
public class PathBookmarkApiImpl implements PathBookmarkApi {

    @Resource
    private PathBookmarkService pathBookmarkService;

    @Override
    public Integer deleteByUserIdList(List<Long> userIdList) {
        return pathBookmarkService.deleteByUserIdList(userIdList);
    }

}
