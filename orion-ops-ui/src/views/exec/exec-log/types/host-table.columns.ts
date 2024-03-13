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
    title: '执行日志id',
    dataIndex: 'logId',
    slotName: 'logId',
    align: 'left',
  }, {
    title: '主机id',
    dataIndex: 'hostId',
    slotName: 'hostId',
    align: 'left',
  }, {
    title: '主机名称',
    dataIndex: 'hostName',
    slotName: 'hostName',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '执行状态',
    dataIndex: 'status',
    slotName: 'status',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '执行命令',
    dataIndex: 'command',
    slotName: 'command',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '执行参数',
    dataIndex: 'parameter',
    slotName: 'parameter',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '退出码',
    dataIndex: 'exitStatus',
    slotName: 'exitStatus',
    align: 'left',
  }, {
    title: '日志路径',
    dataIndex: 'logPath',
    slotName: 'logPath',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '错误信息',
    dataIndex: 'errorMessage',
    slotName: 'errorMessage',
    align: 'left',
    ellipsis: true,
    tooltip: true,
  }, {
    title: '执行开始时间',
    dataIndex: 'startTime',
    slotName: 'startTime',
    align: 'left',
    width: 180,
    render: ({ record }) => {
      return record.startTime && dateFormat(new Date(record.startTime));
    },
  }, {
    title: '执行结束时间',
    dataIndex: 'finishTime',
    slotName: 'finishTime',
    align: 'left',
    width: 180,
    render: ({ record }) => {
      return record.finishTime && dateFormat(new Date(record.finishTime));
    },
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
  }, {
    title: '创建人',
    dataIndex: 'creator',
    slotName: 'creator',
  }, {
    title: '修改人',
    dataIndex: 'updater',
    slotName: 'updater',
  }, {
    title: '操作',
    slotName: 'handle',
    width: 130,
    align: 'center',
    fixed: 'right',
  },
] as TableColumnData[];

export default columns;
