import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';
import { dateFormat } from '@/utils';

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    slotName: 'id',
    width: 100,
    align: 'left',
    fixed: 'left',
  }, {
    title: '上传用户',
    dataIndex: 'username',
    slotName: 'username',
    align: 'left',
    width: 118,
    ellipsis: true,
    tooltip: true,
  }, {
    title: '上传描述',
    dataIndex: 'description',
    slotName: 'description',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '远程路径',
    dataIndex: 'remotePath',
    slotName: 'remotePath',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '上传状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'center',
    width: 138,
  }, {
    title: '文件数量',
    dataIndex: 'fileCount',
    slotName: 'fileCount',
    width: 98,
    align: 'center',
  }, {
    title: '主机数量',
    dataIndex: 'hostCount',
    slotName: 'hostCount',
    width: 98,
    align: 'center',
  }, {
    title: '上传时间',
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
    width: 180,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
