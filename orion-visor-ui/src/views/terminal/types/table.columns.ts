import type { TableColumnData } from '@arco-design/web-vue';
import { getFileSize } from '@/utils/file';

// SFTP 表格列
export const sftpColumns = [
  {
    title: '名称',
    dataIndex: 'name',
    slotName: 'fileName',
    ellipsis: true,
    tooltip: true,
    sortable: {
      sortDirections: ['ascend', 'descend'],
    },
    filterable: {
      filter: (value, record) => record.name.includes(value),
      slotName: 'nameFilter',
    }
  }, {
    title: '大小',
    dataIndex: 'size',
    slotName: 'size',
    ellipsis: true,
    sortable: {
      sortDirections: ['ascend', 'descend'],
    },
    render: ({ record }) => {
      return getFileSize(record.size);
    },
  }, {
    title: '属性',
    dataIndex: 'attr',
    slotName: 'attr',
    ellipsis: true,
  }, {
    title: '修改时间',
    dataIndex: 'modifyTime',
    slotName: 'modifyTime',
    align: 'center',
    sortable: {
      sortDirections: ['ascend', 'descend'],
    },
    width: 264,
    cellClass: 'action-cell',
  },
] as TableColumnData[];

export default sftpColumns;
