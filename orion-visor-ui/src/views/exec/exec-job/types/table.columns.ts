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
    default: true,
  }, {
    title: '任务名称',
    dataIndex: 'name',
    slotName: 'name',
    align: 'left',
    width: 180,
    ellipsis: true,
    default: true,
  }, {
    title: 'cron',
    dataIndex: 'expression',
    slotName: 'expression',
    align: 'left',
    width: 168,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '执行命令',
    dataIndex: 'command',
    slotName: 'command',
    align: 'left',
    minWidth: 238,
    ellipsis: true,
    default: true,
  }, {
    title: '执行用户',
    dataIndex: 'execUsername',
    slotName: 'execUsername',
    align: 'left',
    ellipsis: true,
    tooltip: true,
    width: 124,
    default: true,
  }, {
    title: '任务状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'center',
    width: 112,
    default: true,
  }, {
    title: '最近执行',
    dataIndex: 'recentLog',
    slotName: 'recentLog',
    align: 'left',
    headerCellStyle: {
      display: 'flex',
      justifyContent: 'center'
    },
    width: 200,
    default: true,
  }, {
    title: '创建时间',
    dataIndex: 'createTime',
    slotName: 'createTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
    default: false,
  }, {
    title: '修改时间',
    dataIndex: 'updateTime',
    slotName: 'updateTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.updateTime));
    },
    default: true,
  }, {
    title: '创建人',
    dataIndex: 'creator',
    slotName: 'creator',
    width: 148,
    ellipsis: true,
    tooltip: true,
    default: false,
  }, {
    title: '修改人',
    dataIndex: 'updater',
    slotName: 'updater',
    width: 148,
    ellipsis: true,
    tooltip: true,
    default: false,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 228,
    align: 'center',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
