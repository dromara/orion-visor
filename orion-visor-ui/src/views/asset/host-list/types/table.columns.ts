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
    title: '主机信息',
    dataIndex: 'hostInfo',
    slotName: 'hostInfo',
    minWidth: 328,
    align: 'left',
    fixed: 'left',
    default: true,
  }, {
    title: '主机规格',
    dataIndex: 'hostSpec',
    slotName: 'hostSpec',
    width: 188,
    align: 'left',
    default: true,
  }, {
    title: '主机协议',
    dataIndex: 'protocols',
    slotName: 'protocols',
    align: 'left',
    width: 184,
    default: true,
  }, {
    title: '主机状态',
    dataIndex: 'status',
    slotName: 'status',
    width: 88,
    align: 'center',
    default: true,
  }, {
    title: '主机分组',
    dataIndex: 'groups',
    slotName: 'groups',
    align: 'left',
    minWidth: 148,
    default: true,
  }, {
    title: '主机标签',
    dataIndex: 'tags',
    slotName: 'tags',
    align: 'left',
    minWidth: 148,
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
    width: 162,
    align: 'center',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
