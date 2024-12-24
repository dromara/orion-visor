import { useRouter } from 'vue-router';
import { Message } from '@arco-design/web-vue';
import { useUserStore } from '@/store';
import { LOGIN_ROUTE_NAME } from '@/router/constants';

export default function useUser() {
  const router = useRouter();
  const userStore = useUserStore();

  // 退出登录
  const logout = async (msg: string = '已退出登录') => {
    await userStore.logout();
    if (msg) {
      Message.success(msg);
    }
    await router.push({ name: LOGIN_ROUTE_NAME });
  };

  // 退出并重定向
  const logoutRedirect = async (logoutTo?: string) => {
    await userStore.logout();
    const currentRoute = router.currentRoute.value;
    Message.success('已退出登录');
    await router.push({
      name: logoutTo || LOGIN_ROUTE_NAME,
      query: {
        ...router.currentRoute.value.query,
        redirect: currentRoute.name as string,
      },
    });
  };

  return {
    logout,
    logoutRedirect,
  };
}
