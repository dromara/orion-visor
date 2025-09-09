import type { BarSeriesOption, LineSeriesOption } from 'echarts';
import type { LineChartData, Options } from '@/types/global';

type TimeSeriesType = 'line' | 'bar';

/**
 * 时序系列定义
 */
export interface TimeSeriesColor {
  lineColor: string;
  itemBorderColor: string;
}

/**
 * 时间系列配置
 */
export interface TimeSeriesOption {
  name: string;
  type: TimeSeriesType;
  area: boolean;
  lineColor: string;
  itemBorderColor: string;
  data: any[];
}

/**
 * 时序系列常量
 */
export const TimeSeriesColors: Record<string, TimeSeriesColor> = {
  BLUE: {
    lineColor: '#4263EB',
    itemBorderColor: '#DBE4FF',
  },
  GREEN: {
    lineColor: '#37B24D',
    itemBorderColor: '#D3F9D8',
  },
  CYAN: {
    lineColor: '#1098AD',
    itemBorderColor: '#C5F6FA',
  },
  YELLOW: {
    lineColor: '#F59F00',
    itemBorderColor: '#FFF3BF',
  },
  PURPLE: {
    lineColor: '#AE3EC9',
    itemBorderColor: '#F3D9FA',
  },
  VIOLET: {
    lineColor: '#7048E8',
    itemBorderColor: '#E5DBFF',
  },
  LIME: {
    lineColor: '#74B816',
    itemBorderColor: '#E9FAC8',
  },
  ORANGE: {
    lineColor: '#F76707',
    itemBorderColor: '#FFF3BF',
  },
  INDIGO: {
    lineColor: '#4263EB',
    itemBorderColor: '#DBE4FF',
  },
  TEAL: {
    lineColor: '#0CA678',
    itemBorderColor: '#C3FAE8',
  },
  RED: {
    lineColor: '#F03E3E',
    itemBorderColor: '#FFE3E3',
  }
};

/**
 * 生成时序系列
 */
export const generateTimeSeriesArr = (options: Array<Options>,
                                      chartData: LineChartData,
                                      type: TimeSeriesType = 'line'): Array<LineSeriesOption | BarSeriesOption> => {
  const arr = [];
  const optionLen = options.length;
  for (let i = 0; i < optionLen; i++) {
    // 选项
    const option = options[i];
    // 获取颜色
    let color;
    if (option.seriesColor) {
      color = TimeSeriesColors[option.seriesColor as keyof typeof TimeSeriesColors];
    }
    if (!color) {
      color = Object.values(TimeSeriesColors)[i % Object.keys(TimeSeriesColors).length];
    }
    // 获取数据
    const data = chartData.data[option.value as keyof typeof chartData.data] || [];
    // 生成系列
    arr.push(createTimeSeries({
      name: option.label,
      area: true,
      type,
      lineColor: color.lineColor,
      itemBorderColor: color.itemBorderColor,
      data,
    }));
  }
  return arr as Array<LineSeriesOption>;
};

/**
 * 创建时序系列
 */
export const createTimeSeries = (option: Partial<TimeSeriesOption>): LineSeriesOption | BarSeriesOption => {
  // 设置默认值
  if (option.area === undefined) {
    option.area = true;
  }
  if (option.lineColor === undefined) {
    option.lineColor = TimeSeriesColors.BLUE.lineColor;
  }
  if (option.itemBorderColor === undefined) {
    option.itemBorderColor = TimeSeriesColors.BLUE.itemBorderColor;
  }
  // 配置项
  return {
    name: option.name,
    data: option.data || [],
    type: option.type || 'line',
    smooth: true,
    symbol: 'circle',
    symbolSize: 10,
    itemStyle: {
      color: option.lineColor,
    },
    emphasis: {
      focus: 'series',
      itemStyle: {
        color: option.lineColor,
        borderWidth: 2,
        borderColor: option.itemBorderColor,
      },
    },
    lineStyle: {
      width: 2,
      color: option.lineColor,
    },
    showSymbol: option.data?.length === 1,
    areaStyle: option.area ? {
      opacity: 0.1,
      color: option.lineColor,
    } : undefined,
  };
};
