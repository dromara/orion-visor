<template>
  <div class="header-container">
    <!-- 左侧 -->
    <div class="header-left">
      <!-- tab切换 -->
      <div class="tab-container">
        <a-tabs v-model:active-key="activeKey"
                type="rounded"
                :hide-content="true">
          <a-tab-pane :key="TabKeys.OVERVIEW" title="主机概览" />
          <a-tab-pane :key="TabKeys.CHART" title="监控图表" />
          <a-tab-pane :key="TabKeys.ALARM" title="告警事件" />
        </a-tabs>
        <a-divider direction="vertical"
                   style="height: 22px; margin: 0 16px 0 8px;"
                   :size="2" />
      </div>
      <!-- 基本信息-->
      <div class="info-container">
        <!-- 标题 -->
        <div class="title">{{ host.name }}</div>
        <!-- 地址 -->
        <a-tag class="text-copy"
               color="arcoblue"
               @click="copy(host.address, true)">
          {{ host.address }}
        </a-tag>
        <!-- 编码 -->
        <a-tag color="arcoblue">{{ host.code }}</a-tag>
        <!-- tags -->
        <a-tag v-for="tag in (host.tags || [])" color="arcoblue">{{ tag.name }}</a-tag>
        <!-- 在线状态 -->
        <a-tag :color="getDictValue(OnlineStatusKey, host.agentOnlineStatus, 'color')">
          <template #icon>
            <component :is="getDictValue(OnlineStatusKey, host.agentOnlineStatus, 'icon')" />
          </template>
          {{ getDictValue(OnlineStatusKey, host.agentOnlineStatus) }}
        </a-tag>
        <!-- 版本号 -->
        <a-tag color="green">v{{ host.agentVersion }}</a-tag>
      </div>
    </div>
    <!-- 右侧 -->
    <div class="header-right">
      <!-- 告警事件标签 -->
      <div v-if="activeKey === TabKeys.OVERVIEW" class="handle-wrapper">
        <!-- 单协议连接 -->
        <a-tag v-if="host.types?.length === 1"
               v-permission="['terminal:terminal:access']"
               class="pointer"
               @click="openNewRoute({ name: 'terminal', query: { connect: host.id, type: host.types[0] } })">
          连接终端
        </a-tag>
        <!-- 多协议连接 -->
        <a-popover v-if="(host.types?.length || 0) > 1"
                   :title="undefined"
                   :content-style="{ padding: '8px' }">
          <a-tag v-permission="['terminal:terminal:access']"
                 class="pointer">
            连接终端
          </a-tag>
          <template #content>
            <a-space>
              <a-button v-for="type in host.types"
                        :key="type"
                        size="mini"
                        @click="openNewRoute({ name: 'terminal', query: { connect: host.id, type }})">
                {{ type }}
              </a-button>
            </a-space>
          </template>
        </a-popover>
        <!-- 更新时间 -->
        <a-tag v-if="overrideTimestamp">更新时间: {{ dateFormat(new Date(overrideTimestamp)) }}</a-tag>
      </div>
      <!-- 监控图表操作 -->
      <div v-else-if="activeKey === TabKeys.CHART" class="handle-wrapper">
        <!-- 表格时间区间 -->
        <a-select v-model="chartRange"
                  style="width: 138px;"
                  :options="toOptions(ChartRangeKey)">
          <template #prefix>
            区间
          </template>
        </a-select>
        <!-- 表格窗口 -->
        <a-select v-model="chartWindow"
                  style="width: 138px;"
                  :options="chartWindowOptions">
          <template #prefix>
            窗口
          </template>
        </a-select>
        <!-- 刷新 -->
        <a-button class="fs16"
                  title="刷新"
                  @click="reloadChart">
          <template #icon>
            <icon-refresh />
          </template>
        </a-button>
        <!-- 切换视图 -->
        <a-button class="fs16"
                  title="切换视图"
                  @click="chartCompose = !chartCompose">
          <template #icon>
            <icon-menu v-if="chartCompose" />
            <icon-apps v-else />
          </template>
        </a-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'detail-header'
  };
</script>

<script lang="ts" setup>
  import type { HostQueryResponse } from '@/api/asset/host';
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { ref, onMounted, nextTick } from 'vue';
  import { copy } from '@/hooks/copy';
  import { useDictStore } from '@/store';
  import { dateFormat } from '@/utils';
  import { openNewRoute } from '@/router';
  import { TabKeys, ChartRangeKey } from '../types/const';
  import { OnlineStatusKey } from '@/views/monitor/monitor-host/types/const';
  import { parseWindowUnit, WindowUnitFormatter } from '@/utils/metrics';

  defineProps<{
    host: HostQueryResponse;
    overrideTimestamp: number;
  }>();
  const emits = defineEmits(['reloadChart']);
  const activeKey = defineModel('activeKey', { type: String });
  const chartCompose = defineModel('chartCompose', { type: Boolean });

  const chartRange = ref('-30m');
  const chartWindow = ref('1m');
  const chartWindowOptions = ref<Array<SelectOptionData>>([{ label: '1分钟', value: '1m' }]);

  const { toOptions, getDictValue } = useDictStore();

  // 加载图表
  const reloadChart = () => {
    emits('reloadChart', chartRange.value, chartWindow.value);
  };

  // 切换图表区间
  const changeChartRange = (value: string) => {
    const windowValue = getDictValue(ChartRangeKey, value, 'window') as string;
    chartWindowOptions.value = windowValue.split(',').map(s => {
      const [value, unit] = parseWindowUnit(s);
      const label = WindowUnitFormatter[unit].labelFormatter(value);
      return {
        label,
        value: s,
      };
    });
    chartWindow.value = chartWindowOptions.value[0].value as string;
  };

  onMounted(() => {
    nextTick(() => {
      reloadChart();
    });
  });

</script>

<style lang="less" scoped>
  .header-container {
    width: 100%;
    height: 64px;
    margin-bottom: 16px;
    background-color: var(--color-bg-2);
    border-radius: 8px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  .header-left {
    display: flex;
    align-items: center;

    .info-container {
      display: flex;
      color: var(--color-text-2);

      & > * {
        margin-right: 14px;
        display: flex;
        align-items: center;
      }

      .title {
        font-size: 20px;
        font-weight: bold;
        color: var(--color-text-1);
      }
    }

    .tab-container {
      padding: 0 0 0 12px;
      display: flex;
      align-items: center;
    }

    :deep(.arco-tabs) {
      user-select: none;

      .arco-tabs-tab {
        background-color: var(--color-fill-2);
      }

      .arco-tabs-nav::before {
        height: 0 !important;
      }

      .arco-tabs-tab {
        font-size: 16px;
      }
    }
  }

  .header-right {
    padding-right: 16px;

    .handle-wrapper {
      display: flex;
      gap: 8px;
    }
  }

</style>
