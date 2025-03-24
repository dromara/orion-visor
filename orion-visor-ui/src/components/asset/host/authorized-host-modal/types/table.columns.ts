import type { TableColumnData } from '@arco-design/web-vue';

const columns = [
  {
    title: '主机名称',
    dataIndex: 'name',
    slotName: 'name',
    ellipsis: true,
    tooltip: true
  }, {
    title: '主机地址',
    dataIndex: 'address',
    slotName: 'address',
  }, {
    title: '主机标签',
    dataIndex: 'tags',
    slotName: 'tags',
    align: 'left',
  },
] as TableColumnData[];

export default columns;
