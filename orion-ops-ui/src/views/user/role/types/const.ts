// 角色状态
export const RoleStatus = {
  // 停用
  DISABLED: 0,
  // 启用
  ENABLED: 1,
};

// 快速分配菜单操作
export const quickGrantMenuOperator = [
  {
    name: '',
    rule: undefined
  }, {
    name: '查询',
    rule: (perm: string) => {
      return perm.includes('query') || perm.includes('view');
    }
  }, {
    name: '新增',
    rule: (perm: string) => {
      return perm.includes('add') || perm.includes('create');
    }
  }, {
    name: '修改',
    rule: (perm: string) => {
      return perm.includes('update') || perm.includes('modify');
    }
  }, {
    name: '删除',
    rule: (perm: string) => {
      return perm.includes('delete') || perm.includes('remove');
    }
  }, {
    name: '导入',
    rule: (perm: string) => {
      return perm.includes('import');
    }
  }, {
    name: '导出',
    rule: (perm: string) => {
      return perm.includes('export');
    }
  },
];

// 角色状态 字典项
export const roleStatusKey = 'systemRoleStatus';

// 加载的字典值
export const dictKeys = [roleStatusKey];
