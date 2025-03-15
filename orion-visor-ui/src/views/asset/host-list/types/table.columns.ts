import type { TableColumnData } from '@arco-design/web-vue';

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 68,
    align: 'left',
    fixed: 'left',
  }, {
    title: '主机名称',
    dataIndex: 'name',
    slotName: 'name',
    ellipsis: true,
    tooltip: true,
    minWidth: 238,
  }, {
    title: '主机编码',
    dataIndex: 'code',
    slotName: 'code',
    minWidth: 120,
  }, {
    title: '主机类型',
    dataIndex: 'type',
    slotName: 'type',
    align: 'center',
    width: 100,
  }, {
    title: '主机地址',
    dataIndex: 'address',
    slotName: 'address',
    minWidth: 238
  }, {
    title: '主机标签',
    dataIndex: 'tags',
    slotName: 'tags',
    align: 'left',
    minWidth: 148,
  }, {
    title: '主机状态',
    dataIndex: 'status',
    slotName: 'status',
    width: 88,
    align: 'center',
    fixed: 'right',
  }, {
    title: '主机描述',
    dataIndex: 'description',
    slotName: 'description',
    minWidth: 128,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 192,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
