<template>
  <a-card class="full"
          size="small"
          style="border-radius: 8px;"
          :loading="loading"
          :bordered="false"
          :header-style="{ height: '48px', padding: '8px 16px 0 16px', borderBottom: 'none' }"
          :body-style="{ padding: '0', height: 'calc(100% - 48px)', position: 'relative', display: 'flex', alignItems: 'center', justifyContent: 'center' }">
    <!-- 标题 -->
    <template #title>
      <div class="chart-title">
        <!-- 名称 -->
        <h3>{{ option.name }}</h3>
        <!-- 聚合 -->
        <a-tag color="arcoblue">{{ getDictValue(MetricsAggregateKey, option.aggregate) }}</a-tag>
      </div>
    </template>
    <!-- 无数据 -->
    <div v-if="!series.length && !loading"
         class="nodata-chart">
      <a-empty description="此监控指标暂无数据" />
    </div>
    <!-- 图表 -->
    <chart v-else-if="chartOption"
           :options="chartOption"
           class="chart"
           width="100%"
           height="100%" />
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'metricsChart'
  };
</script>

<script lang="ts" setup>
  import type { TimeChartSeries } from '@/types/global';
  import type { MetricsChartProps } from '../types/const';
  import { ref } from 'vue';
  import { dateFormat } from '@/utils';
  import useLoading from '@/hooks/loading';
  import useChartOption from '@/hooks/chart-option';
  import { getMonitorHostChart } from '@/api/monitor/monitor-host';
  import { createTimeSeries, TimeSeriesColors } from '@/types/chart';
  import { MetricsAggregateKey } from '../types/const';
  import { MetricUnitFormatter, WindowUnitFormatter } from '@/utils/metrics';
  import { useDictStore } from '@/store';

  const props = defineProps<MetricsChartProps>();

  const { getDictValue } = useDictStore();
  const { loading, setLoading } = useLoading(true);
  const series = ref<Array<TimeChartSeries>>([]);

  // 数量图表配置
  const { chartOption } = useChartOption((dark, themeTextColor, themeLineColor) => {
    if (!series.value?.length) {
      return {};
    }
    return {
      grid: {
        left: 68,
        right: 24,
        top: 28,
        bottom: 32,
      },
      backgroundColor: 'transparent',
      animation: false,
      dataZoom: [{
        type: 'inside',
        zoomOnMouseWheel: true,
        moveOnMouseMove: true,
      }],
      legend: {
        show: props.option.legend === true,
        type: 'scroll',
        padding: [4, 8, 8, 8],
        textStyle: {
          color: themeTextColor,
        }
      },
      tooltip: {
        trigger: 'axis',
        appendToBody: true,
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        textStyle: {
          color: 'rgba(0, 0, 0, 0.8)',
        },
        formatter: function (params: any) {
          if (!params.length) {
            return '';
          }
          // 表头
          const allTagKeys = new Set();
          params.forEach((item: any) => {
            const tags = series.value?.[item.seriesIndex]?.tags || {};
            Object.keys(tags).forEach(key => allTagKeys.add(key));
          });
          const headerNames = [...allTagKeys, 'value'];
          const gridColumnCount = headerNames.length;
          const gridTemplate = `grid-template-columns: repeat(${gridColumnCount}, auto);`;

          // 模板
          let result = `
            <div class="chart-tooltip-wrapper">
              <div class="chart-tooltip-time">${dateFormat(new Date(params[0].value[0]))}</div>
              <div class="chart-tooltip-header">
                <div class="chart-tooltip-header-grid" style="${gridTemplate}"">
          `;

          // 表头
          headerNames.forEach(key => {
            const textAlign = key === 'value' ? 'right' : 'left';
            result += `
              <div class="chart-tooltip-header-item" style="text-align: ${textAlign};">${key}</div>
            `;
          });

          // 数据行
          params.forEach((item: any) => {
            const tags = series.value?.[item.seriesIndex]?.tags || {};
            const value = item.data[1];
            let displayValue: string;
            if (value === undefined || value === null) {
              displayValue = '-';
            } else {
              displayValue = MetricUnitFormatter[props.option.unit].format(value, props.option.unitOption);
            }
            headerNames.forEach((key, index) => {
              const cellValue = key === 'value' ? displayValue : (tags[key as any] || '-');
              const justifyContent = key === 'value' ? 'flex-end' : 'flex-start';
              // 圆点
              const dot = index === 0
                ? `<span class="chart-tooltip-dot" style="background-color: ${item.color};"></span>`
                : '';
              result += `
                <div class="chart-tooltip-col" style="justify-content: ${justifyContent};">${dot}${cellValue}</div>
              `;
            });
          });

          // 闭合
          result += `</div></div></div>`;
          return result;
        }
      },
      xAxis: {
        type: 'time',
        boundaryGap: false,
        minInterval: WindowUnitFormatter[props.windowUnit].windowInterval(props.windowValue) * 6,
        axisLabel: {
          formatter: (value: number) => WindowUnitFormatter[props.windowUnit].dateFormatter(value),
          rotate: 0,
          interval: 'auto',
          margin: 12,
          hideOverlap: true,
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
            color: TimeSeriesColors.BLUE.lineColor,
            width: 2,
          },
        },
      },
      yAxis: {
        type: 'value',
        axisLabel: {
          color: themeTextColor,
          formatter: (s: number) => MetricUnitFormatter[props.option.unit].format(s, props.option.unitOption)
        },
        axisLine: {
          show: false,
        },
        splitLine: {
          lineStyle: {
            color: themeLineColor,
          },
        },
      },
      series: series.value.map((s, index) => {
        let colors = props.option.colors[index];
        return createTimeSeries({
          name: s.name,
          type: props.option.type,
          smooth: props.option.smooth,
          area: props.option.background,
          lineColor: colors?.[0],
          itemBorderColor: colors?.[1],
          data: s.data
        });
      })
    };
  });

  // 刷新数据
  const refresh = async () => {
    setLoading(true);
    try {
      // 查询数据
      const { data } = await getMonitorHostChart({
        agentKeys: props.agentKeys,
        range: props.range,
        measurement: props.option.measurement,
        fields: props.option.fields,
        aggregate: props.option.aggregate,
        window: WindowUnitFormatter[props.windowUnit].windowFormatter(props.windowValue),
      });
      series.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  defineExpose({ refresh });

</script>

<style lang="less" scoped>
  .chart-title {
    display: flex;
    align-items: center;
    justify-content: space-between;

    h3 {
      margin: 0;
      display: inline-block;
    }
  }

  .nodata-chart {
    font-size: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
  }
</style>
