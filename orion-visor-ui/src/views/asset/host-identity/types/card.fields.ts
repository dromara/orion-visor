import type { CardField, CardFieldConfig } from '@/types/card';
import { dateFormat } from '@/utils';

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
      label: '用户名',
      dataIndex: 'username',
      slotName: 'username',
      ellipsis: true,
    }, {
      label: '类型',
      dataIndex: 'type',
      slotName: 'type',
    }, {
      label: '主机密钥',
      dataIndex: 'keyId',
      slotName: 'keyId',
      height: '24px',
    }, {
      label: '描述',
      dataIndex: 'description',
      slotName: 'description',
      ellipsis: true,
    }, {
      label: '创建时间',
      dataIndex: 'createTime',
      slotName: 'createTime',
      render: ({ record }) => {
        return dateFormat(new Date(record.createTime));
      },
    }
  ] as CardField[]
} as CardFieldConfig;

export default fieldConfig;
