export default {};

/**
 * 是否生成打包报告
 */
export function isReportMode(): boolean {
  return process.env.REPORT === 'true';
}

/**
 * 是否为生产模式
 */
export function isProductionMode(): boolean {
  return process.env.NODE_ENV === 'production';
}
