/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.asset.define.cache;

import cn.orionsec.kit.lang.define.cache.key.CacheKeyBuilder;
import cn.orionsec.kit.lang.define.cache.key.CacheKeyDefine;
import cn.orionsec.kit.lang.define.cache.key.struct.RedisCacheStruct;
import org.dromara.visor.module.asset.entity.dto.SftpGetContentCacheDTO;
import org.dromara.visor.module.asset.entity.dto.SftpSetContentCacheDTO;
import org.dromara.visor.module.asset.entity.dto.TerminalAccessDTO;
import org.dromara.visor.module.asset.entity.dto.TerminalTransferDTO;

import java.util.concurrent.TimeUnit;

/**
 * 终端服务缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/27 18:13
 */
public interface TerminalCacheKeyDefine {

    CacheKeyDefine TERMINAL_ACCESS = new CacheKeyBuilder()
            .key("terminal:access:{}")
            .desc("终端访问token ${token}")
            .type(TerminalAccessDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(3, TimeUnit.MINUTES)
            .build();

    CacheKeyDefine TERMINAL_TRANSFER = new CacheKeyBuilder()
            .key("terminal:transfer:{}")
            .desc("终端传输token ${token}")
            .type(TerminalTransferDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(3, TimeUnit.MINUTES)
            .build();

    CacheKeyDefine TERMINAL_SFTP_GET_CONTENT = new CacheKeyBuilder()
            .key("terminal:sftp:gc:{}")
            .desc("sftp 获取文件内容 ${token}")
            .type(SftpGetContentCacheDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(5, TimeUnit.MINUTES)
            .build();

    CacheKeyDefine TERMINAL_SFTP_SET_CONTENT = new CacheKeyBuilder()
            .key("terminal:sftp:sc:{}")
            .desc("sftp 设置文件内容 ${token}")
            .type(SftpSetContentCacheDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(5, TimeUnit.MINUTES)
            .build();

}
