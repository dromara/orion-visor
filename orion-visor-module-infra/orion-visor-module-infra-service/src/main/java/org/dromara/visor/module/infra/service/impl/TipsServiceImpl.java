/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.utils.collect.Lists;
import org.dromara.visor.framework.redis.core.utils.RedisLists;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.infra.define.cache.TipsCacheKeyDefine;
import org.dromara.visor.module.infra.service.TipsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提示 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 17:57
 */
@Service
public class TipsServiceImpl implements TipsService {

    @Override
    public void tipped(String tippedKey) {
        Long userId = SecurityUtils.getLoginUserId();
        String key = TipsCacheKeyDefine.TIPS.format(userId);
        RedisLists.push(key, TipsCacheKeyDefine.TIPS, tippedKey);
    }

    @Override
    public List<String> getTippedKeys() {
        return Lists.def(RedisLists.range(TipsCacheKeyDefine.TIPS.format(SecurityUtils.getLoginUserId())));
    }

}
