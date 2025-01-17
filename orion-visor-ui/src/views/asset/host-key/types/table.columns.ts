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
    title: '名称',
    dataIndex: 'name',
    slotName: 'name',
    ellipsis: true,
    tooltip: true
  }, {
    title: '描述',
    dataIndex: 'description',
    slotName: 'description',
    minWidth: 128,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '创建时间',
    dataIndex: 'createTime',
    slotName: 'createTime',
    align: 'center',
    width: 198,
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
  }, {
    title: '修改时间',
    dataIndex: 'updateTime',
    slotName: 'updateTime',
    align: 'center',
    width: 198,
    render: ({ record }) => {
      return dateFormat(new Date(record.updateTime));
    },
  }, {
    title: '操作',
    slotName: 'handle',
    width: 180,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
