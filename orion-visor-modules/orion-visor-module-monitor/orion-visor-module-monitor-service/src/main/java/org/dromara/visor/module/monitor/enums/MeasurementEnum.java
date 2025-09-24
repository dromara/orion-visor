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
package org.dromara.visor.module.monitor.enums;

import lombok.Getter;
import org.apache.commons.collections4.map.HashedMap;
import org.dromara.visor.module.monitor.constant.MetricsConst;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 指标度量类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/14 10:27
 */
@Getter
public enum MeasurementEnum {

    /**
     * cpu
     */
    CPU("cpu", (s) -> {
        s.accept(MetricsConst.CPU_USER_SECONDS_TOTAL, double.class);
        s.accept(MetricsConst.CPU_SYSTEM_SECONDS_TOTAL, double.class);
        s.accept(MetricsConst.CPU_TOTAL_SECONDS_TOTAL, double.class);
    }),

    /**
     * 内存
     */
    MEMORY("memory", s -> {
        s.accept(MetricsConst.MEM_USED_BYTES_TOTAL, long.class);
        s.accept(MetricsConst.MEM_USED_PERCENT, double.class);
        s.accept(MetricsConst.MEM_SWAP_USED_BYTES_TOTAL, long.class);
        s.accept(MetricsConst.MEM_SWAP_USED_PERCENT, double.class);
    }),

    /**
     * 负载
     */
    LOAD("load", s -> {
        s.accept(MetricsConst.LOAD1, double.class);
        s.accept(MetricsConst.LOAD5, double.class);
        s.accept(MetricsConst.LOAD15, double.class);
        s.accept(MetricsConst.LOAD1_CORE_RATIO, double.class);
        s.accept(MetricsConst.LOAD5_CORE_RATIO, double.class);
        s.accept(MetricsConst.LOAD15_CORE_RATIO, double.class);
    }),

    /**
     * 磁盘
     */
    DISK("disk", s -> {
        s.accept(MetricsConst.DISK_FS_USED_BYTES_TOTAL, long.class);
        s.accept(MetricsConst.DISK_FS_USED_PERCENT, double.class);
        s.accept(MetricsConst.DISK_FS_INODES_USED_PERCENT, double.class);
    }),

    /**
     * io
     */
    IO("io", s -> {
        s.accept(MetricsConst.DISK_IO_READ_BYTES_TOTAL, long.class);
        s.accept(MetricsConst.DISK_IO_WRITE_BYTES_TOTAL, long.class);
        s.accept(MetricsConst.DISK_IO_READS_TOTAL, long.class);
        s.accept(MetricsConst.DISK_IO_WRITES_TOTAL, long.class);
        s.accept(MetricsConst.DISK_IO_READ_BYTES_PER_SECOND, double.class);
        s.accept(MetricsConst.DISK_IO_WRITE_BYTES_PER_SECOND, double.class);
        s.accept(MetricsConst.DISK_IO_READS_PER_SECOND, double.class);
        s.accept(MetricsConst.DISK_IO_WRITES_PER_SECOND, double.class);
    }),

    /**
     * 网络
     */
    NETWORK("network", s -> {
        s.accept(MetricsConst.NET_SENT_BYTES_TOTAL, long.class);
        s.accept(MetricsConst.NET_RECV_BYTES_TOTAL, long.class);
        s.accept(MetricsConst.NET_SENT_PACKETS_TOTAL, long.class);
        s.accept(MetricsConst.NET_RECV_PACKETS_TOTAL, long.class);
        s.accept(MetricsConst.NET_SENT_BYTES_PER_SECOND, double.class);
        s.accept(MetricsConst.NET_RECV_BYTES_PER_SECOND, double.class);
        s.accept(MetricsConst.NET_SENT_PACKETS_PER_SECOND, double.class);
        s.accept(MetricsConst.NET_RECV_PACKETS_PER_SECOND, double.class);
    }),

    /**
     * 连接数
     */
    CONNECTIONS("connections", s -> {
        s.accept(MetricsConst.NET_TCP_CONNECTIONS, int.class);
        s.accept(MetricsConst.NET_UDP_CONNECTIONS, int.class);
        s.accept(MetricsConst.NET_INET_CONNECTIONS, int.class);
        s.accept(MetricsConst.NET_ALL_CONNECTIONS, int.class);
    }),

    ;

    private final String measurement;
    private final Map<String, Class<?>> fields;

    MeasurementEnum(String measurement, Consumer<BiConsumer<String, Class<?>>> register) {
        this.measurement = measurement;
        this.fields = new HashedMap<>();
        register.accept(this.fields::put);
    }

    public static MeasurementEnum of(String measurement) {
        if (measurement == null) {
            return null;
        }
        for (MeasurementEnum e : values()) {
            if (e.measurement.equals(measurement)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 获取度量值类型
     *
     * @param measurement measurement
     * @param field       field
     * @return type
     */
    public static Class<?> getMetricsValueType(String measurement, String field) {
        MeasurementEnum m = of(measurement);
        if (m == null) {
            return null;
        }
        return m.getFields().get(field);
    }

}
