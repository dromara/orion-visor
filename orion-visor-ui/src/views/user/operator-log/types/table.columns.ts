import type { TableColumnData } from '@arco-design/web-vue';
import { dateFormat } from '@/utils';

const columns = [
  {
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
    width: 234,
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
    minWidth: 238,
    align: 'left',
    ellipsis: true,
  }, {
    title: '留痕地址',
    dataIndex: 'address',
    slotName: 'address',
    width: 156,
    align: 'left',
    ellipsis: true,
  }, {
    title: '操作时间',
    dataIndex: 'createTime',
    slotName: 'createTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.createTime));
    },
  }, {
    title: '操作',
    dataIndex: 'handle',
    slotName: 'handle',
    width: 128,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
