import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';
import { dateFormat } from '@/utils';

// 主机列
export const hostColumns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 68,
    align: 'left',
    fixed: 'left',
  }, {
    title: '主机类型',
    dataIndex: 'type',
    slotName: 'type',
    align: 'center',
    width: 100,
  }, {
    title: '主机名称',
    dataIndex: 'name',
    slotName: 'name',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '主机地址',
    dataIndex: 'address',
    slotName: 'address',
    ellipsis: true,
    tooltip: true,
  },
] as TableColumnData[];

// 主机密钥列
export const hostKeyColumns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 100,
    align: 'left',
    fixed: 'left',
  }, {
    title: '名称',
    dataIndex: 'name',
    slotName: 'name',
    ellipsis: true,
    tooltip: true
  }, {
    title: '描述',
    dataIndex: 'description',
    slotName: 'description',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '创建时间',
    dataIndex: 'createTime',
    slotName: 'createTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
  },
] as TableColumnData[];

// 主机身份列
export const hostIdentityColumns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 70,
    align: 'left',
    fixed: 'left',
  }, {
    title: '名称',
    dataIndex: 'name',
    slotName: 'name',
    ellipsis: true,
    tooltip: true
  }, {
    title: '类型',
    dataIndex: 'type',
    slotName: 'type',
    width: 98,
  }, {
    title: '用户名',
    dataIndex: 'username',
    slotName: 'username',
    ellipsis: true,
    tooltip: true
  }, {
    title: '主机密钥',
    dataIndex: 'keyId',
    slotName: 'keyId',
  }, {
    title: '描述',
    dataIndex: 'description',
    slotName: 'description',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '创建时间',
    dataIndex: 'createTime',
    slotName: 'createTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
  },
] as TableColumnData[];
