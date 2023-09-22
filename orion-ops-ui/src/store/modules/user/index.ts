import { defineStore } from 'pinia';
import { getUserPermission, login as userLogin, LoginRequest, logout as userLogout } from '@/api/user/auth';
import { clearToken, setToken } from '@/utils/auth';
import { md5 } from '@/utils';
import { removeRouteListener } from '@/utils/route-listener';
import { UserState } from './types';
import useAppStore from '../app';

const useUserStore = defineStore('user', {
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
    /**
     * 设置用户信息
     */
    setInfo(partial: Partial<UserState>) {
      this.$patch(partial);
    },

    /**
     * 获取用户信息
     */
    async info() {
      const { data } = await getUserPermission();
      this.setInfo({
        id: data.user.id,
        username: data.user.username,
        nickname: data.user.nickname,
        avatar: data.user.avatar,
        roles: data.roles,
        permission: data.permissions,
      });
    },

    /**
     * 登录
     */
    async login(loginForm: LoginRequest) {
      try {
        const loginRequest: LoginRequest = {
          username: loginForm.username,
          password: md5(loginForm.password),
        };
        // 执行登陆
        const res = await userLogin(loginRequest);
        // 设置登陆 token
        setToken(res.data.token);
      } catch (err) {
        clearToken();
        throw err;
      }
    },

    /**
     * 登出
     */
    async logout() {
      try {
        await userLogout();
      } catch (e) {
      } finally {
        // 登出回调
        this.logoutCallBack();
      }
    },

    /**
     * 登出回调
     */
    logoutCallBack() {
      this.$reset();
      clearToken();
      // 移除路由监听器
      removeRouteListener();
      // 清空菜单
      const appStore = useAppStore();
      appStore.clearMenu();
    },
  },
});

export default useUserStore;
