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
    title: '配置项',
    dataIndex: 'keyName',
    slotName: 'keyName',
    align: 'left',
    ellipsis: true,
    tooltip: true,
    render: ({ record }) => {
      return `${record.keyName} - ${record.keyDescription}`;
    },
  }, {
    title: '配置描述',
    dataIndex: 'label',
    slotName: 'label',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '配置值',
    dataIndex: 'value',
    slotName: 'value',
    align: 'left',
    ellipsis: true,
  }, {
    title: '额外参数',
    dataIndex: 'extra',
    slotName: 'extra',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '排序',
    dataIndex: 'sort',
    slotName: 'sort',
    align: 'left',
    width: 70,
  }, {
    title: '修改时间',
    dataIndex: 'updateTime',
    slotName: 'updateTime',
    align: 'center',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.updateTime));
    },
  }, {
    title: '操作',
    slotName: 'handle',
    width: 170,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
