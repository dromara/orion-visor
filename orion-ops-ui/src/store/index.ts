import { createPinia } from 'pinia';
import useAppStore from './modules/app';
import useMenuStore from './modules/menu';
import useUserStore from './modules/user';
import useTabBarStore from './modules/tab-bar';
import useCacheStore from './modules/cache';
import useTipsStore from './modules/tips';
import useDictStore from './modules/dict';
import useHostSpaceStore from './modules/host-space';
import useTerminalStore from './modules/terminal';

const pinia = createPinia();

export {
  useAppStore,
  useMenuStore,
  useUserStore,
  useTabBarStore,
  useCacheStore,
  useTipsStore,
  useDictStore,
  useTerminalStore,
  useHostSpaceStore,
};

export default pinia;
