import { TableColumnData } from '@arco-design/web-vue/es/table/interface';

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 70,
    align: 'left',
    fixed: 'left',
  }, {
    title: '主机名称',
    dataIndex: 'name',
    slotName: 'name',
    align: 'center',
  }, {
    title: '主机编码',
    dataIndex: 'code',
    slotName: 'code',
    align: 'center',
  }, {
    title: '主机地址',
    dataIndex: 'address',
    slotName: 'address',
    align: 'center',
  }, {
    title: '标签',
    dataIndex: 'tag',
    slotName: 'tag',
    align: 'left',
  }, {
    title: '操作',
    slotName: 'handle',
    width: 180,
    align: 'right',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
