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
    align: 'left',
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '配置描述',
    dataIndex: 'description',
    slotName: 'description',
    align: 'left',
    ellipsis: true,
    tooltip: true,
    default: true,
  }, {
    title: '类型',
    dataIndex: 'valueType',
    slotName: 'valueType',
    align: 'left',
    width: 150,
    default: true,
  }, {
    title: '额外配置',
    dataIndex: 'extraSchema',
    slotName: 'extraSchema',
    align: 'left',
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
