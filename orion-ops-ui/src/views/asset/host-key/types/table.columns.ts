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
    title: '名称',
    dataIndex: 'name',
    slotName: 'name',
    align: 'center',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '公钥文本',
    dataIndex: 'publicKey',
    slotName: 'publicKey',
    align: 'center',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '私钥文本',
    dataIndex: 'privateKey',
    slotName: 'privateKey',
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
    title: '创建时间',
    dataIndex: 'createTime',
    slotName: 'createTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
  }, {
    title: '修改时间',
    dataIndex: 'updateTime',
    slotName: 'updateTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.updateTime));
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
