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
package com.orion.visor.module.asset.define.cache;

import com.orion.lang.define.cache.key.CacheKeyBuilder;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.visor.module.asset.entity.dto.SftpGetContentCacheDTO;
import com.orion.visor.module.asset.entity.dto.SftpSetContentCacheDTO;
import com.orion.visor.module.asset.entity.dto.TerminalAccessDTO;
import com.orion.visor.module.asset.entity.dto.TerminalTransferDTO;

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
