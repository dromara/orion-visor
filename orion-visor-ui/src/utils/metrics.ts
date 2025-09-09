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
  digit?: number;
  // 后缀
  suffix?: string;
  // 空转0
  createEmpty?: number;

  [key: string]: any;
}

// 指标单位格式化函数
type MetricUnitFormatterFn = (value: number, option?: MetricUnitFormatOptions) => string;

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
export const MetricUnitFormatter: Record<MetricUnitType, MetricUnitFormatterFn> = {
  // 字节
  BYTES: formatBytes,
  // 比特
  BITS: formatBits,
  // 次数
  COUNT: formatCount,
  // 秒
  SECONDS: formatSeconds,
  // 百分比
  PER: formatPer,
  // 字节/秒
  BYTES_S: (value, option) => formatBytes(value, option) + '/s',
  // 比特/秒
  BITS_S: (value, option) => formatBits(value, option) + 'ps',
  // 次数/秒
  COUNT_S: (value, option) => formatCount(value, option) + '/s',
  // 文本
  TEXT: formatText,
  // 无单位
  NONE: (value, option) => formatNumber(value, option),
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

// 安全取小数位
function getFixed(option?: MetricUnitFormatOptions, defaultValue = 2): number {
  return typeof option?.digit === 'number' ? option.digit : defaultValue;
}

// 格式化数字
function formatNumber(value: number, option?: MetricUnitFormatOptions): string {
  const fixed = getFixed(option, 2);
  const abs = Math.abs(value);
  let result: string;

  if (abs >= 1e9) {
    result = (value / 1e9).toFixed(fixed);
  } else if (abs >= 1_000_000) {
    result = (value / 1_000_000).toFixed(fixed);
  } else if (abs >= 1_000) {
    result = (value / 1_000).toFixed(fixed);
  } else {
    result = value.toFixed(fixed);
  }

  return parseFloat(result).toString();
}

// 格式化百分比
function formatPer(value: number, option?: MetricUnitFormatOptions): string {
  const fixed = getFixed(option, 2);
  return parseFloat((value).toFixed(fixed)) + '%';
}

// 格式化字节
function formatBytes(value: number, option?: MetricUnitFormatOptions): string {
  const fixed = getFixed(option, 2);
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
function formatBits(value: number, option?: MetricUnitFormatOptions): string {
  const fixed = getFixed(option, 2);
  const units = ['b', 'Kb', 'Mb', 'Gb'];
  let v = Math.abs(value);
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
  const fixed = getFixed(option, 2);
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
  const fixed = getFixed(option, 2);
  if (value >= 3600) {
    return parseFloat((value / 3600).toFixed(fixed)) + 'h';
  } else if (value >= 60) {
    return parseFloat((value / 60).toFixed(fixed)) + 'm';
  }
  return parseFloat(value.toFixed(fixed)) + 's';
}

// 格式化文本
function formatText(value: number, option?: MetricUnitFormatOptions): string {
  const fixed = getFixed(option, 2);
  const unitText = option?.suffix || '';
  const numStr = value.toFixed(fixed);
  return unitText ? `${numStr} ${unitText}` : numStr;
}
