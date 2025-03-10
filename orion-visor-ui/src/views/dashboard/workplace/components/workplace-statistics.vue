<template>
  <a-row :gutter="16">
    <!-- 统计信息 -->
    <a-col v-for="item in summaryItems"
           :span="4"
           class="statistics-card">
      <div class="card pointer"
           title="双击查看详情"
           @dblclick="item.go">
        <a-statistic :title="item.title"
                     :value="item.value"
                     :value-from="0"
                     :animation-duration="1000"
                     animation
                     show-group-separator>
          <template #prefix>
            <span class="statistic-prefix"
                  :style="{ background: item.prefix.background }">
              <component :is="item.prefix.icon"
                         :style="{ color: item.prefix.iconColor }" />
            </span>
          </template>
        </a-statistic>
      </div>
    </a-col>
    <!-- 连接终端次数图表 -->
    <a-col :span="4" class="statistics-card">
      <div class="card" :style="{
            background: isDark
              ? 'linear-gradient(180deg, #284991 0%, #122B62 100%)'
              : 'linear-gradient(180deg, #CAE6FA 0%, #CAE6FA 100%)',
           }">
        <div class="chart-container">
          <div class="statistics-wrapper">
            <a-statistic title="连接终端次数 (7日)"
                         :value="data.asset?.weekTerminalConnectCount || 0"
                         :value-from="0"
                         :animation-duration="1000"
                         animation
                         show-group-separator />
          </div>
          <div class="chart-wrapper">
            <chart width="100%" height="64px" :option="terminalConnectChart" />
          </div>
        </div>
      </div>
    </a-col>
    <!-- 批量执行次数图表 -->
    <a-col :span="4" class="statistics-card">
      <div class="card" :style="{
            background: isDark
              ? 'linear-gradient(180deg, #424f32 0%, #424f32 100%)'
              : 'linear-gradient(180deg, #BCF5CF 0%, #BCF5CF 100%)',
           }">
        <div class="chart-container">
          <div class="statistics-wrapper">
            <a-statistic title="批量执行次数 (7日)"
                         :value="data.asset?.weekExecCommandCount || 0"
                         :value-from="0"
                         :animation-duration="1000"
                         animation
                         show-group-separator />
          </div>
          <div class="chart-wrapper">
            <chart width="100%" height="64px" :option="execCommandChart" />
          </div>
        </div>
      </div>
    </a-col>
  </a-row>
</template>

<script lang="ts">
  export default {
    name: 'workplaceStatistics'
  };
</script>

<script lang="ts" setup>
  import type { WorkplaceStatisticsData } from '@/views/dashboard/workplace/types/const';
  import { computed } from 'vue';
  import { useRouter } from 'vue-router';
  import useThemes from '@/hooks/themes';
  import useChartOption from '@/hooks/chart-option';

  const props = defineProps<{
    data: WorkplaceStatisticsData;
  }>();

  const router = useRouter();
  const { isDark } = useThemes();

  const summaryItems = computed(() => [
    {
      title: '今日连接终端次数',
      value: props.data.asset?.todayTerminalConnectCount || 0,
      prefix: {
        icon: 'icon-history',
        background: isDark.value ? '#354276' : '#E8F3FF',
        iconColor: isDark.value ? '#4A7FF7' : '#165DFF',
      },
      go: () => router.push({ name: 'connectLog', query: { action: 'self' } })
    }, {
      title: '今日批量执行次数',
      value: props.data.asset?.todayExecCommandCount || 0,
      prefix: {
        icon: 'icon-code-block',
        background: isDark.value ? '#3F385E' : '#F5E8FF',
        iconColor: isDark.value ? '#8558D3' : '#722ED1',
      },
      go: () => router.push({ name: 'execCommandLog', query: { action: 'self' } })
    }, {

      title: '当前登录设备数量',
      value: props.data.infra?.userSessionCount || 0,
      prefix: {
        icon: 'icon-desktop',
        background: isDark.value ? '#3D5A62' : '#D6FFF8',
        iconColor: isDark.value ? '#6ED1CE' : '#33D1C9',
      },
      go: () => router.push({ name: 'userInfo', query: { tab: 'userSession' } })

    }, {
      title: '管理的任务数量',
      value: props.data.asset?.execJobCount || 0,
      prefix: {
        icon: 'icon-calendar-clock',
        background: isDark.value ? '#3F385E' : '#F5E8FF',
        iconColor: isDark.value ? '#8558D3' : '#722ED1',
      },
      go: () => router.push({ name: 'execJob', query: { action: 'self' } })
    }
  ]);

  const { chartOption: terminalConnectChart } = useChartOption(() => {
    return {
      grid: {
        left: 8,
        right: 8,
        top: 8,
        bottom: 8,
      },
      xAxis: {
        type: 'category',
        data: props.data.asset?.terminalConnectChart?.x || [],
        show: false,
      },
      yAxis: {
        show: false,
      },
      tooltip: {
        show: true,
        trigger: 'axis',
      },
      series: {
        name: '连接次数',
        data: !props.data.asset?.terminalConnectChart
          ? []
          : props.data.asset?.terminalConnectChart.data.map(s => {
            return {
              value: s,
            };
          }),
        type: 'line',
        showSymbol: false,
        smooth: true,
        lineStyle: {
          color: '#165DFF',
          width: 3,
          type: 'dashed',
        },
      },
    };
  });

  const { chartOption: execCommandChart } = useChartOption(() => {
    return {
      grid: {
        left: 8,
        right: 8,
        top: 8,
        bottom: 8,
      },
      xAxis: {
        type: 'category',
        data: props.data.asset?.execCommandChart?.x || [],
        show: false,
      },
      yAxis: {
        show: false,
      },
      tooltip: {
        show: true,
        trigger: 'axis',
      },
      series: {
        name: '执行次数',
        data: !props.data.asset?.execCommandChart
          ? []
          : props.data.asset?.execCommandChart.data.map((s, index) => {
            const x = props.data.asset?.execCommandChart.x;
            return {
              x: x[index],
              value: s,
              itemStyle: { color: '#2CAB40' },
            };
          }),
        type: 'bar',
        barWidth: 7,
        itemStyle: {
          borderRadius: 2,
        },
      },
    };
  });

</script>

<style lang="less" scoped>
  @card-height: 104px;

  .statistics-card {
    height: @card-height;

    :deep(.arco-statistic) {
      width: 100%;

      .arco-statistic-title {
        color: rgb(var(--gray-10));
        font-weight: bold;
        user-select: none;
        width: 100%;
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
      }

      .arco-statistic-value {
        display: flex;
        align-items: center;
      }
    }

    .statistic-prefix {
      display: inline-block;
      width: 32px;
      height: 32px;
      margin-right: 8px;
      color: var(--color-white);
      font-size: 16px;
      line-height: 32px;
      text-align: center;
      vertical-align: middle;
      border-radius: 6px;
    }

    .chart-container {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-radius: 4px;

      .statistics-wrapper {
        width: 134px;
      }

      .chart-wrapper {
        width: calc(100% - 134px);
      }
    }
  }

</style>
