import type { TableColumnData } from '@arco-design/web-vue';

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 100,
    align: 'left',
    fixed: 'left',
  }, {
    title: '配置项',
    dataIndex: 'keyName',
    slotName: 'keyName',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '配置描述',
    dataIndex: 'description',
    slotName: 'description',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '类型',
    dataIndex: 'valueType',
    slotName: 'valueType',
    align: 'left',
    width: 150
  }, {
    title: '额外配置',
    dataIndex: 'extraSchema',
    slotName: 'extraSchema',
    align: 'left',
  }, {
    title: '操作',
    slotName: 'handle',
    width: 170,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
