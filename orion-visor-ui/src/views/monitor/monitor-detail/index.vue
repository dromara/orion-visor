<template>
  <a-spin v-if="hostId"
          class="container"
          :loading="!host">
    <!-- 头部 -->
    <detail-header v-if="host"
                   v-model:activeKey="activeKey"
                   v-model:chartCompose="chartCompose"
                   :host="host"
                   :override-timestamp="overrideTimestamp"
                   @reload-chart="reloadChart" />
    <!-- 内容部分 -->
    <div v-if="host" class="content-container">
      <!-- 监控图表 -->
      <a-tabs v-model:active-key="activeKey"
              class="main-content"
              lazy-load>
        <!-- 主机概览 -->
        <a-tab-pane :key="TabKeys.OVERVIEW">
          <host-overview-tab ref="overrideRef"
                             :host="host"
                             :agent-key="host.agentKey"
                             @set-timestamp="(s: number) => overrideTimestamp = s" />
        </a-tab-pane>
        <!-- 监控图表 -->
        <a-tab-pane :key="TabKeys.CHART">
          <metrics-chart-tab ref="chartRef"
                             :agentKey="host.agentKey"
                             :chartCompose="chartCompose"
                             :chartRange="chartRange"
                             :chartWindow="chartWindow" />
        </a-tab-pane>
        <!-- 告警列表 -->
        <a-tab-pane :key="TabKeys.ALARM">
          <alarm-event-tab :agentKey="host.agentKey" />
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'monitorDetail'
  };
</script>

<script lang="ts" setup>
  import { type HostQueryResponse, getHost } from '@/api/asset/host';
  import { useRoute } from 'vue-router';
  import { onMounted, ref, onUnmounted, onActivated, onDeactivated } from 'vue';
  import { TabKeys } from './types/const';
  import { Message } from '@arco-design/web-vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import { parseWindowUnit, WindowUnitFormatter } from '@/utils/metrics';
  import DetailHeader from './compoments/detail-header.vue';
  import MetricsChartTab from './compoments/metrics-chart-tab.vue';
  import AlarmEventTab from './compoments/alarm-event-tab.vue';
  import HostOverviewTab from './compoments/host-overview-tab.vue';

  const hostId = ref<number>();
  const host = ref<HostQueryResponse>();
  const activeKey = ref(TabKeys.OVERVIEW);
  const chartCompose = ref(true);

  const chartRef = ref();
  const overrideRef = ref();
  const reloadChartId = ref<number>();
  const reloadOverrideId = ref<number>();
  const overrideTimestamp = ref<number>(0);
  const chartRange = ref<string>('-30m');
  const chartWindow = ref<string>('1m');

  // 重新加载
  const reloadChart = (_chartRange: string, _chartWindow: string) => {
    chartRange.value = _chartRange;
    chartWindow.value = _chartWindow;
    // 立即加载和定时加载
    setTimeout(() => {
      chartRef?.value?.reload?.();
    }, 50);
    // 重置定时加载表格;
    resetReloadChartInterval();
  };

  // 重置计时器
  const resetInterval = () => {
    // 重置定时加载概览
    resetReloadOverrideInterval();
    // 重置定时加载图表
    resetReloadChartInterval();
  };

  // 重置定时加载概览
  const resetReloadOverrideInterval = () => {
    // 清除定时
    window.clearInterval(reloadOverrideId.value);
    // 重新设置定时刷新
    reloadOverrideId.value = window.setInterval(() => {
      if (activeKey.value === TabKeys.OVERVIEW) {
        overrideRef.value.reload();
      }
    }, 60000);
  };

  // 重置定时加载图表
  const resetReloadChartInterval = () => {
    if (!chartWindow.value) {
      return;
    }
    // 清除定时
    window.clearInterval(reloadChartId.value);
    // 计算窗口
    const [windowTime, windowUnit] = parseWindowUnit(chartWindow.value as string);
    const interval = WindowUnitFormatter[windowUnit].windowInterval(windowTime) + 5000;
    // 重新设置定时刷新
    reloadChartId.value = window.setInterval(() => {
      if (activeKey.value === TabKeys.CHART) {
        chartRef.value.reload();
      }
    }, interval);
  };

  // 清除计时器
  const clearInterval = () => {
    // 清除定时刷新概览
    window.clearInterval(reloadOverrideId.value);
    // 清除定时刷新图表
    window.clearInterval(reloadChartId.value);
  };

  onMounted(async () => {
    const route = useRoute();
    hostId.value = parseInt(route.query.hostId as string);
    if (!hostId.value) {
      Message.error('参数错误');
      return;
    }
    // 加载字典项
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    // 查询主机信息
    const { data } = await getHost(hostId.value);
    host.value = data;
  });

  onActivated(resetInterval);
  onDeactivated(clearInterval);
  onUnmounted(clearInterval);

</script>

<style lang="less" scoped>
  .container {
    width: 100%;
    height: 100%;
    padding: 16px;
    position: relative;
  }

  .content-container {
    border-radius: 4px;
  }

  .main-content {
    :deep(.arco-tabs-nav-tab) {
      display: none;
    }

    :deep(.arco-tabs-content) {
      padding-top: 0 !important;
    }
  }
</style>
