package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.request.preference.PreferenceUpdateRequest;
import com.orion.ops.module.infra.entity.vo.PreferenceVO;
import com.orion.ops.module.infra.enums.PreferenceTypeEnum;

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
     * 更新用户偏好
     *
     * @param request request
     * @param partial 是否为部分更新
     * @return effect
     */
    Integer updatePreference(PreferenceUpdateRequest request, boolean partial);

    /**
     * 查询用户偏好
     *
     * @param type type
     * @return row
     */
    PreferenceVO getPreferenceByType(String type);

    /**
     * 获取用户偏好
     *
     * @param userId userId
     * @param type   type
     * @return 偏好
     */
    Future<Map<String, Object>> getPreference(Long userId, PreferenceTypeEnum type);

    /**
     * 删除用户偏好
     *
     * @param userId userId
     */
    void deletePreferenceByUserId(Long userId);

}
