import CronParser from 'cron-parser';

// 验证器
export const cronValidator = ({}, value: any) => {
  // 没填写就不校验
  if (!value) {
    return Promise.resolve();
  }
  const values: string[] = value.split(' ').filter((item: any) => !!item);
  if (values.length > 7) {
    return Promise.reject('表达式最多 7 项');
  }
  // 检查第7项
  let val: string = value;
  if (values.length === 7) {
    const year = values[6];
    if (year !== '*' && year !== '?') {
      let yearValues: string[] = [];
      if (year.indexOf('-') >= 0) {
        yearValues = year.split('-');
      } else if (year.indexOf('/')) {
        yearValues = year.split('/');
      } else {
        yearValues = [year];
      }
      // 判断是否都是数字
      const checkYear = yearValues.some((item: any) => isNaN(Number(item)));
      if (checkYear) {
        return Promise.reject('表达式参数[年]错误: ' + year);
      }
    }
    // 取其中的前六项
    val = values.slice(0, 6).join(' ');
  }
  // 6位 没有年, 5位 没有秒年
  try {
    const iter = CronParser.parseExpression(val);
    iter.next();
    return Promise.resolve();
  } catch (e) {
    return Promise.reject('表达式错误: ' + e);
  }
};

export default cronValidator;
