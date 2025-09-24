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
    title: '告警条件',
    dataIndex: 'triggerCondition',
    slotName: 'triggerCondition',
    align: 'left',
    minWidth: 348,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '指标标签',
    dataIndex: 'tags',
    slotName: 'tags',
    align: 'left',
    width: 168,
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '告警级别',
    dataIndex: 'level',
    slotName: 'level',
    align: 'left',
    width: 120,
    default: true,
  }, {
    title: '持续数据点',
    dataIndex: 'consecutiveCount',
    slotName: 'consecutiveCount',
    align: 'left',
    width: 108,
    default: true,
  }, {
    title: '静默时间',
    dataIndex: 'silencePeriod',
    slotName: 'silencePeriod',
    align: 'left',
    width: 108,
    default: true,
  }, {
    title: '规则开关',
    dataIndex: 'ruleSwitch',
    slotName: 'ruleSwitch',
    align: 'left',
    width: 118,
    default: true,
  }, {
    title: '规则描述',
    dataIndex: 'description',
    slotName: 'description',
    align: 'left',
    minWidth: 128,
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
