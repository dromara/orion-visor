import type { TableColumnData } from '@arco-design/web-vue';
import { dateFormat } from '@/utils';

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 68,
    align: 'left',
    fixed: 'left',
    default: true,
  }, {
    title: '通知名称',
    dataIndex: 'name',
    slotName: 'name',
    align: 'left',
    width: 168,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '渠道类型',
    dataIndex: 'channelType',
    slotName: 'channelType',
    align: 'left',
    width: 128,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '消息标识',
    dataIndex: 'messageTag',
    slotName: 'messageTag',
    align: 'left',
    width: 468,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '通知描述',
    dataIndex: 'description',
    slotName: 'description',
    align: 'left',
    minWidth: 168,
    ellipsis: true,
    tooltip: true,
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
    width: 148,
    dataIndex: 'creator',
    slotName: 'creator',
  }, {
    title: '修改人',
    width: 148,
    dataIndex: 'updater',
    slotName: 'updater',
    default: true,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 168,
    align: 'center',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
