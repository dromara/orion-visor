import { TableColumnData } from '@arco-design/web-vue/es/table/interface';
import { dateFormat } from '@/utils';

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 70,
    align: 'left',
    fixed: 'left',
  }, {
    title: '用户名',
    dataIndex: 'username',
    slotName: 'username',
    align: 'center',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '密码',
    dataIndex: 'password',
    slotName: 'password',
    align: 'center',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '花名',
    dataIndex: 'nickname',
    slotName: 'nickname',
    align: 'center',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '头像地址',
    dataIndex: 'avatar',
    slotName: 'avatar',
    align: 'center',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '手机号',
    dataIndex: 'mobile',
    slotName: 'mobile',
    align: 'center',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '邮箱',
    dataIndex: 'email',
    slotName: 'email',
    align: 'center',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '用户状态 0停用 1启用 2锁定',
    dataIndex: 'status',
    slotName: 'status',
    align: 'center',
  }, {
    title: '最后登录时间',
    dataIndex: 'lastLoginTime',
    slotName: 'lastLoginTime',
    align: 'center',
    render: ({ record }) => {
      return record.lastLoginTime && dateFormat(new Date(record.lastLoginTime));
    },
  }, {
    title: '创建时间',
    dataIndex: 'createTime',
    slotName: 'createTime',
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
  }, {
    title: '修改时间',
    dataIndex: 'updateTime',
    slotName: 'updateTime',
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
  }, {
    title: '创建人',
    dataIndex: 'creator',
    slotName: 'creator',
  }, {
    title: '修改人',
    dataIndex: 'updater',
    slotName: 'updater',
  }, {
    title: '操作',
    slotName: 'handle',
    width: 130,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
