import type { SelectOptionData, TreeNodeData } from '@arco-design/web-vue';

// 通过 label 进行过滤
export const labelFilter = (searchValue: string, option: { label: string }) => {
  return option.label.toLowerCase().includes(searchValue.toLowerCase());
};

// 通过 title 进行过滤
export const titleFilter = (searchValue: string, option: TreeNodeData) => {
  return (option.title as string)?.toLowerCase().includes(searchValue.toLowerCase());
};

// 通过 tag label 进行过滤
export const tagLabelFilter = (searchValue: string, option: SelectOptionData) => {
  if (searchValue.startsWith('@') && option.isTag) {
    // tag 过滤
    return (option.label as string)?.toLowerCase().startsWith(searchValue.substring(1, searchValue.length).toLowerCase());
  } else {
    // 文本过滤
    return (option.label as string)?.toLowerCase().includes(searchValue.toLowerCase());
  }
};
