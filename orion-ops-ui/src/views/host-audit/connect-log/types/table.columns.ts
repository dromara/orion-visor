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
    title: '连接用户',
    dataIndex: 'username',
    slotName: 'username',
    width: 180,
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '连接主机',
    dataIndex: 'hostName',
    slotName: 'hostName',
    align: 'left',
    ellipsis: true,
  }, {
    title: '类型',
    dataIndex: 'type',
    slotName: 'type',
    width: 74,
    align: 'left',
  }, {
    title: '状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'left',
    width: 96,
  }, {
    title: '连接时间',
    dataIndex: 'connectTime',
    slotName: 'connectTime',
    align: 'left',
    width: 318,
    render: ({ record }) => {
      return (record.startTime && dateFormat(new Date(record.startTime)))
        + ' - '
        + (record.endTime && dateFormat(new Date(record.endTime)) || '现在');
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
