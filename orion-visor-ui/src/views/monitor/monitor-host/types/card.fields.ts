import type { CardField, CardFieldConfig } from '@/types/card';

const fieldConfig = {
  rowGap: '10px',
  labelSpan: 6,
  minHeight: '22px',
  fields: [
    {
      label: '主机ID',
      dataIndex: 'hostId',
      slotName: 'hostId',
      default: true,
    }, {
      label: '主机地址',
      dataIndex: 'address',
      slotName: 'address',
      default: true,
    }, {
      label: '主机编码',
      dataIndex: 'code',
      slotName: 'code',
      default: false,
    }, {
      label: '负责人',
      dataIndex: 'ownerUsername',
      slotName: 'ownerUsername',
      ellipsis: true,
      default: true,
    }, {
      label: '设备状态',
      dataIndex: 'agentOnlineStatus',
      slotName: 'agentOnlineStatus',
      default: true,
    }, {
      label: 'CPU',
      dataIndex: 'cpuUsage',
      slotName: 'cpuUsage',
      default: true,
    }, {
      label: '内存',
      dataIndex: 'memoryUsage',
      slotName: 'memoryUsage',
      default: true,
    }, {
      label: '磁盘',
      dataIndex: 'diskUsage',
      slotName: 'diskUsage',
      default: true,
    }, {
      label: '网络',
      dataIndex: 'network',
      slotName: 'network',
      default: true,
    }, {
      label: '负载',
      dataIndex: 'load',
      slotName: 'load',
      default: true,
    }, {
      label: '标签',
      dataIndex: 'tags',
      slotName: 'tags',
      default: false,
    }, {
      label: 'agentKey',
      dataIndex: 'agentKey',
      slotName: 'agentKey',
      ellipsis: true,
      default: false,
    }, {
      label: '探针版本',
      dataIndex: 'agentVersion',
      slotName: 'agentVersion',
      ellipsis: true,
      tooltip: true,
      default: true,
    }
  ] as CardField[]
} as CardFieldConfig;

export default fieldConfig;
