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
    width: 74,
    align: 'left',
  }, {
    title: '状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'center',
    width: 106,
  }, {
    title: '留痕地址',
    dataIndex: 'address',
    slotName: 'address',
    width: 156,
    align: 'left',
    ellipsis: true,
  }, {
    title: '连接时间',
    dataIndex: 'connectTime',
    slotName: 'connectTime',
    align: 'left',
    width: 192,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 180,
    align: 'left',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
