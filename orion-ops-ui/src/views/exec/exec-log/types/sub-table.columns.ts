import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';

const columns = [
  {
    title: '执行主机',
    dataIndex: 'hostName',
    slotName: 'hostName',
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
    tooltip: true,
  }, {
    title: '执行状态1',
    dataIndex: 'status1',
    slotName: 'status1',
    align: 'left',
    width: 118,
  }, {
    title: '执行时间',
    dataIndex: 'startTime',
    slotName: 'startTime',
    align: 'left',
    width: 180,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 130,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
