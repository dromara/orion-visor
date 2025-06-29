import type { TableColumnData } from '@arco-design/web-vue';
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
    title: '连接用户',
    dataIndex: 'username',
    slotName: 'username',
    width: 140,
    align: 'left',
    ellipsis: true,
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
    width: 116,
    align: 'left',
  }, {
    title: '留痕地址',
    dataIndex: 'address',
    slotName: 'address',
    width: 156,
    align: 'left',
    ellipsis: true,
  }, {
    title: '开始时间',
    dataIndex: 'startTime',
    slotName: 'startTime',
    align: 'center',
    width: 188,
    render: ({ record }) => {
      return (record.startTime && dateFormat(new Date(record.startTime)));
    },
  }, {
    title: '操作',
    slotName: 'handle',
    width: 208,
    align: 'left',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
