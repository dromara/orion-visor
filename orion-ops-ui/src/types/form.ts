import type { SelectOptionData, TreeNodeData } from '@arco-design/web-vue';

// 通过 label 进行过滤
export const labelFilter = (searchValue: string, option: { label: string }) => {
  return option.label.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
};

// 通过 title 进行过滤
export const titleFilter = (searchValue: string, option: TreeNodeData) => {
  return (option.title as string).toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
};

// 通过 tag label 进行过滤
export const tagLabelFilter = (searchValue: string, option: SelectOptionData) => {
  if (searchValue.startsWith('@')) {
    // tag 过滤
    return option.isTag && (option.label as string).toLowerCase().startsWith(searchValue.substring(1, searchValue.length).toLowerCase());
  } else {
    // 文本过滤
    return !option.isTag && (option.label as string).toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
  }
};
