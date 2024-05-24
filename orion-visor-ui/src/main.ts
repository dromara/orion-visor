import { createApp } from 'vue';
import ArcoVue from '@arco-design/web-vue';
import ArcoVueIcon from '@arco-design/web-vue/es/icon';
import globalComponents from '@/components';
import router from './router';
import store from './store';
import i18n from './locale';
import directive from './directive';
import './mock';
// 样式通过 arco-plugin 插件导入 详见目录文件 config/plugin/arcoStyleImport.ts
import '@/assets/style/global.less';
import '@/assets/style/layout.less';
import '@/assets/style/arco-extends.less';
import '@/api/interceptor';
import App from './App.vue';

const app = createApp(App);

app.use(ArcoVue, {});
app.use(ArcoVueIcon);

app.use(router);
app.use(store);
app.use(i18n);
app.use(globalComponents);
app.use(directive);

app.mount('#app');

// 监听 PWA 注册事件
window.addEventListener('beforeinstallprompt', (e) => {
  e.preventDefault();
  (window as CustomWindow).deferredPrompt = e;
});
