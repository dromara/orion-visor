import type { OperatorLogQueryResponse } from '@/api/user/operator-log';
import { pick } from 'lodash';
import { dateFormat } from '@/utils';

// 表名称
export const TableName = 'opt-log';

// 结果状态
export const ResultStatus = {
  // 失败
  FAILED: 0,
  // 成功
  SUCCESS: 1,
};

// 获取日志详情
export const getLogDetail = (record: OperatorLogQueryResponse): Record<string, any> => {
  try {
    const detail = Object.assign({} as Record<string, any>,
      pick(record, 'traceId', 'address', 'location',
        'userAgent', 'errorMessage'));
    detail.duration = `${record.duration} ms`;
    detail.startTime = dateFormat(new Date(record.startTime));
    detail.endTime = dateFormat(new Date(record.endTime));
    detail.extra = JSON.parse(record?.extra);
    detail.returnValue = JSON.parse(record?.returnValue);
    return detail;
  } catch (e) {
    return record;
  }
};

// 最大清理数量
export const maxClearLimit = 2000;

// 操作日志模块 字典项
export const operatorLogModuleKey = 'operatorLogModule';

// 操作日志类型 字典项
export const operatorLogTypeKey = 'operatorLogType';

// 操作风险等级 字典项
export const operatorRiskLevelKey = 'operatorRiskLevel';

// 操作日志结果 字典项
export const operatorLogResultKey = 'operatorLogResult';

// 加载的字典值
export const dictKeys = [operatorLogModuleKey, operatorLogTypeKey, operatorRiskLevelKey, operatorLogResultKey];
