package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.request.preference.PreferenceUpdatePartialRequest;
import com.orion.ops.module.infra.entity.request.preference.PreferenceUpdateRequest;
import com.orion.ops.module.infra.enums.PreferenceTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 用户偏好 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-27 18:37
 */
public interface PreferenceService {

    /**
     * 更新用户偏好-单个
     *
     * @param request request
     * @return effect
     */
    Integer updatePreference(PreferenceUpdateRequest request);

    /**
     * 更新用户偏好-部分
     *
     * @param request request
     */
    void updatePreferencePartial(PreferenceUpdatePartialRequest request);

    /**
     * 查询用户偏好
     *
     * @param type  type
     * @param items items
     * @return rows
     */
    Map<String, Object> getPreferenceByType(String type, List<String> items);

    /**
     * 查询默认偏好
     *
     * @param type  type
     * @param items items
     * @return rows
     */
    Map<String, Object> getDefaultPreferenceByType(String type, List<String> items);

    /**
     * 获取用户偏好
     *
     * @param userId userId
     * @param type   type
     * @return 偏好
     */
    Future<Map<String, Object>> getPreferenceAsync(Long userId, PreferenceTypeEnum type);

    /**
     * 删除用户偏好
     *
     * @param userId userId
     */
    void deletePreferenceByUserId(Long userId);

}
