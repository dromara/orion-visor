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
    ellipsis: true,
    tooltip: true,
  }, {
    title: '花名',
    dataIndex: 'nickname',
    slotName: 'nickname',
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
    ellipsis: true,
    tooltip: true,
  }, {
    title: '用户状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'center',
  }, {
    title: '最后登录时间',
    dataIndex: 'lastLoginTime',
    slotName: 'lastLoginTime',
    width: 180,
    align: 'center',
    render: ({ record }) => {
      return record.lastLoginTime && dateFormat(new Date(record.lastLoginTime));
    },
  }, {
    title: '操作',
    slotName: 'handle',
    width: 290,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
