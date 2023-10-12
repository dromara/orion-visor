package com.orion.ops.framework.biz.operator.log.core.uitls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.orion.ops.framework.biz.operator.log.core.constant.OperatorLogKeys;
import com.orion.ops.framework.common.security.LoginUser;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 11:32
 */
public class OperatorLogs implements OperatorLogKeys {

    private static final String UN_SAVE_FLAG = "__un__save__";

    private static SerializeFilter[] serializeFilters;

    /**
     * 拓展信息
     */
    private static final ThreadLocal<Map<String, Object>> EXTRA_HOLDER = new ThreadLocal<>();

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
        initMap().put(key, value);
    }

    /**
     * 添加日志参数 json
     *
     * @param key   key
     * @param value value
     */
    public static void addJson(String key, Object value) {
        initMap().put(key, JSON.parseObject(JSON.toJSONString(value, serializeFilters)));
    }

    /**
     * 添加日志参数
     *
     * @param map map
     */
    public static void add(Map<String, ?> map) {
        initMap().putAll(map);
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
            add((Map<String, ?>) obj);
            return;
        }
        initMap().putAll(JSON.parseObject(JSON.toJSONString(obj, serializeFilters)));
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
        if (!save) {
            initMap().put(UN_SAVE_FLAG, UN_SAVE_FLAG);
        }
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
     * 清空
     */
    public static void clear() {
        EXTRA_HOLDER.remove();
        USER_HOLDER.remove();
    }

    /**
     * 设置是否保存
     *
     * @param map map
     * @return save
     */
    public static boolean isSave(Map<String, Object> map) {
        return map == null || !map.containsKey(UN_SAVE_FLAG);
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
     * 初始化
     *
     * @return map
     */
    private static Map<String, Object> initMap() {
        Map<String, Object> map = EXTRA_HOLDER.get();
        if (map == null) {
            map = new HashMap<>();
            EXTRA_HOLDER.set(map);
        }
        return map;
    }

    public static void setSerializeFilters(SerializeFilter[] serializeFilters) {
        OperatorLogs.serializeFilters = serializeFilters;
    }

}
