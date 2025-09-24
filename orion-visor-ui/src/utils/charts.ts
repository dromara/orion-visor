// 获取百分比进度颜色
export const getPercentProgressColor = (percent: number, defaultColor = 'rgb(var(--green-6))') => {
  try {
    if (percent < 0.6) {
      return defaultColor;
    } else if (percent < 0.8) {
      return 'rgb(var(--orange-6))';
    } else {
      return 'rgb(var(--red-6))';
    }
  } catch (e) {
    return defaultColor;
  }
};
