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
    title: '模板名称',
    dataIndex: 'name',
    slotName: 'name',
    align: 'left',
    width: 200,
    ellipsis: true,
  }, {
    title: '模板命令',
    dataIndex: 'command',
    slotName: 'command',
    align: 'left',
    ellipsis: true,
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
    width: 180,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
