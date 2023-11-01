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
    title: '操作用户',
    dataIndex: 'username',
    slotName: 'username',
    width: 120,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '操作模块',
    dataIndex: 'module',
    slotName: 'module',
    width: 120,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '操作类型',
    dataIndex: 'type',
    slotName: 'type',
    width: 150,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '风险等级',
    dataIndex: 'riskLevel',
    slotName: 'riskLevel',
    width: 90,
    align: 'center',
  }, {
    title: '执行结果',
    dataIndex: 'result',
    slotName: 'result',
    width: 90,
    align: 'center',
  }, {
    title: '操作日志',
    dataIndex: 'originLogInfo',
    slotName: 'originLogInfo',
    ellipsis: true,
    tooltip: true,
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
    title: '操作',
    slotName: 'handle',
    width: 90,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
