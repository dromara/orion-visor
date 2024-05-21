export const cronEmits = ['change', 'update:modelValue'];

// cron 参数类型
export interface CronPropType {
  modelValue: string;
  disabled: boolean;
  hideSecond: boolean;
  hideYear: boolean;
  placeholder: string;
  callback: (expression: string, timestamp: number, validated: boolean) => void;
}

// cron 参数默认值
export const cronDefaultProps: Partial<CronPropType> = {
  disabled: false,
  hideSecond: false,
  hideYear: false,
  placeholder: '请输入 cron 表达式',
};

/**
 * 转化为 quartz 周
 * 1 = 周日
 * 2 = 周一
 * 3 = 周二
 * 4 = 周三
 * 5 = 周四
 * 6 = 周五
 * 7 = 周六
 */
export const convertWeekToQuartz = (week: string) => {
  const convert = (v: string) => {
    if (v === '0') {
      return '1';
    }
    if (v === '1') {
      return '0';
    }
    return (Number.parseInt(v) - 1).toString();
  };
  // 匹配示例 1-7 or 1/7
  const patten1 = /^([0-7])([-/])([0-7])$/;
  // 匹配示例 1,4,7
  const patten2 = /^([0-7])(,[0-7])+$/;
  if (/^[0-7]$/.test(week)) {
    return convert(week);
  } else if (patten1.test(week)) {
    return week.replace(patten1, ($0, before, separator, after) => {
      if (separator === '/') {
        return convert(before) + separator + after;
      } else {
        return convert(before) + separator + convert(after);
      }
    });
  } else if (patten2.test(week)) {
    return week.split(',')
      .map((v) => convert(v))
      .join(',');
  }
  return week;
};
