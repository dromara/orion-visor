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
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '命令',
    dataIndex: 'command',
    slotName: 'command',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '超时时间',
    dataIndex: 'timeout',
    slotName: 'timeout',
    align: 'left',
  }, {
    title: '参数',
    dataIndex: 'parameter',
    slotName: 'parameter',
    align: 'left',
    ellipsis: true,
    tooltip: true,
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
