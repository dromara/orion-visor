import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';
import { dateFormat } from '@/utils';

// 终端日志列
export const terminalLogColumns = [
  {
    title: '连接主机',
    dataIndex: 'hostName',
    slotName: 'hostName',
    align: 'left',
    ellipsis: true,
  }, {
    title: '类型',
    dataIndex: 'type',
    slotName: 'type',
    width: 88,
    align: 'left',
  }, {
    title: '连接时间',
    dataIndex: 'startTime',
    slotName: 'startTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.startTime));
    },
  }, {
    title: '操作',
    slotName: 'handle',
    width: 92,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

// 批量执行列
export const batchExecColumns = [
  {
    title: '执行描述',
    dataIndex: 'description',
    slotName: 'description',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '执行状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'left',
    width: 108,
  }, {
    title: '执行时间',
    dataIndex: 'startTime',
    slotName: 'startTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.startTime));
    },
  }, {
    title: '操作',
    slotName: 'handle',
    width: 92,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

// 用户登录日志列
export const userLoginColumns = [
  {
    title: '登录设备',
    dataIndex: 'content',
    slotName: 'content',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '登录结果',
    dataIndex: 'result',
    slotName: 'result',
    align: 'center',
    width: 90,
  }, {
    title: '登录时间',
    dataIndex: 'createTime',
    slotName: 'createTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
  },
] as TableColumnData[];
