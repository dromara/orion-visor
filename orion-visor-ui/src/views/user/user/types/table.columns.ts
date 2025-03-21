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
    title: '用户名',
    dataIndex: 'username',
    slotName: 'username',
    minWidth: 138,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '花名',
    dataIndex: 'nickname',
    slotName: 'nickname',
    minWidth: 138,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '手机号',
    dataIndex: 'mobile',
    slotName: 'mobile',
    minWidth: 88,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '邮箱',
    dataIndex: 'email',
    slotName: 'email',
    minWidth: 88,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '用户描述',
    dataIndex: 'description',
    slotName: 'description',
    minWidth: 128,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '用户状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'center',
    width: 128,
    default: true,
  }, {
    title: '最后登录时间',
    dataIndex: 'lastLoginTime',
    slotName: 'lastLoginTime',
    width: 180,
    align: 'center',
    render: ({ record }) => {
      return record.lastLoginTime && dateFormat(new Date(record.lastLoginTime));
    },
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
    width: 298,
    align: 'center',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
