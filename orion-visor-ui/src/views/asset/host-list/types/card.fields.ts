import type { CardField, CardFieldConfig } from '@/types/card';

const fieldConfig = {
  rowGap: '10px',
  labelSpan: 8,
  minHeight: '22px',
  fields: [
    {
      label: 'id',
      dataIndex: 'id',
      slotName: 'id',
    }, {
      label: '主机类型',
      dataIndex: 'type',
      slotName: 'type',
    }, {
      label: '主机编码',
      dataIndex: 'code',
      slotName: 'code',
    }, {
      label: '主机地址',
      dataIndex: 'address',
      slotName: 'address',
      tooltip: true,
    }, {
      label: '主机状态',
      dataIndex: 'status',
      slotName: 'status',
    }, {
      label: '主机标签',
      dataIndex: 'tags',
      slotName: 'tags',
      rowAlign: 'start',
    },
  ] as CardField[]
} as CardFieldConfig;
export default fieldConfig;
