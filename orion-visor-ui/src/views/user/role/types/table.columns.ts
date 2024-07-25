import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';
import { dateFormat } from '@/utils';

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 100,
    align: 'left',
    fixed: 'left',
  }, {
    title: '角色名称',
    dataIndex: 'name',
    slotName: 'name',
    minWidth: 118,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '角色编码',
    dataIndex: 'code',
    slotName: 'code',
    minWidth: 118,
  }, {
    title: '角色状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'center',
    width: 128,
  }, {
    title: '创建时间',
    dataIndex: 'createTime',
    slotName: 'createTime',
    width: 180,
    align: 'center',
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
  }, {
    title: '操作',
    slotName: 'handle',
    width: 198,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
