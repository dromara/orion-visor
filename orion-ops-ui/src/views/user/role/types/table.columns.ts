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
    title: '角色名称',
    dataIndex: 'name',
    slotName: 'name',
  }, {
    title: '角色编码',
    dataIndex: 'code',
    slotName: 'code',
  }, {
    title: '角色状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'center',
  }, {
    title: '创建时间',
    dataIndex: 'createTime',
    slotName: 'createTime',
    align: 'center',
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
  }, {
    title: '操作',
    slotName: 'handle',
    width: 240,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
