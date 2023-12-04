// 通过 label 进行过滤
export const labelFilter = (searchValue: string, option: { label: string; }) => {
  return option.label.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
};

// 通过 title 进行过滤
export const titleFilter = (searchValue: string, option: { title: string; }) => {
  return option.title.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
};
