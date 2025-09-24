import { dateFormat } from '@/utils/index';

// 监控指标单位
export const MetricsUnit = {
  BYTES: 'BYTES',
  BITS: 'BITS',
  COUNT: 'COUNT',
  PER: 'PER',
  SECONDS: 'SECONDS',
  BYTES_S: 'BYTES_S',
  BITS_S: 'BITS_S',
  COUNT_S: 'COUNT_S',
  TEXT: 'TEXT',
  NONE: 'NONE',
};

// 指标单位类型
export type MetricUnitType = keyof typeof MetricsUnit;

// 窗口单位
export type WindowUnit =
  | 'SEC'
  | 'MIN'
  | 'HOUR'
  | 'DAY';

// 指标单位格式化选项
export interface MetricUnitFormatOptions {
  // 小数位
  precision?: number;
  // 后缀
  suffix?: string;
  // 空转0
  createEmpty?: number;

  [key: string]: any;
}

// 指标单位格式化函数
type MetricUnitFormatterOption = {
  // 格式化单位
  format: (value: number, option?: MetricUnitFormatOptions) => string;
  // 获取阈值原始值
  getThresholdOriginalValue: (value: number) => number;
};

// 指标单位格式化配置
type WindowTimeFormatterOption = {
  // 单位
  unit: string;
  // 窗口间隔 (ms)
  windowInterval: (window: number) => number;
  // 窗口格式化
  windowFormatter: (window: number) => string;
  // 标签格式化
  labelFormatter: (time: number) => string;
  // 时间格式化
  dateFormatter: (time: number) => string;
};

// 指标单位格式化
export const MetricUnitFormatter: Record<MetricUnitType, MetricUnitFormatterOption> = {
  // 字节
  BYTES: {
    format: formatBytes,
    getThresholdOriginalValue: getByteThresholdOriginalValue,
  },
  // 比特
  BITS: {
    format: formatBits,
    getThresholdOriginalValue: getBitThresholdOriginalValue,
  },
  // 次数
  COUNT: {
    format: formatCount,
    getThresholdOriginalValue: identity,
  },
  // 秒
  SECONDS: {
    format: formatSeconds,
    getThresholdOriginalValue: identity,
  },
  // 百分比
  PER: {
    format: formatPer,
    getThresholdOriginalValue: identity,
  },
  // 字节/秒
  BYTES_S: {
    format: (value, option) => formatBytes(value, option) + '/s',
    getThresholdOriginalValue: getByteThresholdOriginalValue,
  },
  // 比特/秒
  BITS_S: {
    format: (value, option) => formatBits(value, option) + 'ps',
    getThresholdOriginalValue: getBitThresholdOriginalValue,
  },
  // 次数/秒
  COUNT_S: {
    format: (value, option) => formatCount(value, option) + '/s',
    getThresholdOriginalValue: identity,
  },
  // 文本
  TEXT: {
    format: formatText,
    getThresholdOriginalValue: identity,
  },
  // 无单位
  NONE: {
    format: formatText,
    getThresholdOriginalValue: identity,
  },
};

// 窗口单位格式化
export const WindowUnitFormatter: Record<WindowUnit, WindowTimeFormatterOption> = {
  // 秒
  SEC: {
    unit: 's',
    windowInterval: (window: number) => window * 1000,
    windowFormatter: (window: number) => `${window}${WindowUnitFormatter.SEC.unit}`,
    labelFormatter: (time: number) => `${time}秒`,
    dateFormatter: (date: number) => dateFormat(new Date(date), 'mm:ss'),
  },
  // 分钟
  MIN: {
    unit: 'm',
    windowInterval: (window: number) => window * 60 * 1000,
    windowFormatter: (window: number) => `${window}${WindowUnitFormatter.MIN.unit}`,
    labelFormatter: (time: number) => `${time}分钟`,
    dateFormatter: (date: number) => dateFormat(new Date(date), 'HH:mm'),
  },
  // 小时
  HOUR: {
    unit: 'h',
    windowInterval: (window: number) => window * 60 * 60 * 1000,
    windowFormatter: (window: number) => `${window}${WindowUnitFormatter.HOUR.unit}`,
    labelFormatter: (time: number) => `${time}小时`,
    dateFormatter: (date: number) => dateFormat(new Date(date), 'dd mm'),
  },
  // 天
  DAY: {
    unit: 'd',
    windowInterval: (window: number) => window * 24 * 60 * 60 * 1000,
    windowFormatter: (window: number) => `${window}${WindowUnitFormatter.DAY.unit}`,
    labelFormatter: (time: number) => `${time}天`,
    dateFormatter: (date: number) => dateFormat(new Date(date), 'MM-dd'),
  }
};

