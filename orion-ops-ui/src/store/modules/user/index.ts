import { defineStore } from 'pinia';
import { getUserInfo } from '@/api/user';
import { login as userLogin, LoginRequest, logout as userLogout, } from '@/api/user/auth';
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
    permission: undefined,
    roles: undefined,
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
      const res = await getUserInfo();
      this.setInfo({
        id: 1,
        username: 'admin',
        nickname: '管理员',
        permission: ['*'],
        roles: ['admin'],
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
        const res = await userLogin(loginRequest);
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
      } finally {
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
