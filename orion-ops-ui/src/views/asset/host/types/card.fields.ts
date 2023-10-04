import { CardField, CardRecord, toCardDesc } from '@/types/card';

export const fields = [
  {
    field: 'id',
    label: 'id',
    render: r => r.id,
  }, {
    field: 'name',
    label: '主机名称',
    render: r => r.name,
  }, {
    field: 'code',
    label: '主机编码',
    render: r => 'code',
  }, {
    field: 'address',
    label: '主机地址',
    render: r => 'address',
  }, {
    field: 'tag',
    label: '标签',
    render: r => 'tag',
  },
] as CardField[];

export const convert = (record: CardRecord) => {
  return toCardDesc(fields, record);
};

