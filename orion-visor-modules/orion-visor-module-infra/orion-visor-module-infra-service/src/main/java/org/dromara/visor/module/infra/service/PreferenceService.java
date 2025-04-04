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
package org.dromara.visor.module.infra.service;

import org.dromara.visor.module.infra.entity.request.preference.PreferenceUpdateBatchRequest;
import org.dromara.visor.module.infra.entity.request.preference.PreferenceUpdateRequest;
import org.dromara.visor.module.infra.enums.PreferenceTypeEnum;

import java.util.List;
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
     */
    void updatePreference(PreferenceUpdateRequest request);

    /**
     * 更新用户偏好-多个
     *
     * @param request request
     */
    void updatePreferenceBatch(PreferenceUpdateBatchRequest request);

    /**
     * 查询用户偏好
     *
     * @param type  type
     * @param items items
     * @param <T>   T
     * @return rows
     */
    <T> T getPreferenceByType(String type, List<String> items);

    /**
     * 查询默认偏好
     *
     * @param type  type
     * @param items items
     * @param <T>   T
     * @return rows
     */
    <T> T getDefaultPreferenceByType(String type, List<String> items);

    /**
     * 获取用户偏好
     *
     * @param userId userId
     * @param type   type
     * @param <T>    T
     * @return 偏好
     */
    <T> Future<T> getPreferenceAsync(Long userId, PreferenceTypeEnum type);

    /**
     * 删除用户偏好
     *
     * @param userId userId
     */
    void deletePreferenceByUserId(Long userId);

    /**
     * 删除用户偏好
     *
     * @param userIdList userIdList
     */
    void deletePreferenceByUserIdList(List<Long> userIdList);

}
