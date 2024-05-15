import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';
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
    ellipsis: true,
    tooltip: true
  }, {
    title: '类型',
    dataIndex: 'type',
    slotName: 'type',
    width: 138,
  }, {
    title: '用户名',
    dataIndex: 'username',
    slotName: 'username',
  }, {
    title: '主机秘钥',
    dataIndex: 'keyId',
    slotName: 'keyId',
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
    title: '操作',
    slotName: 'handle',
    width: 130,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
