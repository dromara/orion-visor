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
package org.dromara.visor.module.monitor.constant;

/**
 * 指标常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/15 17:23
 */
public interface MetricsConst {

    String CPU_USER_SECONDS_TOTAL = "cpu_user_seconds_total";
    String CPU_SYSTEM_SECONDS_TOTAL = "cpu_system_seconds_total";
    String CPU_TOTAL_SECONDS_TOTAL = "cpu_total_seconds_total";

    String MEM_USED_BYTES_TOTAL = "mem_used_bytes_total";
    String MEM_USED_PERCENT = "mem_used_percent";
    String MEM_SWAP_USED_BYTES_TOTAL = "mem_swap_used_bytes_total";
    String MEM_SWAP_USED_PERCENT = "mem_swap_used_percent";

    String LOAD1 = "load1";
    String LOAD5 = "load5";
    String LOAD15 = "load15";
    String LOAD1_CORE_RATIO = "load1_core_ratio";
    String LOAD5_CORE_RATIO = "load5_core_ratio";
    String LOAD15_CORE_RATIO = "load15_core_ratio";

    String DISK_FS_USED_BYTES_TOTAL = "disk_fs_used_bytes_total";
    String DISK_FS_USED_PERCENT = "disk_fs_used_percent";
    String DISK_FS_INODES_USED_PERCENT = "disk_fs_inodes_used_percent";

    String DISK_IO_READ_BYTES_TOTAL = "disk_io_read_bytes_total";
    String DISK_IO_WRITE_BYTES_TOTAL = "disk_io_write_bytes_total";
    String DISK_IO_READS_TOTAL = "disk_io_reads_total";
    String DISK_IO_WRITES_TOTAL = "disk_io_writes_total";
    String DISK_IO_READ_BYTES_PER_SECOND = "disk_io_read_bytes_per_second";
    String DISK_IO_WRITE_BYTES_PER_SECOND = "disk_io_write_bytes_per_second";
    String DISK_IO_READS_PER_SECOND = "disk_io_reads_per_second";
    String DISK_IO_WRITES_PER_SECOND = "disk_io_writes_per_second";

    String NET_SENT_BYTES_TOTAL = "net_sent_bytes_total";
    String NET_RECV_BYTES_TOTAL = "net_recv_bytes_total";
    String NET_SENT_PACKETS_TOTAL = "net_sent_packets_total";
    String NET_RECV_PACKETS_TOTAL = "net_recv_packets_total";
    String NET_SENT_BYTES_PER_SECOND = "net_sent_bytes_per_second";
    String NET_RECV_BYTES_PER_SECOND = "net_recv_bytes_per_second";
    String NET_SENT_PACKETS_PER_SECOND = "net_sent_packets_per_second";
    String NET_RECV_PACKETS_PER_SECOND = "net_recv_packets_per_second";

    String NET_TCP_CONNECTIONS = "net_tcp_connections";
    String NET_UDP_CONNECTIONS = "net_udp_connections";
    String NET_INET_CONNECTIONS = "net_inet_connections";
    String NET_ALL_CONNECTIONS = "net_all_connections";

}