// 解析窗口单位
export const parseWindowUnit = (windowValue: string): [number, WindowUnit] => {
  const value = Number.parseInt(windowValue);
  const item = Object.entries(WindowUnitFormatter).find((item) => windowValue.includes(item[1].unit));
  if (item) {
    return [value, item[0] as WindowUnit];
  } else {
    return [value, 'MIN'];
  }
};

// 提取单位
export function extractUnit(str: string): string {
  const match = str.match(/[^\d.]+$/);
  return match ? match[0] : '';
}

// 安全取小数位
function getPrecision(option?: MetricUnitFormatOptions, defaultValue = 2): number {
  return typeof option?.precision === 'number' ? option.precision : defaultValue;
}

// 格式化百分比
function formatPer(value: number, option?: MetricUnitFormatOptions): string {
  const fixed = getPrecision(option, 2);
  return parseFloat((value).toFixed(fixed)) + '%';
}

// 格式化字节
export function formatBytes(value: number, option?: MetricUnitFormatOptions): string {
  const fixed = getPrecision(option, 2);
  const units = ['B', 'KB', 'MB', 'GB', 'TB'];
  let v = Math.abs(value);
  let i = 0;
  while (v >= 1024 && i < units.length - 1) {
    v /= 1024;
    i++;
  }
  const signedValue = value < 0 ? -v : v;
  const formattedNum = parseFloat(signedValue.toFixed(i < 3 ? 0 : fixed));
  return `${formattedNum} ${units[i]}`;
}

// 格式化比特
export function formatBits(value: number, option?: MetricUnitFormatOptions): string {
  const fixed = getPrecision(option, 2);
  const units = ['b', 'Kb', 'Mb', 'Gb'];
  let v = Math.abs(value * 8);
  let i = 0;
  while (v >= 1000 && i < units.length - 1) {
    v /= 1000;
    i++;
  }
  const signedValue = value < 0 ? -v : v;
  const formattedNum = parseFloat(signedValue.toFixed(i < 2 ? 0 : fixed));
  return `${formattedNum} ${units[i]}`;
}

// 格式化次数
function formatCount(value: number, option?: MetricUnitFormatOptions): string {
  const fixed = getPrecision(option, 2);
  const abs = Math.abs(value);
  if (abs >= 1_000_000) {
    return parseFloat((value / 1_000_000).toFixed(fixed)) + 'M';
  } else if (abs >= 1_000) {
    return parseFloat((value / 1_000).toFixed(fixed)) + 'K';
  }
  return value.toFixed(0);
}

// 格式化时间
function formatSeconds(value: number, option?: MetricUnitFormatOptions): string {
  const fixed = getPrecision(option, 2);
  if (value >= 3600) {
    return parseFloat((value / 3600).toFixed(fixed)) + 'h';
  } else if (value >= 60) {
    return parseFloat((value / 60).toFixed(fixed)) + 'm';
  }
  return parseFloat(value.toFixed(fixed)) + 's';
}

// 格式化文本
function formatText(value: number, option?: MetricUnitFormatOptions): string {
  const fixed = getPrecision(option, 2);
  const unitText = option?.suffix || '';
  const numStr = value.toFixed(fixed);
  return unitText ? `${numStr} ${unitText}` : numStr;
}

// 获取 byte 的阈值原值 MB > b
function getByteThresholdOriginalValue(value: number) {
  return value * 1024 * 1024;
}

// 获取 bit 的阈值原值 Mb > bit
function getBitThresholdOriginalValue(value: number) {
  return value / 8 * 1000 * 1000;
}

// 返回原值
function identity(value: number): number {
  return value;
}
