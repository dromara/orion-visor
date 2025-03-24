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
    title: '名称',
    dataIndex: 'name',
    slotName: 'name',
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '用户名',
    dataIndex: 'username',
    slotName: 'username',
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '类型',
    dataIndex: 'type',
    slotName: 'type',
    width: 138,
    default: true,
  }, {
    title: '主机密钥',
    dataIndex: 'keyId',
    slotName: 'keyId',
    width: 180,
    default: true,
  }, {
    title: '描述',
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
    width: 130,
    align: 'center',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
