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
    title: '策略名称',
    dataIndex: 'name',
    slotName: 'name',
    align: 'left',
    minWidth: 218,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '规则数量',
    dataIndex: 'ruleCount',
    slotName: 'ruleCount',
    align: 'left',
    width: 128,
    default: true,
  }, {
    title: '主机数量',
    dataIndex: 'hostCount',
    slotName: 'hostCount',
    align: 'left',
    width: 128,
    default: true,
  }, {
    title: '今日触发次数',
    dataIndex: 'todayTriggerCount',
    slotName: 'todayTriggerCount',
    align: 'left',
    width: 128,
    default: true,
  }, {
    title: '7日触发次数',
    dataIndex: 'weekTriggerCount',
    slotName: 'weekTriggerCount',
    align: 'left',
    width: 128,
    default: true,
  }, {
    title: '策略描述',
    dataIndex: 'description',
    slotName: 'description',
    align: 'left',
    minWidth: 238,
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
    width: 248,
    align: 'center',
    fixed: 'right',
    default: true,
  },
] as TableColumnData[];

export default columns;
