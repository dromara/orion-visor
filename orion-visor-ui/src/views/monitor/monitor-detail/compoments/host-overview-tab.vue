<template>
  <div class="host-overview">
    <a-row :gutter="[24, 24]" align="stretch">
      <!-- 主机信息 -->
      <a-col :span="8">
        <a-card v-if="host && host.spec"
                class="host-info-card"
                size="small"
                :bordered="false"
                :header-style="{ height: '48px', borderBottom: 'none' }"
                :body-style="{ padding: '0', height: '328px' }">
          <template #title>
            <h3>主机信息</h3>
          </template>
          <div class="host-info-content">
            <a-descriptions :column="1"
                            :label-style="{ width: '100px' }"
                            :value-style="{ fontWeight: '600' }">
              <a-descriptions-item label="SN">{{ host.spec?.sn || '-' }}</a-descriptions-item>
              <a-descriptions-item label="系统名称">{{ host.spec?.osName || '-' }}</a-descriptions-item>
              <a-descriptions-item label="系统类型">{{ host.osType }} - {{ host.archType }}</a-descriptions-item>
              <a-descriptions-item label="CPU型号">{{ host.spec?.cpuModel || '-' }}</a-descriptions-item>
              <a-descriptions-item label="CPU核心">
                {{ host.spec?.cpuPhysicalCore ? `${host.spec.cpuPhysicalCore} 核` : '-' }}
                {{ host.spec?.cpuLogicalCore ? `${host.spec.cpuLogicalCore} 线程` : '-' }}
              </a-descriptions-item>
              <a-descriptions-item label="CPU频率">{{ host.spec?.cpuFrequency ? `${host.spec.cpuFrequency} GHz` : '-' }}</a-descriptions-item>
              <a-descriptions-item label="内存大小">{{ host.spec?.memorySize ? `${host.spec.memorySize} GB` : '-' }}</a-descriptions-item>
              <a-descriptions-item label="磁盘大小">{{ host.spec?.diskSize ? `${host.spec.diskSize} GB` : '-' }}</a-descriptions-item>
              <a-descriptions-item label="网络带宽">
                {{ host.spec?.inBandwidth ? `${host.spec.inBandwidth} Mbps` : '-' }}
                / {{ host.spec?.outBandwidth ? `${host.spec.outBandwidth} Mbps` : '-' }}
              </a-descriptions-item>
            </a-descriptions>
          </div>
        </a-card>
      </a-col>
      <!-- 第一层加载中 -->
      <a-col v-if="renderLoading" :span="16">
        <a-card class="metric-card"
                size="small"
                :bordered="false"
                style="height: 376px;"
                :header-style="{ height: '48px', borderBottom: 'none' }"
                :body-style="{ padding: '24px' }">
          <a-skeleton :animation="true">
            <a-skeleton-line :rows="5"
                             :line-height="56"
                             :line-spacing="12" />
          </a-skeleton>
        </a-card>
      </a-col>
      <!-- 第一层无数据 -->
      <a-col v-else-if="nodata" :span="16">
        <a-card class="metric-card"
                size="small"
                :bordered="false"
                style="height: 376px;"
                :header-style="{ height: '48px', borderBottom: 'none' }"
                :body-style="{ padding: '24px' }">
          <a-empty style="margin-top: 88px;" />
        </a-card>
      </a-col>
      <!-- 第一层数据 -->
      <a-col v-else :span="16">
        <a-row :gutter="[24, 24]">
          <!-- cpu -->
          <a-col :span="12">
            <a-card class="metric-card"
                    size="small"
                    :bordered="false"
                    :header-style="{ height: '48px', borderBottom: 'none' }"
                    :body-style="{ height: 'calc(100% - 48px)' }">
              <template #title>
                <h3>CPU</h3>
              </template>
              <div class="card-content">
                <a-statistic title="总计"
                             :value="cpuMetrics.total"
                             :precision="2"
                             :value-style="{ color: getPercentProgressColor(cpuMetrics.total / 100, '') }">
                  <template #suffix>%</template>
                </a-statistic>
                <a-statistic title="用户态"
                             :value="cpuMetrics.user"
                             :precision="2"
                             :value-style="{ color: getPercentProgressColor(cpuMetrics.user / 100, '') }">
                  <template #suffix>%</template>
                </a-statistic>
                <a-statistic title="内核态"
                             :value="cpuMetrics.system"
                             :precision="2"
                             :value-style="{ color: getPercentProgressColor(cpuMetrics.system / 100, '') }">
                  <template #suffix>%</template>
                </a-statistic>
              </div>
            </a-card>
          </a-col>
          <!-- 内存 -->
          <a-col :span="12">
            <a-card class="metric-card"
                    size="small"
                    :bordered="false"
                    :header-style="{ height: '48px', borderBottom: 'none' }"
                    :body-style="{ height: 'calc(100% - 48px)' }">
              <template #title>
                <h3>内存</h3>
                <a-space>
                  <a-tag color="arcoblue">total: {{ memoryMetrics.total }}</a-tag>
                  <a-tag color="purple">swap: {{ memoryMetrics.swapTotal }}</a-tag>
                </a-space>
              </template>
              <div class="card-content">
                <a-statistic title="已使用"
                             :value="memoryMetrics.used"
                             :precision="2"
                             :value-style="{ color: getPercentProgressColor(memoryMetrics.usedPercent / 100, '') }">
                  <template #suffix>{{ memoryMetrics.usedUnit }}<span style="margin: 0 4px;" />{{ memoryMetrics.usedPercent.toFixed(2) }}%</template>
                </a-statistic>
                <a-statistic title="交换分区"
                             :value="memoryMetrics.swapUsed"
                             :precision="2"
                             :value-style="{ color: getPercentProgressColor(memoryMetrics.swapUsedPercent, '') }">
                  <template #suffix>{{ memoryMetrics.swapUsedUnit }}<span style="margin: 0 4px;" />{{ memoryMetrics.swapUsedPercent.toFixed(2) }}%</template>
                </a-statistic>
              </div>
            </a-card>
          </a-col>
          <!-- 负载 -->
          <a-col :span="12">
            <a-card class="metric-card"
                    size="small"
                    :bordered="false"
                    :header-style="{ height: '48px', borderBottom: 'none' }"
                    :body-style="{ height: 'calc(100% - 48px)' }">
              <template #title>
                <h3>负载</h3>
              </template>
              <div class="card-content">
                <a-statistic title="1分钟"
                             :value="loadMetrics.load1"
                             :precision="2">
                </a-statistic>
                <a-statistic title="5分钟"
                             :value="loadMetrics.load5"
                             :precision="2">
                </a-statistic>
                <a-statistic title="15分钟"
                             :value="loadMetrics.load15"
                             :precision="2">
                </a-statistic>
              </div>
            </a-card>
          </a-col>
          <!-- 连接数 -->
          <a-col :span="12">
            <a-card class="metric-card"
                    size="small"
                    :bordered="false"
                    :header-style="{ height: '48px', borderBottom: 'none' }"
                    :body-style="{ height: 'calc(100% - 48px)' }">
              <template #title>
                <div class="card-title">
                  <h3>连接数</h3>
                </div>
              </template>
              <div class="card-content">
                <a-statistic title="TCP" :value="connectionsMetrics.tcp" />
                <a-statistic title="UDP" :value="connectionsMetrics.udp" />
                <a-statistic title="总计" :value="connectionsMetrics.tcp + connectionsMetrics.udp" />
              </div>
            </a-card>
          </a-col>
        </a-row>
      </a-col>
      <!-- 第二层数据 -->
      <template v-if="!renderLoading && !nodata">
        <!-- 磁盘 -->
        <a-col v-for="disk in diskMetrics" :span="6">
          <a-card class="metric-card"
                  size="small"
                  :bordered="false"
                  :header-style="{ height: '48px', borderBottom: 'none' }"
                  :body-style="{ height: 'calc(100% - 48px)' }">
            <template #title>
              <h3>磁盘</h3>
              <a-space>
                <a-tag color="green">{{ disk.capacity }}</a-tag>
                <a-tag color="arcoblue">name: {{ disk.name }}</a-tag>
              </a-space>
            </template>
            <div class="card-content">
              <a-statistic title="使用率"
                           :value="disk.usedPercent"
                           :precision="2"
                           :value-style="{ color: getPercentProgressColor(disk.usedPercent / 100, '') }">
                <template #suffix>%</template>
              </a-statistic>
              <a-statistic title="使用量"
                           :value="disk.used"
                           :precision="2"
                           :value-style="{ color: getPercentProgressColor(disk.usedPercent / 100, '') }">
                <template #suffix>{{ disk.usedUnit }}</template>
              </a-statistic>
            </div>
          </a-card>
        </a-col>
        <!-- 磁盘吞吐量 -->
        <a-col :span="6">
          <a-card class="metric-card"
                  size="small"
                  :bordered="false"
                  :header-style="{ height: '48px', borderBottom: 'none' }"
                  :body-style="{ height: 'calc(100% - 48px)' }">
            <template #title>
              <h3>磁盘吞吐量</h3>
            </template>
            <div class="card-content">
              <a-statistic title="读取/秒"
                           :value="ioMetrics.readBytesPerSecond"
                           :precision="2"
                           :value-style="{ color: 'rgb(var(--green-6))' }">
                <template #prefix>
                  <icon-arrow-rise />
                </template>
                <template #suffix>{{ ioMetrics.readsUnit }}</template>
              </a-statistic>
              <a-statistic title="写入/秒"
                           :value="ioMetrics.writeBytesPerSecond"
                           :precision="2"
                           :value-style="{ color: 'rgb(var(--arcoblue-6))' }">
                <template #prefix>
                  <icon-arrow-fall />
                </template>
                <template #suffix>{{ ioMetrics.writesUnit }}</template>
              </a-statistic>
            </div>
          </a-card>
        </a-col>
        <!-- 磁盘 IOPS -->
        <a-col :span="6">
          <a-card class="metric-card"
                  size="small"
                  :bordered="false"
                  :header-style="{ height: '48px', borderBottom: 'none' }"
                  :body-style="{ height: 'calc(100% - 48px)' }">
            <template #title>
              <h3>磁盘 IOPS</h3>
            </template>
            <div class="card-content">
              <a-statistic title="读取/秒"
                           :value="ioMetrics.readsPerSecond"
                           :precision="2"
                           :value-style="{ color: 'rgb(var(--green-6))' }">
                <template #prefix>
                  <icon-arrow-fall />
                </template>
                <template #suffix>次</template>
              </a-statistic>
              <a-statistic title="写入/秒"
                           :value="ioMetrics.writesPerSecond"
                           :precision="2"
                           :value-style="{ color: 'rgb(var(--arcoblue-6))' }">
                <template #prefix>
                  <icon-arrow-fall />
                </template>
                <template #suffix>次</template>
              </a-statistic>
            </div>
          </a-card>
        </a-col>
        <!-- 网络 -->
        <a-col v-for="network in networkMetrics" :span="6">
          <a-card class="metric-card"
                  size="small"
                  :bordered="false"
                  :header-style="{ height: '48px', borderBottom: 'none' }"
                  :body-style="{ height: 'calc(100% - 48px)' }">
            <template #title>
              <h3>网络</h3>
              <a-tag color="arcoblue">name: {{ network.name }}</a-tag>
            </template>
            <div class="card-content">
              <a-statistic title="上行速率/秒"
                           :value="network.sentBytesPerSecond"
                           :precision="2"
                           :value-style="{ color: 'rgb(var(--green-6))' }">
                <template #prefix>
                  <icon-arrow-rise />
                </template>
                <template #suffix>{{ network.sentUnit }}</template>
              </a-statistic>
              <a-statistic title="下行速率/秒"
                           :value="network.recvBytesPerSecond"
                           :precision="2"
                           :value-style="{ color: 'rgb(var(--arcoblue-6))' }">
                <template #prefix>
                </template>
                <template #suffix>{{ network.recvUnit }}</template>
              </a-statistic>
            </div>
          </a-card>
        </a-col>
      </template>
    </a-row>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'hostOverviewTab'
  };
