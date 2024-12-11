import { mergeConfig } from 'vite';
import baseConfig from './vite.config.base';
import configCompressPlugin from './plugin/compress';
import configVisualizerPlugin from './plugin/visualizer';
import configArcoResolverPlugin from './plugin/arcoResolver';
import configImageminPlugin from './plugin/imagemin';

export default mergeConfig(
  {
    mode: 'production',
    plugins: [
      configCompressPlugin('gzip'),
      configVisualizerPlugin(),
      configArcoResolverPlugin(),
      configImageminPlugin(),
    ],
    build: {
      rollupOptions: {
        output: {
          manualChunks: {
            arco: ['@arco-design/web-vue'],
            arcoExt: ['@sanqi377/arco-vue-icon-picker', '@dangojs/a-query-header'],
            chart: ['echarts', 'vue-echarts'],
            vue: ['vue', 'vue-router', 'pinia', '@vueuse/core', 'vue-i18n'],
            axios: ['axios'],
            xterm: ['@xterm/xterm', '@xterm/addon-canvas', '@xterm/addon-fit',
              '@xterm/addon-image', '@xterm/addon-search', '@xterm/addon-unicode11',
              '@xterm/addon-web-links', '@xterm/addon-webgl'],
            monaco: ['monaco-editor'],
            pkg: ['dayjs', 'cron-parser', 'ts-md5', 'file-saver', 'html2canvas']
          },
        },
      },
      chunkSizeWarningLimit: 1024 * 8,
    },
  },
  baseConfig,
);
