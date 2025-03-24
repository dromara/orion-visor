import type { TableColumnData } from '@arco-design/web-vue';

const columns = [
  {
    title: '菜单名称',
    dataIndex: 'menuName',
    slotName: 'menuName',
    fixed: 'left',
    width: 250,
  }, {
    title: '图标',
    dataIndex: 'icon',
    slotName: 'icon',
    align: 'center',
    width: 60,
  }, {
    title: '类型',
    dataIndex: 'type',
    slotName: 'type',
    width: 68,
  }, {
    title: '排序',
    dataIndex: 'sort',
    slotName: 'sort',
    width: 70,
  }, {
    title: '状态',
    dataIndex: 'status',
    slotName: 'status',
    width: 120,
  }, {
    title: '权限标识',
    dataIndex: 'permission',
    slotName: 'permission',
    minWidth: 168,
    ellipsis: true,
    tooltip: true
  }, {
    title: '组件名称',
    dataIndex: 'component',
    slotName: 'component',
    minWidth: 218,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '链接路径',
    dataIndex: 'path',
    slotName: 'path',
    width: 168,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 168,
    fixed: 'right',
  }
] as TableColumnData[];

export default columns;
