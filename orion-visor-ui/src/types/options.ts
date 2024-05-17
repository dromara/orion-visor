import type { SelectOptionData } from '@arco-design/web-vue';
import type { HostQueryResponse } from '@/api/asset/host';

// 获取认证主机过滤器选项
export const getAuthorizedHostOptions = (list: Array<HostQueryResponse>): Array<SelectOptionData> => {
  const options: Array<SelectOptionData> = [];
  // 添加 tags
  const tagNames = list.map(s => s.tags)
    .filter(s => s?.length)
    .flat(1)
    .sort((o1, o2) => o1.id - o2.id)
    .map(s => s.name);
  [...new Set(tagNames)].map(value => {
    return { label: value, value: `@${value}`, isTag: true };
  }).forEach(s => options.push(s));
  // 添加主机信息
  const hostMeta = list.map(s => {
    return [s.name, s.code, s.address, s.alias];
  }).filter(Boolean).flat(1);
  [...new Set(hostMeta)].map(value => {
    return { label: value, value };
  }).forEach(s => options.push(s));
  return options;
};
