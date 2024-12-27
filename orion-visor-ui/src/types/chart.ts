import type { LineSeriesOption } from 'echarts';

/**
 * 折线图系列定义
 */
export interface LineSeriesColor {
  lineColor: string;
  itemBorderColor: string;
}

/**
 * 折线图系列常量
 */
export const LineSeriesColors: Record<string, LineSeriesColor> = {
  BLUE: {
    lineColor: '#4263EB',
    itemBorderColor: '#DBE4FF',
  },
  CYAN: {
    lineColor: '#1098AD',
    itemBorderColor: '#C5F6FA',
  },
  GREEN: {
    lineColor: '#37B24D',
    itemBorderColor: '#D3F9D8',
  },
  PURPLE: {
    lineColor: '#AE3EC9',
    itemBorderColor: '#F3D9FA',
  },
  ORANGE: {
    lineColor: '#F76707',
    itemBorderColor: '#FFF3BF',
  },
  VIOLET: {
    lineColor: '#7048E8',
    itemBorderColor: '#E5DBFF',
  },
  YELLOW: {
    lineColor: '#F59F00',
    itemBorderColor: '#FFF3BF',
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
 * 生成折线图系列
 */
export const createLineSeries = (name: string,
                                 lineColor: string,
                                 itemBorderColor: string,
                                 data: number[]): LineSeriesOption => {
  return {
    name,
    data,
    type: 'line',
    smooth: true,
    symbol: 'circle',
    symbolSize: 10,
    itemStyle: {
      color: lineColor,
    },
    emphasis: {
      focus: 'series',
      itemStyle: {
        color: lineColor,
        borderWidth: 2,
        borderColor: itemBorderColor,
      },
    },
    lineStyle: {
      width: 2,
      color: lineColor,
    },
    showSymbol: data.length === 1,
    areaStyle: {
      opacity: 0.1,
      color: lineColor,
    },
  };
};
