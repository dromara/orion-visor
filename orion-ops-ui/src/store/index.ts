import { createPinia } from 'pinia';
import useAppStore from './modules/app';
import useMenuStore from './modules/menu';
import useUserStore from './modules/user';
import useTabBarStore from './modules/tab-bar';
import useCacheStore from './modules/cache';
import useTipsStore from './modules/tips';

const pinia = createPinia();

export {
  useAppStore,
  useMenuStore,
  useUserStore,
  useTabBarStore,
  useCacheStore,
  useTipsStore,
};

export default pinia;
