<template>
  <a-col :span="16">
    <div class="card full">
      <div class="card-title">
        <p class="card-title-left">系统操作数量 (7日)</p>
        <!-- 跳转 -->
        <span class="pointer span-blue"
              title="详情"
              @click="router.push({ name: 'userInfo', query: { tab: 'operatorLog' } })">
          详情
        </span>
      </div>
      <!-- 图表 -->
      <div class="card-body">
        <chart height="440px" :options="chartOption" />
      </div>
    </div>
  </a-col>
</template>

<script lang="ts">
  export default {
    name: 'operator-log-chart'
  };
</script>

<script lang="ts" setup>
  import type { WorkplaceStatisticsData } from '@/views/dashboard/workplace/types/const';
  import { createLineSeries, LineSeriesColors } from '@/types/chart';
  import { useRouter } from 'vue-router';
  import useChartOption from '@/hooks/chart-option';

  const props = defineProps<{
    data: WorkplaceStatisticsData;
  }>();

  const router = useRouter();

  // 数量图表配置
  const { chartOption } = useChartOption((dark, themeTextColor, themeLineColor) => {
    return {
      grid: {
        left: '50',
        right: '36',
        top: '12',
        bottom: '32',
      },
      xAxis: {
        type: 'category',
        offset: 2,
        data: props.data.infra?.operatorChart.x || [],
        boundaryGap: false,
        axisLabel: {
          color: themeTextColor,
        },
        axisLine: {
          show: false,
        },
        axisTick: {
          show: false,
        },
        splitLine: {
          show: false,
        },
        axisPointer: {
          show: true,
          lineStyle: {
            color: '#23ADFF',
            width: 2,
          },
        },
      },
      yAxis: {
        type: 'value',
        axisLine: {
          show: false,
        },
        axisLabel: {
          color: themeTextColor,
          formatter: (value: number) => {
            return Math.floor(value);
          }
        },
        splitLine: {
          lineStyle: {
            color: themeLineColor,
          },
        },
      },
      tooltip: {
        trigger: 'axis',
      },
      series: [
        createLineSeries('操作数量', LineSeriesColors.BLUE.lineColor, LineSeriesColors.BLUE.itemBorderColor, props.data.infra?.operatorChart.data || []),
      ],
    };
  });

</script>

<style lang="less" scoped>

</style>
