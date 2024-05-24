import type { VitePWAOptions } from 'vite-plugin-pwa';
import { VitePWA } from 'vite-plugin-pwa';
import { isProductionMode } from '../utils';

/**
 * 配置 pwa
 */
export default function configPwaPlugin() {
  if (isProductionMode()) {
    // 生产启用
    return VitePWA(enabled());
  } else {
    // 本地禁用
    return VitePWA(disabled());
  }
}

// 禁用
const disabled = (): Partial<VitePWAOptions> => {
  return {
    disable: true,
    manifest: false,
    selfDestroying: true,
    devOptions: {
      enabled: false,
      disableRuntimeConfig: true,
    },
  };
};

// 启用
const enabled = (): Partial<VitePWAOptions> => {
  return {
    manifest: {
      name: 'Orion Visor Community',
      short_name: 'Orion Visor',
      description: '一款高颜值、现代化的智能运维&轻量堡垒机平台。',
      theme_color: '#212529',
      icons: [{
        src: 'logo_150.png',
        sizes: '150x150',
        type: 'image/png',
      }],
    },
    registerType: 'autoUpdate',
    workbox: {
      // 缓存相关静态资源
      globPatterns: ['**/*.{js,css,html,ico,png,jpg,svg}'],
      // 配置自定义运行时缓存
      runtimeCaching: [
        isProductionMode()
          ? {
            urlPattern: ({ url }) => url.origin === 'https://app-api.id',
            handler: 'NetworkFirst',
            options: {
              cacheName: 'wisbayar-api',
              cacheableResponse: {
                statuses: [200]
              }
            }
          } : {
            urlPattern: ({ url }) => url.origin === 'https://app-api-0.com',
            handler: 'NetworkFirst',
            options: {
              cacheName: 'wisbayar-api',
              cacheableResponse: {
                statuses: [200]
              }
            }
          },
        {
          urlPattern: /\.(?:png|jpg|jpeg|svg)$/,
          handler: 'CacheFirst',
          options: {
            cacheName: 'wisbayar-images',
            expiration: {
              // 最多30个图
              maxEntries: 30
            }
          }
        },
        {
          urlPattern: /.*\.js.*/,
          handler: 'StaleWhileRevalidate',
          options: {
            cacheName: 'wisbayar-js',
            // 最多缓存30个, 超过的按照LRU原则删除
            expiration: {
              maxEntries: 30,
              maxAgeSeconds: 7 * 24 * 60 * 60
            },
            cacheableResponse: {
              statuses: [200]
            }
          }
        },
        {
          urlPattern: /.*\.css.*/,
          handler: 'StaleWhileRevalidate',
          options: {
            cacheName: 'wisbayar-css',
            expiration: {
              maxEntries: 20,
              maxAgeSeconds: 7 * 24 * 60 * 60
            },
            cacheableResponse: {
              statuses: [200]
            }
          }
        },
        {
          urlPattern: /.*\.html.*/,
          handler: 'StaleWhileRevalidate',
          options: {
            cacheName: 'wisbayar-html',
            expiration: {
              maxEntries: 20,
              maxAgeSeconds: 7 * 24 * 60 * 60
            },
            cacheableResponse: {
              statuses: [200]
            }
          }
        }
      ]
    },
    devOptions: {
      enabled: true,
    },
  };
};
