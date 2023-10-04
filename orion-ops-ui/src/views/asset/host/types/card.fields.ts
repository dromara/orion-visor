import { CardField, CardFieldConfig } from '@/types/card';

export const fieldConfig = {
  rowGap: '10px',
  labelSpan: 8,
  fields: [
    {
      label: '主机编码',
      dataIndex: 'code',
      slotName: 'code',
    }, {
      label: '主机地址',
      dataIndex: 'address',
      slotName: 'address',
      tooltip: true,
    }, {
      label: '主机标签',
      dataIndex: 'tags',
      slotName: 'tags',
    },
  ] as CardField[]
} as CardFieldConfig;
export default fieldConfig;
