// 通过 label 进行过滤
export const labelFilter = (searchValue: string, option: { label: string; }) => {
  return option.label.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
};
