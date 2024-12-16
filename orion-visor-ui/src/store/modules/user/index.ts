import type { UserState } from './types';
import type { LoginRequest } from '@/api/user/auth';
import { userLogin, userLogout } from '@/api/user/auth';
import { md5 } from '@/utils';
import { defineStore } from 'pinia';
import { clearToken, setToken } from '@/utils/auth';
import { removeRouteListener } from '@/utils/route-listener';
import { getUserAggregateInfo } from '@/api/user/user-aggregate';
import { useAppStore, useCacheStore, useMenuStore, useTabBarStore, useTipsStore } from '@/store';

export default defineStore('user', {
  state: (): UserState => ({
    id: undefined,
    username: undefined,
    nickname: undefined,
    avatar: undefined,
    roles: undefined,
    permission: undefined,
  }),

  getters: {
    userInfo(state: UserState): UserState {
      return { ...state };
    },
  },

  actions: {
    // 设置用户信息
    setUserInfo(partial: Partial<UserState>) {
      this.$patch(partial);
    },

    // 获取用户信息
    async getUserInfo() {
      const { data } = await getUserAggregateInfo();
      // 设置用户信息
      this.setUserInfo({
        id: data.user.id,
        username: data.user.username,
        nickname: data.user.nickname,
        avatar: data.user.avatar,
        roles: data.roles,
        permission: data.permissions,
      });
      // 设置用户偏好
      useAppStore().updateSettings(data.systemPreference);
      // 设置已经提示的key
      useTipsStore().set(data.tippedKeys);
      return data;
    },

    // 登录
    async login(loginForm: LoginRequest) {
      try {
        const loginRequest: LoginRequest = {
          username: loginForm.username,
          password: md5(loginForm.password as string),
        };
        // 执行登录
        const res = await userLogin(loginRequest);
        // 设置登录 token
        setToken(res.data.token);
      } catch (err) {
        clearToken();
        throw err;
      }
    },

    // 登出
    async logout() {
      try {
        await userLogout();
      } catch (e) {
      } finally {
        // 登出回调
        this.logoutCallBack();
      }
    },

    // 登出回调
    logoutCallBack() {
      this.$reset();
      clearToken();
      // 移除路由监听器
      removeRouteListener();
      // 清空菜单
      const menuStore = useMenuStore();
      menuStore.clearMenu();
      // 清除 tabs
      const tabBarStore = useTabBarStore();
      tabBarStore.resetTabList();
      // 清除缓存
      const cacheStore = useCacheStore();
      cacheStore.clear();
    },
  },
});
