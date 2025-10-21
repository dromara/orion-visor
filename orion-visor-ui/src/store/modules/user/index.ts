import type { UserState } from './types';
import type { LoginRequest } from '@/api/user/auth';
import { userLogin, userLogout } from '@/api/user/auth';
import { md5 } from '@/utils';
import { defineStore } from 'pinia';
import { clearToken, setToken } from '@/utils/auth';
import { removeRouteListener } from '@/utils/route-listener';
import { getUserAggregateInfo } from '@/api/user/user-aggregate';
import { useAppStore, useCacheStore, useMenuStore, useTabBarStore, useTipsStore } from '@/store';
import { ApiError } from '@/api/error';

const CHECK_APP_VERSION_KEY = 'check-app-version';

// 检查版本更新
const checkForVersionUpdate = (serverVersion: string) => {
  try {
    if (!serverVersion) {
      return;
    }
    const clientVersion = import.meta.env.VITE_APP_VERSION;
    // 版本相同
    if (serverVersion === clientVersion) {
      localStorage.removeItem(CHECK_APP_VERSION_KEY);
      return;
    }
    // 版本不同
    const lastCheck = localStorage.getItem(CHECK_APP_VERSION_KEY);
    const lastCheckData = lastCheck ? JSON.parse(lastCheck) : null;
    // 判断是否是同版本 或 距离上次提醒不超过 24 小时
    if (lastCheckData?.version === serverVersion && Date.now() - (lastCheckData?.time || 0) < 24 * 60 * 60 * 1000) {
      return;
    }
    // 提示用户更新
    if (window.confirm('检测到新版本, 请强制刷新页面以获取最新内容!')) {
      window.location.reload();
    }
    // 更新 localStorage 记录
    localStorage.setItem(CHECK_APP_VERSION_KEY, JSON.stringify({
      version: serverVersion,
      time: Date.now(),
    }));
  } catch (error) {
    // ignored
  }
};

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
      const resp = await getUserAggregateInfo();
      const { data: { code, msg, data }, headers } = resp;
      // 检查版本更新
      checkForVersionUpdate(headers?.['x-app-version']);
      if (code !== 200) {
        throw new ApiError(msg, resp);
      }
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
