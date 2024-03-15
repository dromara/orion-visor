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
    title: '修改前',
    dataIndex: 'beforeValue',
    slotName: 'beforeValue',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '修改后',
    dataIndex: 'afterValue',
    slotName: 'afterValue',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '修改时间',
    dataIndex: 'createTime',
    slotName: 'createTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
  }, {
    title: '修改人',
    dataIndex: 'creator',
    slotName: 'creator',
    width: 80,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 80,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
