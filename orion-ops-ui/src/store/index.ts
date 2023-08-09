import { createPinia } from 'pinia';
import useAppStore from './modules/app';
import useUserStore from './modules/user';
import useTabBarStore from './modules/tab-bar';
import useCacheStore from './modules/cache';

const pinia = createPinia();

export {
  useAppStore,
  useUserStore,
  useTabBarStore,
  useCacheStore
};

export default pinia;
