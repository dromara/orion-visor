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
package org.dromara.visor.module.infra.define.cache;

import cn.orionsec.kit.lang.define.cache.key.CacheKeyBuilder;
import cn.orionsec.kit.lang.define.cache.key.CacheKeyDefine;
import cn.orionsec.kit.lang.define.cache.key.struct.RedisCacheStruct;
import org.dromara.visor.common.security.LoginUser;
import org.dromara.visor.module.infra.entity.dto.LoginTokenDTO;
import org.dromara.visor.module.infra.entity.dto.UserInfoDTO;

import java.util.concurrent.TimeUnit;

/**
 * 用户模块缓存 key
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/13 21:54
 */
public interface UserCacheKeyDefine {

    CacheKeyDefine USER_INFO = new CacheKeyBuilder()
            .key("user:info:{}")
            .desc("用户信息 ${id}")
            .type(LoginUser.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine USER_LIST = new CacheKeyBuilder()
            .key("user:base:list")
            .desc("用户列表")
            .type(UserInfoDTO.class)
            .struct(RedisCacheStruct.HASH)
            .timeout(8, TimeUnit.HOURS)
            .build();

    CacheKeyDefine LOGIN_FAILED_COUNT = new CacheKeyBuilder()
            .key("user:login-failed:{}")
            .desc("用户登录失败次数 ${username}")
            .noPrefix()
            .type(Integer.class)
            .struct(RedisCacheStruct.STRING)
            .build();

    CacheKeyDefine LOGIN_TOKEN = new CacheKeyBuilder()
            .key("user:token:{}:{}")
            .desc("用户登录 token ${id} ${time}")
            .type(LoginTokenDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(24 * 60, TimeUnit.MINUTES)
            .build();

    CacheKeyDefine LOGIN_REFRESH = new CacheKeyBuilder()
            .key("user:refresh:{}:{}")
            .desc("用户刷新 token ${id} ${time}")
            .type(LoginTokenDTO.class)
            .struct(RedisCacheStruct.STRING)
            .timeout(32 * 60, TimeUnit.MINUTES)
            .build();

}
