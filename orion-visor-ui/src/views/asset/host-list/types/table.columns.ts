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
    title: '主机名称',
    dataIndex: 'name',
    slotName: 'name',
    ellipsis: true,
    tooltip: true
  }, {
    title: '主机编码',
    dataIndex: 'code',
    slotName: 'code',
  }, {
    title: '主机地址',
    dataIndex: 'address',
    slotName: 'address',
  }, {
    title: '主机标签',
    dataIndex: 'tags',
    slotName: 'tags',
    align: 'left',
  }, {
    title: '操作',
    slotName: 'handle',
    width: 162,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
