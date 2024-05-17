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
    name: '全部',
    filter: undefined
  }, {
    name: '常规读',
    filter: (perm: string) => {
      return !!standardRead.find(s => perm.includes(s));
    }
  }, {
    name: '常规写',
    filter: (perm: string) => {
      return !!standardWrite.find(s => perm.includes(s));
    }
  }, {
    name: '查询',
    filter: (perm: string) => {
      return !!queryType.find(s => perm.includes(s));
    }
  }, {
    name: '新增',
    filter: (perm: string) => {
      return !!addType.find(s => perm.includes(s));
    }
  }, {
    name: '修改',
    filter: (perm: string) => {
      return !!updateType.find(s => perm.includes(s));
    }
  }, {
    name: '删除',
    filter: (perm: string) => {
      return !!deleteType.find(s => perm.includes(s));
    }
  }, {
    name: '管理',
    filter: (perm: string) => {
      return perm.includes('management');
    }
  }, {
    name: '授权',
    filter: (perm: string) => {
      return perm.includes('grant');
    }
  },
];

// 角色状态 字典项
export const roleStatusKey = 'systemRoleStatus';

// 加载的字典值
export const dictKeys = [roleStatusKey];
