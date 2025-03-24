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
      label: '主机编码',
      dataIndex: 'code',
      slotName: 'code',
      default: true,
    }, {
      label: '主机地址',
      dataIndex: 'address',
      slotName: 'address',
      tooltip: true,
      default: true,
    }, {
      label: '主机类型',
      dataIndex: 'type',
      slotName: 'type',
      default: true,
    }, {
      label: '主机状态',
      dataIndex: 'status',
      slotName: 'status',
      default: true,
    }, {
      label: '主机标签',
      dataIndex: 'tags',
      slotName: 'tags',
      rowAlign: 'start',
      default: true,
    }, {
      label: '主机描述',
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
      default: false,
    }, {
      label: '修改时间',
      dataIndex: 'updateTime',
      slotName: 'updateTime',
      render: ({ record }) => {
        return dateFormat(new Date(record.updateTime));
      },
      default: false,
    }, {
      label: '创建人',
      dataIndex: 'creator',
      slotName: 'creator',
      ellipsis: true,
      default: false,
    }, {
      label: '修改人',
      dataIndex: 'updater',
      slotName: 'updater',
      ellipsis: true,
      default: false,
    }
  ] as CardField[]
} as CardFieldConfig;
export default fieldConfig;
