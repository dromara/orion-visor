<template>
  <div class="charts-container">
    <a-grid :cols="chartCols" :colGap="16" :rowGap="16">
      <a-grid-item v-for="(item, index) of chartItems"
                   :span="item.option?.span || 1"
                   :style="{ height: (chartCompose ? '400px': '270px') }">
        <metrics-chart v-bind="item" :ref="(el: any) => setRef(index, el)" />
      </a-grid-item>
    </a-grid>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'metricsChartTab'
  };
</script>

<script lang="ts" setup>
  import type { MetricsChartProps, MetricsChartOption } from '../types/const';
  import { computed, ref, onMounted } from 'vue';
  import { parseWindowUnit, MetricsUnit, type MetricUnitType } from '@/utils/metrics';
  import { TimeSeriesColors } from '@/types/chart';
  import MetricsChart from './metrics-chart.vue';

  const props = defineProps<{
    agentKey: string;
    chartCompose: boolean;
    chartRange: string;
    chartWindow: string;
  }>();

  const chartsRef = ref<Record<number, any>>({});

  // 响应式布局
  const chartCols = computed(() => {
    return {
      xs: 1,
      sm: props.chartCompose ? 2 : 1,
      md: props.chartCompose ? 2 : 1,
      lg: props.chartCompose ? 2 : 1,
      xl: props.chartCompose ? 3 : 1,
      xxl: props.chartCompose ? 3 : 1,
    };
  });

  // 设置图表引用
  const setRef = (index: number, el: any) => {
    chartsRef.value[index] = el;
  };

  // 图表项
  const chartItems = computed<Array<MetricsChartProps>>(() => {
    // 获取窗口单位
    const [windowValue, windowUnit] = parseWindowUnit(props.chartWindow);
    const options: Array<MetricsChartOption> = [
      {
        name: 'CPU使用率',
        measurement: 'cpu',
        fields: ['cpu_total_seconds_total'],
        colors: [[TimeSeriesColors.BLUE.lineColor, TimeSeriesColors.BLUE.itemBorderColor]],
        aggregate: 'mean',
        unit: MetricsUnit.PER as MetricUnitType,
        unitOption: { precision: 3 }
      },
      {
        name: '内存使用率',
        measurement: 'memory',
        fields: ['mem_used_percent', 'mem_swap_used_percent'],
        colors: [[TimeSeriesColors.LIME.lineColor, TimeSeriesColors.LIME.itemBorderColor], [TimeSeriesColors.TEAL.lineColor, TimeSeriesColors.TEAL.itemBorderColor]],
        aggregate: 'mean',
        unit: MetricsUnit.PER as MetricUnitType,
        unitOption: { precision: 3 }
      },
      {
        name: '内存使用量',
        measurement: 'memory',
        fields: ['mem_used_bytes_total', 'mem_swap_used_bytes_total'],
        colors: [[TimeSeriesColors.LIME.lineColor, TimeSeriesColors.LIME.itemBorderColor], [TimeSeriesColors.TEAL.lineColor, TimeSeriesColors.TEAL.itemBorderColor]],
        aggregate: 'mean',
        unit: MetricsUnit.BYTES as MetricUnitType,
        unitOption: { precision: 2 }
      },
      {
        name: '系统负载',
        measurement: 'load',
        fields: ['load1', 'load5', 'load15'],
        span: 1,
        legend: true,
        background: false,
        colors: [[TimeSeriesColors.LIME.lineColor, TimeSeriesColors.LIME.itemBorderColor], [TimeSeriesColors.RED.lineColor, TimeSeriesColors.RED.itemBorderColor], [TimeSeriesColors.BLUE.lineColor, TimeSeriesColors.BLUE.itemBorderColor]],
        aggregate: 'mean',
        unit: MetricsUnit.NONE as MetricUnitType,
        unitOption: { precision: 2 }
      },
      {
        name: '磁盘使用率',
        measurement: 'disk',
        fields: ['disk_fs_used_percent'],
        colors: [[TimeSeriesColors.VIOLET.lineColor, TimeSeriesColors.VIOLET.itemBorderColor]],
        aggregate: 'mean',
        unit: MetricsUnit.PER as MetricUnitType,
        unitOption: { precision: 2 }
      },
      {
        name: '磁盘使用量',
        measurement: 'disk',
        fields: ['disk_fs_used_bytes_total'],
        colors: [[TimeSeriesColors.LIME.lineColor, TimeSeriesColors.LIME.itemBorderColor]],
        aggregate: 'mean',
        unit: MetricsUnit.BYTES as MetricUnitType,
        unitOption: { precision: 2 }
      },
      {
        name: '网络连接数',
        measurement: 'connections',
        fields: ['net_tcp_connections', 'net_udp_connections'],
        colors: [[TimeSeriesColors.CYAN.lineColor, TimeSeriesColors.CYAN.itemBorderColor]],
        aggregate: 'mean',
        unit: MetricsUnit.COUNT as MetricUnitType,
        unitOption: { precision: 0, suffix: '个' }
      },
      {
        name: '网络带宽',
        measurement: 'network',
        fields: ['net_sent_bytes_per_second', 'net_recv_bytes_per_second'],
        colors: [[TimeSeriesColors.BLUE.lineColor, TimeSeriesColors.BLUE.itemBorderColor], [TimeSeriesColors.GREEN.lineColor, TimeSeriesColors.GREEN.itemBorderColor]],
        aggregate: 'mean',
        unit: MetricsUnit.BITS_S as MetricUnitType,
        unitOption: { precision: 2 }
      },
      {
        name: '磁盘IO',
        measurement: 'io',
        fields: ['disk_io_read_bytes_per_second', 'disk_io_write_bytes_per_second'],
        colors: [[TimeSeriesColors.CYAN.lineColor, TimeSeriesColors.CYAN.itemBorderColor], [TimeSeriesColors.YELLOW.lineColor, TimeSeriesColors.YELLOW.itemBorderColor]],
        aggregate: 'mean',
        unit: MetricsUnit.BYTES_S as MetricUnitType,
        unitOption: { precision: 2 }
      },
    ];
    return options.map(option => {
      return {
        agentKeys: [props.agentKey],
        range: props.chartRange,
        windowValue: windowValue,
        windowUnit,
        option,
      };
    });
  });

  // 重新加载
  const reload = async () => {
    const allCharts = Object.values(chartsRef.value);
    const chunks = [];
    // 分组
    for (let i = 0; i < allCharts.length; i += 3) {
      chunks.push(allCharts.slice(i, i + 3));
    }
    // 顺序刷新
    for (const chunk of chunks) {
      try {
        await Promise.all(chunk.map(s => s.refresh()));
      } catch (e) {
      }
    }
  };

  defineExpose({ reload });

  onMounted(reload);

</script>

<style lang="less" scoped>

  .charts-container {
    width: 100%;
  }
</style>
