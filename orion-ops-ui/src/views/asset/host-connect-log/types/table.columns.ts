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
    title: '用户id',
    dataIndex: 'userId',
    slotName: 'userId',
    width: 110,
    align: 'left',
  }, {
    title: '主机id',
    dataIndex: 'hostId',
    slotName: 'hostId',
    width: 110,
    align: 'left',
  }, {
    title: '主机名称',
    dataIndex: 'hostName',
    slotName: 'hostName',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '主机地址',
    dataIndex: 'hostAddress',
    slotName: 'hostAddress',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: 'token',
    dataIndex: 'token',
    slotName: 'token',
    align: 'left',
    width: 180,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '开始时间',
    dataIndex: 'startTime',
    slotName: 'startTime',
    align: 'left',
    width: 180,
    render: ({ record }) => {
      return record.startTime && dateFormat(new Date(record.startTime));
    },
  }, {
    title: '结束时间',
    dataIndex: 'endTime',
    slotName: 'endTime',
    align: 'left',
    width: 180,
    render: ({ record }) => {
      return record.endTime && dateFormat(new Date(record.endTime));
    },
  },
] as TableColumnData[];

export default columns;
