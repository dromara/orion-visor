<template>
  <div class="layout-container" v-if="render">
    <!-- 头部 -->
    <workplace-header class="mb16"
                      :data="statisticsData" />
    <!-- 统计信息 -->
    <workplace-statistics class="mb16"
                          :data="statisticsData" />
    <a-row :gutter="16" class="mb16" align="stretch">
      <!-- 最近终端连接表格 -->
      <terminal-connect-table :loading="terminalLoading"
                              :data="statisticsData" />
      <!-- 最近批量执行表格 -->
      <batch-exec-table :loading="execLoading"
                        :data="statisticsData" />
      <!-- 快捷操作 -->
      <quick-operation />
    </a-row>
    <a-row :gutter="16" align="stretch">
      <!-- 每日操作数量图表 -->
      <operator-log-chart :data="statisticsData" />
      <!-- 用户登录日志 -->
      <user-login-table :loading="infraLoading"
                        :data="statisticsData" />
    </a-row>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'workplace',
  };
</script>

<script lang="ts" setup>
  import type { WorkplaceStatisticsData } from './types/const';
  import { onBeforeMount, onMounted, ref } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import useLoading from '@/hooks/loading';
  import { getExecWorkplaceStatisticsData } from '@/api/statistics/exec-statistics';
  import { getTerminalWorkplaceStatisticsData } from '@/api/statistics/terminal-statistics';
  import { getInfraWorkplaceStatisticsData } from '@/api/statistics/infra-statistics';
  import WorkplaceHeader from './components/workplace-header.vue';
  import WorkplaceStatistics from './components/workplace-statistics.vue';
  import TerminalConnectTable from './components/terminal-connect-table.vue';
  import BatchExecTable from './components/batch-exec-table.vue';
  import QuickOperation from './components/quick-operation.vue';
  import UserLoginTable from './components/user-login-table.vue';
  import OperatorLogChart from './components/operator-log-chart.vue';

  const { loading: infraLoading, setLoading: setInfraLoading } = useLoading();
  const { loading: execLoading, setLoading: setExecLoading } = useLoading();
  const { loading: terminalLoading, setLoading: setTerminalLoading } = useLoading();

  const render = ref(false);
  const statisticsData = ref({} as WorkplaceStatisticsData);

  const getWorkplaceData = () => {
    // 基建模块
    setInfraLoading(true);
    getInfraWorkplaceStatisticsData().then(({ data }) => {
      setInfraLoading(false);
      statisticsData.value.infra = data;
    }).catch(() => {
      setInfraLoading(false);
    });
    // 执行模块
    setExecLoading(true);
    getExecWorkplaceStatisticsData().then(({ data }) => {
      setExecLoading(false);
      statisticsData.value.exec = data;
    }).catch(() => {
      setExecLoading(false);
    });
    // 终端模块
    setTerminalLoading(true);
    getTerminalWorkplaceStatisticsData().then(({ data }) => {
      setTerminalLoading(false);
      statisticsData.value.terminal = data;
    }).catch(() => {
      setTerminalLoading(false);
    });
  };

  onMounted(getWorkplaceData);

  // 加载字典值
  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

  :deep(.card) {
    padding: 16px 20px;
    border-radius: 4px;
    background-color: var(--color-bg-2);

    &-title {
      display: flex;
      justify-content: space-between;
      margin-bottom: 16px;
      user-select: none;

      &-left {
        margin: 0;
        color: var(--color-text-1);
        font-weight: 600;
      }
    }

    &-body {
      height: calc(100% - 36px);
    }
  }

  :deep(.arco-table-empty) {
    .arco-table-td {
      border-bottom: none;
    }
  }

</style>
