import type { CardField, CardFieldConfig } from '@/types/card';
import { dateFormat } from '@/utils';

const fieldConfig = {
  rowGap: '10px',
  labelSpan: 6,
  minHeight: '22px',
  fields: [
    {
      label: 'id',
      dataIndex: 'id',
      slotName: 'id',
      default: true,
    }, {
      label: '描述',
      dataIndex: 'description',
      slotName: 'description',
      ellipsis: true,
      default: true,
    }, {
      label: '创建时间',
      dataIndex: 'createTime',
      slotName: 'createTime',
      render: ({ record }) => {
        return dateFormat(new Date(record.createTime));
      },
      default: true,
    }, {
      label: '修改时间',
      dataIndex: 'updateTime',
      slotName: 'updateTime',
      render: ({ record }) => {
        return dateFormat(new Date(record.updateTime));
      },
      default: true,
    }, {
      label: '创建人',
      dataIndex: 'creator',
      slotName: 'creator',
      ellipsis: true,
      default: true,
    }, {
      label: '修改人',
      dataIndex: 'updater',
      slotName: 'updater',
      ellipsis: true,
      default: true,
    }
  ] as CardField[]
} as CardFieldConfig;

export default fieldConfig;
