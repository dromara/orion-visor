// 获取百分比进度状态
export const getPercentProgressColor = (percent: number) => {
  if (percent < 0.6) {
    return 'rgb(var(--green-6))';
  } else if (percent < 0.8) {
    return 'rgb(var(--orange-6))';
  } else {
    return 'rgb(var(--red-6))';
  }
};
