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
    title: '配置项',
    dataIndex: 'keyName',
    slotName: 'keyName',
    minWidth: 158,
    align: 'left',
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '配置描述',
    dataIndex: 'label',
    slotName: 'label',
    minWidth: 158,
    align: 'left',
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '配置值',
    dataIndex: 'value',
    slotName: 'value',
    minWidth: 158,
    align: 'left',
    ellipsis: true,
    default: true,
  }, {
    title: '额外参数',
    dataIndex: 'extra',
    slotName: 'extra',
    align: 'left',
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '排序',
    dataIndex: 'sort',
    slotName: 'sort',
    align: 'left',
    width: 70,
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
    width: 170,
    align: 'center',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
