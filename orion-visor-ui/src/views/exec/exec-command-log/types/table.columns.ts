import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';

const columns = [
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

export default columns;
