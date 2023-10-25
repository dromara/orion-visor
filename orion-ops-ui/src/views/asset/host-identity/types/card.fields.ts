import type { CardField, CardFieldConfig } from '@/types/card';
import { dateFormat } from '@/utils';

const fieldConfig = {
  rowGap: '12px',
  labelSpan: 8,
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
      label: '主机秘钥',
      dataIndex: 'keyId',
      slotName: 'keyId',
      height: '24px',
    }, {
      label: '修改时间',
      dataIndex: 'updateTime',
      slotName: 'updateTime',
      render: ({ record }) => {
        return dateFormat(new Date(record.updateTime));
      },
    }
  ] as CardField[]
} as CardFieldConfig;

export default fieldConfig;
