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
    title: '连接用户',
    dataIndex: 'username',
    slotName: 'username',
    width: 140,
    align: 'left',
    ellipsis: true,
    default: true,
  }, {
    title: '连接主机',
    dataIndex: 'hostName',
    slotName: 'hostName',
    align: 'left',
    ellipsis: true,
    default: true,
  }, {
    title: '类型',
    dataIndex: 'type',
    slotName: 'type',
    width: 116,
    align: 'left',
    default: true,
  }, {
    title: '状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'left',
    width: 118,
    default: true,
  }, {
    title: '留痕地址',
    dataIndex: 'address',
    slotName: 'address',
    width: 156,
    align: 'left',
    ellipsis: true,
    default: true,
  }, {
    title: '连接时间',
    dataIndex: 'connectTime',
    slotName: 'connectTime',
    align: 'left',
    width: 192,
    default: true,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 218,
    align: 'left',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
