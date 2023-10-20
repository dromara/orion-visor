import { CardField, CardFieldConfig } from '@/types/card';
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
      label: '配置项id',
      dataIndex: 'keyId',
      slotName: 'keyId',
    }, {
      label: '配置项',
      dataIndex: 'keyName',
      slotName: 'keyName',
      ellipsis: true,
    }, {
      label: '配置名称',
      dataIndex: 'name',
      slotName: 'name',
      ellipsis: true,
    }, {
      label: '配置值',
      dataIndex: 'value',
      slotName: 'value',
      ellipsis: true,
    }, {
      label: '配置描述',
      dataIndex: 'label',
      slotName: 'label',
      ellipsis: true,
    }, {
      label: '额外参数',
      dataIndex: 'extra',
      slotName: 'extra',
      ellipsis: true,
    }, {
      label: '排序',
      dataIndex: 'sort',
      slotName: 'sort',
    }, {
      label: '创建时间',
      dataIndex: 'createTime',
      slotName: 'createTime',
      render: ({ record }) => {
        return dateFormat(new Date(record.createTime));
      },
    }, {
      label: '修改时间',
      dataIndex: 'updateTime',
      slotName: 'updateTime',
      render: ({ record }) => {
        return dateFormat(new Date(record.updateTime));
      },
    }, {
      label: '创建人',
      dataIndex: 'creator',
      slotName: 'creator',
    }, {
      label: '修改人',
      dataIndex: 'updater',
      slotName: 'updater',
    }
  ] as CardField[]
} as CardFieldConfig;

export default fieldConfig;
