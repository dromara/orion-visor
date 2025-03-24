import type { TableColumnData } from '@arco-design/web-vue';
import { dateFormat } from '@/utils';

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 68,
    align: 'left',
    fixed: 'left',
    default: true,
  }, {
    title: '主机名称',
    dataIndex: 'name',
    slotName: 'name',
    ellipsis: true,
    tooltip: true,
    minWidth: 238,
    default: true,
  }, {
    title: '主机编码',
    dataIndex: 'code',
    slotName: 'code',
    minWidth: 120,
    default: true,
  }, {
    title: '主机类型',
    dataIndex: 'type',
    slotName: 'type',
    align: 'center',
    width: 100,
    default: true,
  }, {
    title: '主机地址',
    dataIndex: 'address',
    slotName: 'address',
    minWidth: 238,
    default: true,
  }, {
    title: '主机标签',
    dataIndex: 'tags',
    slotName: 'tags',
    align: 'left',
    minWidth: 148,
    default: true,
  }, {
    title: '主机状态',
    dataIndex: 'status',
    slotName: 'status',
    width: 88,
    align: 'center',
    fixed: 'right',
    default: true,
  }, {
    title: '主机描述',
    dataIndex: 'description',
    slotName: 'description',
    minWidth: 128,
    ellipsis: true,
    tooltip: true,
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
    default: false,
  }, {
    title: '修改时间',
    dataIndex: 'updateTime',
    slotName: 'updateTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.updateTime));
    },
    default: false,
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
    width: 192,
    align: 'center',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
