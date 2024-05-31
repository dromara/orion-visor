import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';
import { isNumber } from '@/utils/is';

const columns = [
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

export default columns;
