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
package org.dromara.visor.framework.biz.operator.log.core.utils;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.collect.Maps;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import org.dromara.visor.framework.common.constant.ExtraFieldConst;
import org.dromara.visor.framework.common.security.LoginUser;

import java.util.Map;

/**
 * 操作日志工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 11:32
 */
public class OperatorLogs implements ExtraFieldConst {

    private static final String UN_SAVE_FLAG = "__un__save__";

    private static SerializeFilter[] serializeFilters;

    /**
     * 拓展信息
     */
    private static final ThreadLocal<Map<String, Object>> EXTRA_HOLDER = ThreadLocal.withInitial(Maps::newMap);

    /**
     * 当前用户 优先于登录用户
     */
    private static final ThreadLocal<LoginUser> USER_HOLDER = new ThreadLocal<>();

    private OperatorLogs() {
    }

    /**
     * 添加日志参数
     *
     * @param key   key
     * @param value value
     */
    public static void add(String key, Object value) {
        EXTRA_HOLDER.get().put(key, value);
    }

    /**
     * 添加日志参数 json
     *
     * @param key   key
     * @param value value
     */
    public static void addJson(String key, Object value) {
        EXTRA_HOLDER.get().put(key, JSON.parseObject(JSON.toJSONString(value, serializeFilters)));
    }

    /**
     * 添加日志参数
     *
     * @param map map
     */
    public static void add(Map<String, ?> map) {
        EXTRA_HOLDER.get().putAll(map);
    }

    /**
     * 添加日志参数
     *
     * @param obj obj
     */
    @SuppressWarnings("unchecked")
    public static void add(Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Map) {
            EXTRA_HOLDER.get().putAll((Map<String, ?>) obj);
        } else {
            EXTRA_HOLDER.get().putAll(JSON.parseObject(JSON.toJSONString(obj, serializeFilters)));
        }
    }

    /**
     * 设置不保存
     */
    public static void unSave() {
        setSave(false);
    }

    /**
     * 设置是否保存
     *
     * @param save save
     */
    public static void setSave(boolean save) {
        if (save) {
            EXTRA_HOLDER.get().remove(UN_SAVE_FLAG);
        } else {
            EXTRA_HOLDER.get().put(UN_SAVE_FLAG, UN_SAVE_FLAG);
        }
    }

    /**
     * 设置是否保存
     *
     * @return save
     */
    public static boolean isSave() {
        return !UN_SAVE_FLAG.equals(EXTRA_HOLDER.get().get(UN_SAVE_FLAG));
    }

    /**
     * 获取参数
     *
     * @return map
     */
    public static Map<String, Object> get() {
        return EXTRA_HOLDER.get();
    }

    /**
     * 设置用户信息
     *
     * @param user user
     */
    public static void setUser(LoginUser user) {
        USER_HOLDER.set(user);
    }

    /**
     * 获取用户
     *
     * @return user
     */
    public static LoginUser getUser() {
        return USER_HOLDER.get();
    }

    /**
     * 清空
     */
    public static void clear() {
        EXTRA_HOLDER.remove();
        USER_HOLDER.remove();
    }

    public static void setSerializeFilters(SerializeFilter[] serializeFilters) {
        if (OperatorLogs.serializeFilters != null) {
            // unmodified
            throw Exceptions.state();
        }
        OperatorLogs.serializeFilters = serializeFilters;
    }

}
