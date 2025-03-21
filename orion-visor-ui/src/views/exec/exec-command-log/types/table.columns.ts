import type { TableColumnData } from '@arco-design/web-vue';
import { isNumber } from '@/utils/is';

// 表格列
export const logColumns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 100,
    align: 'left',
    fixed: 'left',
  }, {
    title: '执行描述',
    dataIndex: 'description',
    slotName: 'description',
    align: 'left',
    width: 168,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '执行命令',
    dataIndex: 'command',
    slotName: 'command',
    align: 'left',
    ellipsis: true,
    minWidth: 238,
  }, {
    title: '执行用户',
    dataIndex: 'username',
    slotName: 'username',
    align: 'left',
    width: 118,
    ellipsis: true,
  }, {
    title: '执行状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'left',
    width: 118,
  }, {
    title: '执行时间',
    dataIndex: 'startTime',
    slotName: 'startTime',
    align: 'left',
    width: 190,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 288,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

// 主机列
export const hostColumns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 100,
    align: 'left',
    fixed: 'left',
  }, {
    title: '执行主机',
    dataIndex: 'hostName',
    slotName: 'hostName',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '退出码',
    dataIndex: 'exitCode',
    slotName: 'exitCode',
    align: 'left',
    width: 118,
    render: ({ record }) => {
      return isNumber(record.exitCode) ? record.exitCode : '-';
    },
  }, {
    title: '执行状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'left',
    width: 118,
  }, {
    title: '错误信息',
    dataIndex: 'errorMessage',
    slotName: 'errorMessage',
    align: 'left',
    ellipsis: true,
    tooltip: true,
    width: 168,
  }, {
    title: '执行时间',
    dataIndex: 'startTime',
    slotName: 'startTime',
    align: 'left',
    width: 190,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 258,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];
