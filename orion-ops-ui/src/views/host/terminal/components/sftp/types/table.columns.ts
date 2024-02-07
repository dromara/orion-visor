import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';

// 表格列
const columns = [
  {
    title: '名称',
    dataIndex: 'name',
    slotName: 'fileName',
    ellipsis: true,
    tooltip: true,
    sortable: {
      sortDirections: ['ascend', 'descend'],
    },
    filterable: {
      filter: (value, record) => record.name.includes(value),
      slotName: 'nameFilter',
    }
  }, {
    title: '大小',
    dataIndex: 'sizeByte',
    slotName: 'size',
    sortable: {
      sortDirections: ['ascend', 'descend'],
    },
  }, {
    title: '属性',
    dataIndex: 'attr',
    slotName: 'attr',
  }, {
    title: '修改时间',
    dataIndex: 'modifyTime',
    slotName: 'modifyTime',
    align: 'center',
    sortable: {
      sortDirections: ['ascend', 'descend'],
    },
    cellClass: 'action-cell',
  },
] as TableColumnData[];

export default columns;
