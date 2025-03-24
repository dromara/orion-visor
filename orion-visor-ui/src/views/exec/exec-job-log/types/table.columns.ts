import type { TableColumnData } from '@arco-design/web-vue';

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 100,
    align: 'left',
    fixed: 'left',
    default: true,
  }, {
    title: '任务名称',
    dataIndex: 'description',
    slotName: 'description',
    align: 'left',
    width: 188,
    ellipsis: true,
    default: true,
  }, {
    title: '执行命令',
    dataIndex: 'command',
    slotName: 'command',
    align: 'left',
    ellipsis: true,
    minWidth: 238,
    default: true,
  }, {
    title: '执行用户',
    dataIndex: 'username',
    slotName: 'username',
    align: 'left',
    width: 128,
    ellipsis: true,
    default: true,
  }, {
    title: '执行状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'left',
    width: 118,
    default: true,
  }, {
    title: '执行时间',
    dataIndex: 'startTime',
    slotName: 'startTime',
    align: 'left',
    width: 190,
    default: true,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 218,
    align: 'center',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
