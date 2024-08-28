import type { App } from 'vue';
import permission from './permission';
import focus from './focus';

export default {
  install(Vue: App) {
    Vue.directive('permission', permission);
    Vue.directive('focus', focus);
  },
};
