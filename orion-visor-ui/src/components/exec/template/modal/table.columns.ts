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
    title: '模板名称',
    dataIndex: 'name',
    slotName: 'name',
    align: 'left',
    width: 250,
    ellipsis: true,
  }, {
    title: '模板命令',
    dataIndex: 'command',
    slotName: 'command',
    align: 'left',
    ellipsis: true,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 80,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
