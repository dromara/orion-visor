// 角色状态
export const RoleStatus = {
  // 停用
  DISABLED: 0,
  // 启用
  ENABLED: 1,
};

// 查询操作
const queryType = ['query', 'view'];
const addType = ['add', 'create'];
const updateType = ['update', 'modify'];
const deleteType = ['delete', 'remove'];
const standardRead = [...queryType];
const standardWrite = [...addType, ...updateType];

// 快速分配菜单操作
export const quickGrantMenuOperator = [
  {
    name: '',
    rule: undefined
  }, {
    name: '常规读操作',
    rule: (perm: string) => {
      return !!standardRead.find(s => perm.includes(s));
    }
  }, {
    name: '常规写操作',
    rule: (perm: string) => {
      return !!standardWrite.find(s => perm.includes(s));
    }
  }, {
    name: '查询',
    rule: (perm: string) => {
      return !!queryType.find(s => perm.includes(s));
    }
  }, {
    name: '新增',
    rule: (perm: string) => {
      return !!addType.find(s => perm.includes(s));
    }
  }, {
    name: '修改',
    rule: (perm: string) => {
      return !!updateType.find(s => perm.includes(s));
    }
  }, {
    name: '删除',
    rule: (perm: string) => {
      return !!deleteType.find(s => perm.includes(s));
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
