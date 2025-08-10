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
package org.dromara.visor.module.asset.handler.host.extra.model;

import lombok.*;
import org.dromara.visor.common.handler.data.model.GenericsDataModel;

import java.util.Date;
import java.util.List;

/**
 * 主机拓展信息 - 规格模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/24 0:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostSpecExtraModel implements GenericsDataModel {

    /**
     * sn
     */
    private String sn;

    /**
     * 系统名称
     */
    private String osName;

    /**
     * cpu 数量
     */
    private String cpuCount;

    /**
     * cpu 核心数
     */
    private Integer cpuPhysicalCore;

    /**
     * cpu 线程数
     */
    private Integer cpuLogicalCore;

    /**
     * cpu 频率
     */
    private Double cpuFrequency;

    /**
     * cpu 型号
     */
    private String cpuModel;

    /**
     * 内存大小
     */
    private Double memorySize;

    /**
     * 磁盘大小
     */
    private Double diskSize;

    /**
     * 上行带宽
     */
    private Integer inBandwidth;

    /**
     * 下行带宽
     */
    private Integer outBandwidth;

    /**
     * 公网 ip 列表
     */
    private List<String> publicIpAddresses;

    /**
     * 内网 ip 列表
     */
    private List<String> privateIpAddresses;

    /**
     * 负责人
     */
    private String chargePerson;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 到期时间
     */
    private Date expiredTime;

    /**
     * 扩展信息
     */
    @Singular
    private List<HostSpecExtraItem> items;

    /**
     * 扩展信息项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HostSpecExtraItem {

        /**
         * 标签
         */
        private String label;

        /**
         * 键
         */
        private String key;

        /**
         * 值
         */
        private String value;

    }

}
