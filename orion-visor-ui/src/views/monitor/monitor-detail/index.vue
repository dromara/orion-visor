<template>
  <a-spin v-if="hostId"
          class="container"
          :loading="!host">
    <!-- 头部 -->
    <detail-header v-if="host"
                   v-model:activeKey="activeKey"
                   v-model:chartCompose="chartCompose"
                   :host="host"
                   @reload-chart="reloadChart" />
    <!-- 内容部分 -->
    <div v-if="host" class="content-container">
      <!-- 监控图表 -->
      <metrics-chart-tab v-show="activeKey === TabKeys.CHART"
                         ref="chartRef"
                         :agentKey="host.agentKey"
                         :chartCompose="chartCompose"
                         :chartRange="chartRange"
                         :chartWindow="chartWindow" />
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

  const hostId = ref<number>();
  const host = ref<HostQueryResponse>();
  const activeKey = ref(TabKeys.CHART);
  const chartCompose = ref(true);

  const chartRef = ref();
  const reloadChartId = ref<number>();
  const chartRange = ref<string>('-30m');
  const chartWindow = ref<string>('1m');

  // 重新加载
  const reloadChart = (_chartRange: string, _chartWindow: string) => {
    chartRange.value = _chartRange;
    chartWindow.value = _chartWindow;
    // 立即加载和定时加载
    setTimeout(() => {
      chartRef.value.reload();
    }, 50);
    // 重置定时加载表格;
    resetReloadChartInterval();
  };

  // 重置定时加载表格
  const resetReloadChartInterval = () => {
    if (!chartWindow.value) {
      return;
    }
    // 清除定时
    window.clearInterval(reloadChartId.value);
    // 计算窗口
    const [windowTime, windowUnit] = parseWindowUnit(chartWindow.value as string);
    const interval = WindowUnitFormatter[windowUnit].windowInterval(windowTime) + 5000;
    // 重新设置定时
    reloadChartId.value = window.setInterval(() => {
      chartRef.value.reload();
    }, interval);
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

  onActivated(resetReloadChartInterval);
  onDeactivated(() => window.clearInterval(reloadChartId.value));
  onUnmounted(() => window.clearInterval(reloadChartId.value));

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
</style>
