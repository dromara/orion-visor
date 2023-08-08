import { createPinia } from 'pinia';
import useAppStore from './modules/app';
import useUserStore from './modules/user';
import useTabBarStore from './modules/tab-bar';
import useMenuStore from './modules/system/menu';

const pinia = createPinia();

export {
  useAppStore,
  useUserStore,
  useTabBarStore,
  useMenuStore
};

export default pinia;
