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
package org.dromara.visor.module.exec.define.cache;

import cn.orionsec.kit.lang.define.cache.key.CacheKeyBuilder;
import cn.orionsec.kit.lang.define.cache.key.CacheKeyDefine;
import cn.orionsec.kit.lang.define.cache.key.struct.RedisCacheStruct;
import org.dromara.visor.module.exec.entity.dto.ExecLogTailDTO;

import java.util.concurrent.TimeUnit;

/**
 * 命令执行服务缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 16:31
 */
public interface ExecCacheKeyDefine {

    CacheKeyDefine EXEC_TAIL = new CacheKeyBuilder()
            .key("exec:tail:{}")
            .desc("命令执行日志查看 ${token}")
            .type(ExecLogTailDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(5, TimeUnit.MINUTES)
            .build();

}
