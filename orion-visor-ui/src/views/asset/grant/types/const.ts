import { defineAsyncComponent } from 'vue';

// 组件
const HostGroupGrant = defineAsyncComponent(() => import('../components/host-group-grant.vue'));
const HostKeyGrant = defineAsyncComponent(() => import('../components/host-key-grant.vue'));
const HostIdentityGrant = defineAsyncComponent(() => import('../components/host-identity-grant.vue'));

// 路由
export const GrantRouteName = 'assetGrant';

// 授权 key
export const GrantKey = {
  // 主机分组-角色
  HOST_GROUP_ROLE: 1,
  // 主机分组-用户
  HOST_GROUP_USER: 2,
  // 主机密钥-角色
  HOST_KEY_ROLE: 3,
  // 主机密钥-用户
  HOST_KEY_USER: 4,
  // 主机身份-角色
  HOST_IDENTITY_ROLE: 5,
  // 主机身份-用户
  HOST_IDENTITY_USER: 6,
};

// 授权类型
export const GrantType = {
  ROLE: 'role',
  USER: 'user',
};

// 授权 tab 组件
export const GrantTabs = [
  {
    key: GrantKey.HOST_GROUP_ROLE,
    permission: ['asset:host-group:grant'],
    title: '主机分组授权 - 角色',
    icon: 'icon-desktop',
    type: GrantType.ROLE,
    component: HostGroupGrant
  }, {
    key: GrantKey.HOST_GROUP_USER,
    permission: ['asset:host-group:grant'],
    title: '主机分组授权 - 用户',
    icon: 'icon-desktop',
    type: GrantType.USER,
    component: HostGroupGrant
  }, {
    key: GrantKey.HOST_KEY_ROLE,
    permission: ['asset:host-key:grant'],
    title: '主机密钥授权 - 角色',
    icon: 'icon-lock',
    type: GrantType.ROLE,
    component: HostKeyGrant
  }, {
    key: GrantKey.HOST_KEY_USER,
    permission: ['asset:host-key:grant'],
    title: '主机密钥授权 - 用户',
    icon: 'icon-lock',
    type: GrantType.USER,
    component: HostKeyGrant
  }, {
    key: GrantKey.HOST_IDENTITY_ROLE,
    permission: ['asset:host-identity:grant'],
    title: '主机身份授权 - 角色',
    icon: 'icon-idcard',
    type: GrantType.ROLE,
    component: HostIdentityGrant
  }, {
    key: GrantKey.HOST_IDENTITY_USER,
    permission: ['asset:host-identity:grant'],
    title: '主机身份授权 - 用户',
    icon: 'icon-idcard',
    type: GrantType.USER,
    component: HostIdentityGrant
  },
];

// 主机类型 字典项
export const hostTypeKey = 'hostType';

// 主机系统类型 字典项
export const hostOsTypeKey = 'hostOsType';

// 身份类型 字典项
export const identityTypeKey = 'hostIdentityType';

// 加载的字典值
export const dictKeys = [hostTypeKey, hostOsTypeKey, identityTypeKey];
