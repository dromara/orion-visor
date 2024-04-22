import { useRouter } from 'vue-router';
import { Message } from '@arco-design/web-vue';

import { useUserStore } from '@/store';

export default function useUser() {
  const router = useRouter();
  const userStore = useUserStore();

  // 退出登录
  const logout = async () => {
    await userStore.logout();
    Message.success('已退出登录');
    await router.push({ name: 'login' });
  };

  // 退出并重定向
  const logoutRedirect = async (logoutTo?: string) => {
    await userStore.logout();
    const currentRoute = router.currentRoute.value;
    Message.success('已退出登录');
    await router.push({
      name: logoutTo || 'login',
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
