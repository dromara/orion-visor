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
    title: '指标名称',
    dataIndex: 'name',
    slotName: 'name',
    align: 'left',
    width: 238,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '数据集',
    dataIndex: 'measurement',
    slotName: 'measurement',
    align: 'left',
    width: 148,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '指标项',
    dataIndex: 'value',
    slotName: 'value',
    align: 'left',
    minWidth: 288,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '指标单位',
    dataIndex: 'unit',
    slotName: 'unit',
    align: 'left',
    width: 168,
    default: true,
  }, {
    title: '指标描述',
    dataIndex: 'description',
    slotName: 'description',
    align: 'left',
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
    dataIndex: 'creator',
    slotName: 'creator',
    width: 148,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '修改人',
    dataIndex: 'updater',
    slotName: 'updater',
    width: 148,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '操作',
    slotName: 'handle',
    width: 130,
    align: 'center',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
