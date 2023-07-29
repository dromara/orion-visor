import { computed } from 'vue';
import { RouteRecordRaw, RouteRecordNormalized } from 'vue-router';
import usePermission from '@/hooks/permission';
import { useAppStore } from '@/store';
import { cloneDeep } from 'lodash';

export default function useMenuTree() {
  const appStore = useAppStore();
  const appRoute = computed(() => {
    return appStore.appAsyncMenus;
  });
  const menuTree = computed(() => {
    const copyRouter = cloneDeep(appRoute.value) as RouteRecordNormalized[];
    copyRouter.sort((a: RouteRecordNormalized, b: RouteRecordNormalized) => {
      return (a.meta.order || 0) - (b.meta.order || 0);
    });

    function travel(_routes: RouteRecordRaw[], layer: number) {
      if (!_routes) return null;

      const collector: any = _routes.map((element) => {
        // 隐藏子目录
        if (element.meta?.hideChildrenInMenu || !element.children) {
          element.children = [];

          if (element.meta?.hideInMenu) {
            // 如果隐藏菜单 则不显示
            return null;
          } else {
            return element;
          }
        }

        // 过滤不显示的菜单
        element.children = element.children.filter(
          (x) => x.meta?.hideInMenu !== true
        );

        // 关联子节点
        const subItem = travel(element.children, layer + 1);

        if (subItem.length) {
          element.children = subItem;
          return element;
        }
        // 第二层 (子目录)
        if (layer > 1) {
          element.children = subItem;
          return element;
        }

        // 是否隐藏目录
        if (element.meta?.hideInMenu === false) {
          return element;
        }
        return null;
      });
      return collector.filter(Boolean);
    }

    return travel(copyRouter, 0);
  });

  return {
    menuTree,
  };
}
