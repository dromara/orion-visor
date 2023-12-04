import HostGroupRoleGrant from '../components/host-group-role-grant.vue';
import HostGroupUserGrant from '../components/host-group-user-grant.vue';
import HostKeyRoleGrant from '../components/host-key-role-grant.vue';
import HostKeyUserGrant from '../components/host-key-user-grant.vue';
import HostIdentityRoleGrant from '../components/host-identity-role-grant.vue';
import HostIdentityUserGrant from '../components/host-identity-user-grant.vue';

// 路由
export const GrantRouteName = 'assetGrant';

// 授权 key
export const GrantKey = {
  // 主机分组-角色
  HOST_GROUP_ROLE: 1,
  // 主机分组-用户
  HOST_GROUP_USER: 2,
  // 主机秘钥-角色
  HOST_KEY_ROLE: 3,
  // 主机秘钥-用户
  HOST_KEY_USER: 4,
  // 主机身份-角色
  HOST_IDENTITY_ROLE: 5,
  // 主机身份-用户
  HOST_IDENTITY_USER: 6,
};

// 授权 tab 组件
export const GrantTabs = [
  {
    key: GrantKey.HOST_GROUP_ROLE,
    permission: ['asset:host-group:grant'],
    title: '主机分组授权 - 角色',
    icon: 'icon-desktop',
    component: HostGroupRoleGrant
  }, {
    key: GrantKey.HOST_GROUP_USER,
    permission: ['asset:host-group:grant'],
    title: '主机分组授权 - 用户',
    icon: 'icon-desktop',
    component: HostGroupUserGrant
  }, {
    key: GrantKey.HOST_KEY_ROLE,
    permission: ['asset:host-key:grant'],
    title: '主机秘钥授权 - 角色',
    icon: 'icon-lock',
    component: HostKeyRoleGrant
  }, {
    key: GrantKey.HOST_KEY_USER,
    permission: ['asset:host-key:grant'],
    title: '主机秘钥授权 - 用户',
    icon: 'icon-lock',
    component: HostKeyUserGrant
  }, {
    key: GrantKey.HOST_IDENTITY_ROLE,
    permission: ['asset:host-identity:grant'],
    title: '主机身份授权 - 角色',
    icon: 'icon-idcard',
    component: HostIdentityRoleGrant
  }, {
    key: GrantKey.HOST_IDENTITY_USER,
    permission: ['asset:host-identity:grant'],
    title: '主机身份授权 - 用户',
    icon: 'icon-idcard',
    component: HostIdentityUserGrant
  },
];
