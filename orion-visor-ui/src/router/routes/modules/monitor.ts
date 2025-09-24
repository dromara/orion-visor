import type { AppRouteRecordRaw } from '../types';
import type { RouteLocationNormalized } from 'vue-router';
import { DEFAULT_LAYOUT } from '../base';

const MONITOR: AppRouteRecordRaw = {
  name: 'monitorModule',
  path: '/monitor-module',
  component: DEFAULT_LAYOUT,
  children: [
    {
      name: 'metrics',
      path: '/monitor/metrics',
      component: () => import('@/views/monitor/metrics/index.vue'),
    },
    {
      name: 'monitorHost',
      path: '/monitor/monitor-host',
      component: () => import('@/views/monitor/monitor-host/index.vue'),
    },
    {
      name: 'alarmPolicy',
      path: '/monitor/alarm-policy',
      component: () => import('@/views/monitor/alarm-policy/index.vue'),
    },
    {
      name: 'alarmEvent',
      path: '/monitor/alarm-event',
      component: () => import('@/views/monitor/alarm-event/index.vue'),
    },
    {
      name: 'monitorDetail',
      path: '/monitor/detail',
      meta: {
        // 固定到 tab
        noAffix: false,
        // 是否允许打开多个 tab
        multipleTab: true,
        // 名称模板
        localeTemplate: (route: RouteLocationNormalized) => {
          return `${route.meta.locale} - ${route.query.name || ''}`;
        },
      },
      component: () => import('@/views/monitor/monitor-detail/index.vue'),
    },
    {
      name: 'alarmRule',
      path: '/monitor/alarm-rule',
      meta: {
        // 固定到 tab
        noAffix: false,
        // 是否允许打开多个 tab
        multipleTab: true,
        // 名称模板
        localeTemplate: (route: RouteLocationNormalized) => {
          return `${route.meta.locale} - ${route.query.name || ''}`;
        },
      },
      component: () => import('@/views/monitor/alarm-rule/index.vue'),
    },
  ],
};

export default MONITOR;