</script>

<script lang="ts" setup>
  import type { HostQueryResponse } from '@/api/asset/host';
  import type { MonitorMetrics } from '@/api/monitor/monitor-host';
  import { ref, onMounted } from 'vue';
  import { getMonitorHostOverride } from '@/api/monitor/monitor-host';
  import { getPercentProgressColor } from '@/utils/charts';
  import { extractUnit, formatBytes } from '@/utils/metrics';

  const props = defineProps<{
    agentKey: string;
    host: HostQueryResponse;
  }>();

  const emits = defineEmits(['setTimestamp']);

  const renderLoading = ref(true);
  const nodata = ref(true);

  const cpuMetrics = ref({ user: 0, system: 0, total: 0 });
  const memoryMetrics = ref({ used: 0, usedUnit: 'B', usedPercent: 0, total: '', swapUsed: 0, swapUsedUnit: 'B', swapUsedPercent: 0, swapTotal: '' });
  const loadMetrics = ref({ load1: 0, load5: 0, load15: 0 });
  const connectionsMetrics = ref({ tcp: 0, udp: 0 });
  const ioMetrics = ref({ readsPerSecond: 0, readBytesPerSecond: 0, readsUnit: 'B', writesPerSecond: 0, writeBytesPerSecond: 0, writesUnit: 'B' });
  const diskMetrics = ref<any[]>([]);
  const networkMetrics = ref<any[]>([]);

  // 重新加载
  const reload = async () => {
    // 加载概览信息
    const { data } = await getMonitorHostOverride(props.agentKey);
    if (data?.timestamp) {
      emits('setTimestamp', data.timestamp);
    }
    if (data?.metrics?.length) {
      nodata.value = false;
      // 解析数据
      parseData(data.metrics);
    } else {
      nodata.value = true;
    }
  };

  defineExpose({ reload });

  // 解析数据
  const parseData = (metrics: Array<MonitorMetrics>) => {
    // cpu
    const cpu = metrics.find(s => s.type === 'cpu');
    if (cpu) {
      cpuMetrics.value = {
        user: cpu.values?.cpu_user_seconds_total || 0,
        system: cpu.values?.cpu_system_seconds_total || 0,
        total: cpu.values?.cpu_total_seconds_total || 0,
      };
    }
    // 内存
    const memory = metrics.find(s => s.type === 'memory');
    if (memory) {
      const used = memory.values?.mem_used_bytes_total || 0;
      const swapUsed = memory.values?.mem_swap_used_bytes_total || 0;
      const usedFormat = formatBytes(used);
      const usedSwapFormat = formatBytes(swapUsed);
      memoryMetrics.value = {
        used: Number.parseFloat(usedFormat),
        usedUnit: extractUnit(usedFormat),
        usedPercent: memory.values?.mem_used_percent || 0,
        total: formatBytes(used / (memory.values?.mem_used_percent || 100) * 100),
        swapUsed: Number.parseFloat(usedSwapFormat),
        swapUsedUnit: extractUnit(usedSwapFormat),
        swapUsedPercent: memory.values?.mem_swap_used_percent || 0,
        swapTotal: formatBytes(swapUsed / (memory.values?.mem_swap_used_percent || 100) * 100),
      };
    }
    // 负载
    const load = metrics.find(s => s.type === 'load');
    if (load) {
      loadMetrics.value = {
        load1: load.values?.load1 || 0,
        load5: load.values?.load5 || 0,
        load15: load.values?.load15 || 0,
      };
    }
    // 连接数
    const connections = metrics.find(s => s.type === 'connections');
    if (connections) {
      connectionsMetrics.value = {
        udp: connections.values?.net_udp_connections || 0,
        tcp: connections.values?.net_tcp_connections || 0,
      };
    }
    // io
    const io = metrics.find(s => s.type === 'io');
    if (io) {
      const readBytesPerSecond = formatBytes(io.values?.disk_io_read_bytes_per_second || 0);
      const writeBytesPerSecond = formatBytes(io.values?.disk_io_write_bytes_per_second || 0);
      ioMetrics.value = {
        readsPerSecond: io.values?.disk_io_reads_per_second || 0,
        readBytesPerSecond: Number.parseFloat(readBytesPerSecond),
        readsUnit: extractUnit(readBytesPerSecond),
        writesPerSecond: io.values?.disk_io_writes_per_second || 0,
        writeBytesPerSecond: Number.parseFloat(writeBytesPerSecond),
        writesUnit: extractUnit(writeBytesPerSecond),
      };
    }
    // 磁盘
    const disks = metrics.filter(s => s.type === 'disk');
    if (disks.length) {
      diskMetrics.value = disks.map(disk => {
        const used = disk.values?.disk_fs_used_bytes_total || 0;
        const total = used / (disk.values?.disk_fs_used_percent || 100) * 100;
        const usedFormat = formatBytes(used);
        const totalFormat = formatBytes(total);
        return {
          name: disk.tags?.['name'] || '',
          used: Number.parseFloat(usedFormat),
          usedUnit: extractUnit(usedFormat),
          usedPercent: disk.values?.disk_fs_used_percent || 0,
          capacity: totalFormat,
        };
      });
    }
    // 网卡
    const networks = metrics.filter(s => s.type === 'network');
    if (networks.length) {
      networkMetrics.value = networks.map(network => {
        const sentBytesPerSecond = network.values?.net_sent_bytes_per_second || 0;
        const recvBytesPerSecond = network.values?.net_recv_bytes_per_second || 0;
        const sentFormat = formatBytes(sentBytesPerSecond);
        const recvFormat = formatBytes(recvBytesPerSecond);
        return {
          name: network.tags?.['name'] || '',
          sentBytesPerSecond: Number.parseFloat(sentFormat),
          sentUnit: extractUnit(sentFormat),
          recvBytesPerSecond: Number.parseFloat(recvFormat),
          recvUnit: extractUnit(recvFormat),
        };
      });
    }
  };

  // 初始化数据
  onMounted(async () => {
    try {
      renderLoading.value = true;
      await reload();
    } catch (e) {
    } finally {
      renderLoading.value = false;
    }
  });

</script>

<style lang="less" scoped>

  :deep(.arco-card-header-title) {
    display: flex;
    justify-content: space-between;
    align-items: center;

    h3 {
      margin: 0;
      display: inline-block;
    }
  }

  .host-info-card {
    height: 100%;
    border-radius: 8px;

    :deep(.arco-card-body) {
      height: 100%;
      padding: 0;
    }

    .host-info-content {
      height: 100%;
      overflow-y: auto;
      padding: 12px 20px 16px 20px;
    }
  }

  .render-skeleton {
    padding: 16px 24px;
    border-radius: 8px;
    background: var(--color-bg-2);;
  }

  .metric-card {
    height: 180px;
    border-radius: 8px;

    :deep(.arco-card-body) {
      height: 100%;
      padding: 0;
    }
  }

  .card-content {
    height: 100%;
    padding: 20px;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    align-items: center;
  }

</style>
