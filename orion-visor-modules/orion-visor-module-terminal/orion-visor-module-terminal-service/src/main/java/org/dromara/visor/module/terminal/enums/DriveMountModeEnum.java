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
package org.dromara.visor.module.terminal.enums;

import cn.orionsec.kit.lang.utils.time.Dates;
import lombok.AllArgsConstructor;

/**
 * 驱动挂载模式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/6/29 1:32
 */
@AllArgsConstructor
public enum DriveMountModeEnum {

    /**
     * 完全共享
     */
    SHARED("S") {
        @Override
        public String getDriveMountPath(Long userId, Long assetId, String sessionId) {
            return this.buildDriveMountPath(DEFAULT_S, userId, DEFAULT_N, DEFAULT_S);
        }
    },

    /**
     * 会话维度
     */
    SESSION("SE") {
        @Override
        public String getDriveMountPath(Long userId, Long assetId, String sessionId) {
            return this.buildDriveMountPath(Dates.current(Dates.YMD2), userId, assetId, sessionId);
        }
    },

    /**
     * 资产维度
     */
    ASSET("A") {
        @Override
        public String getDriveMountPath(Long userId, Long assetId, String sessionId) {
            return this.buildDriveMountPath(DEFAULT_S, userId, assetId, DEFAULT_S);
        }
    },

    /**
     * 天维度
     */
    DAY("D") {
        @Override
        public String getDriveMountPath(Long userId, Long assetId, String sessionId) {
            return this.buildDriveMountPath(Dates.current(Dates.YMD2), userId, DEFAULT_N, DEFAULT_S);
        }
    },

    /**
     * 天维度 + 资产维度
     */
    DAY_ASSET("DA") {
        @Override
        public String getDriveMountPath(Long userId, Long assetId, String sessionId) {
            return this.buildDriveMountPath(Dates.current(Dates.YMD2), userId, DEFAULT_N, DEFAULT_S);
        }
    },

    /**
     * 月维度
     */
    MONTH("M") {
        @Override
        public String getDriveMountPath(Long userId, Long assetId, String sessionId) {
            String date = Dates.stream()
                    .setDay(1)
                    .format(Dates.YMD2);
            return this.buildDriveMountPath(date, userId, DEFAULT_N, DEFAULT_S);
        }
    },

    /**
     * 月维度 + 资产维度
     */
    MONTH_ASSET("MA") {
        @Override
        public String getDriveMountPath(Long userId, Long assetId, String sessionId) {
            String date = Dates.stream()
                    .setDay(1)
                    .format(Dates.YMD2);
            return this.buildDriveMountPath(date, userId, assetId, DEFAULT_S);
        }
    },

    ;

    private static final Long DEFAULT_N = 0L;

    private static final String DEFAULT_S = "0";

    private final String prefix;

    /**
     * 获取驱动挂载路径
     *
     * @param userId    userId
     * @param assetId   assetId
     * @param sessionId sessionId
     * @return path
     */
    public abstract String getDriveMountPath(Long userId, Long assetId, String sessionId);

    /**
     * 构建驱动挂载路径
     *
     * @param time      time
     * @param userId    userId
     * @param assetId   assetId
     * @param sessionId sessionId
     * @return path
     */
    protected String buildDriveMountPath(String time, Long userId, Long assetId, String sessionId) {
        return prefix + "_"
                + time + "_"
                + userId + "_"
                + assetId + "_"
                + sessionId;
    }

    public static DriveMountModeEnum of(String mode) {
        if (mode == null) {
            return ASSET;
        }
        for (DriveMountModeEnum value : values()) {
            if (value.name().equalsIgnoreCase(mode)) {
                return value;
            }
        }
        return ASSET;
    }

}
