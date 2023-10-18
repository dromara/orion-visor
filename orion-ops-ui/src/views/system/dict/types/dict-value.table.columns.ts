import { TableColumnData } from '@arco-design/web-vue/es/table/interface';
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
    ellipsis: true,
    tooltip: true,
  }, {
    title: '配置名称',
    dataIndex: 'name',
    slotName: 'name',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '配置描述',
    dataIndex: 'label',
    slotName: 'label',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '配置值',
    dataIndex: 'value',
    slotName: 'value',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '额外参数',
    dataIndex: 'extra',
    slotName: 'extra',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '排序',
    dataIndex: 'sort',
    slotName: 'sort',
    width: 70,
  }, {
    title: '修改时间',
    dataIndex: 'updateTime',
    slotName: 'updateTime',
    width: 180,
    render: ({ record }) => {
      return dateFormat(new Date(record.updateTime));
    },
  }, {
    title: '操作',
    slotName: 'handle',
    width: 160,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
