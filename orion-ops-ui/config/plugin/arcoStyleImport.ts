import { vitePluginForArco } from '@arco-plugins/vite-vue';

/**
 * 样式按需引入
 * https://github.com/arco-design/arco-plugins/blob/main/packages/plugin-vite-vue/README.md
 * https://arco.design/vue/docs/start
 */
export default function configArcoStyleImportPlugin() {
  return vitePluginForArco({});
}
