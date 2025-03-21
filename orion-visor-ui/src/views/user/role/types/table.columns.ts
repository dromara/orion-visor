import type { TableColumnData } from '@arco-design/web-vue';
import { dateFormat } from '@/utils';

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
    title: '角色名称',
    dataIndex: 'name',
    slotName: 'name',
    minWidth: 118,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '角色编码',
    dataIndex: 'code',
    slotName: 'code',
    minWidth: 118,
    default: true,
  }, {
    title: '角色描述',
    dataIndex: 'description',
    slotName: 'description',
    minWidth: 128,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '角色状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'center',
    width: 128,
    default: true,
  }, {
    title: '创建时间',
    dataIndex: 'createTime',
    slotName: 'createTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
    default: true,
  }, {
    title: '修改时间',
    dataIndex: 'updateTime',
    slotName: 'updateTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.updateTime));
    },
    default: true,
  }, {
    title: '创建人',
    dataIndex: 'creator',
    slotName: 'creator',
    width: 148,
    ellipsis: true,
    tooltip: true,
    default: false,
  }, {
    title: '修改人',
    dataIndex: 'updater',
    slotName: 'updater',
    width: 148,
    ellipsis: true,
    tooltip: true,
    default: false,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 198,
    align: 'center',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
