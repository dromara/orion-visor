<template>
  <a-menu class="full"
          :mode="topMenu ? 'horizontal' : 'vertical'"
          v-model:collapsed="collapsed"
          v-model:open-keys="openKeys"
          v-model:selected-keys="selectedKey"
          :show-collapse-button="appStore.device !== 'mobile'"
          :auto-open="false"
          :auto-open-selected="true"
          :level-indent="34"
          @collapse="setCollapse">
    <template v-for="menu in menuTree">
      <!-- 一级菜单 -->
      <a-menu-item v-if="!menu.children?.length"
                   :key="menu.name"
                   @click="(e) => goto(e, menu)">
        <!-- 图标 -->
        <template #icon>
          <component v-if="menu.meta?.icon" :is="menu.meta?.icon" />
        </template>
        <!-- 名称 -->
        {{ menu.meta?.locale || '' }}
      </a-menu-item>
      <!-- 父菜单 -->
      <a-sub-menu v-else :key="menu.name">
        <!-- 图标 -->
        <template #icon>
          <component v-if="menu.meta?.icon" :is="menu.meta?.icon" />
        </template>
        <!-- 名称 -->
        <template #title>
          {{ menu.meta?.locale || '' }}
        </template>
        <!-- 子菜单 -->
        <a-menu-item v-for="child in menu.children"
                     :key="child.name"
                     @click="(e) => goto(e, child)">
          <!-- 图标 -->
          <template #icon v-if="child.meta?.icon">
            <component :is="child.meta?.icon" />
          </template>
          <!-- 名称 -->
          {{ child.meta?.locale || '' }}
        </a-menu-item>
      </a-sub-menu>
    </template>
  </a-menu>
</template>

<script lang="ts">
  export default {
    name: 'systemMenuTree'
  };
</script>

<script lang="ts" setup>
  import type { RouteMeta, RouteRecordRaw } from 'vue-router';
  import { useRoute, useRouter } from 'vue-router';
  import { computed, ref } from 'vue';
  import { useAppStore } from '@/store';
  import { listenerRouteChange } from '@/utils/route-listener';
  import { openWindow, regexUrl } from '@/utils';
  import { openNewRoute } from '@/router';
  import useMenuTree from './use-menu-tree';

  const emits = defineEmits(['collapse']);

  const appStore = useAppStore();
  const router = useRouter();
  const route = useRoute();
  const { menuTree } = useMenuTree();

  const collapsed = computed({
    get() {
      if (appStore.device === 'desktop') return appStore.menuCollapse;
      return false;
    },
    set(value: boolean) {
      appStore.updateSettings({ menuCollapse: value });
    },
  });
  const topMenu = computed(() => appStore.topMenu);

  const openKeys = ref<string[]>([]);
  const selectedKey = ref<string[]>([]);

  // 跳转路由
  const goto = (e: any, item: RouteRecordRaw) => {
    // 打开外链
    if (regexUrl.test(item.path)) {
      openWindow(item.path);
      return;
    }
    const { hideInMenu, activeMenu, newWindow } = item.meta as RouteMeta;
    // 新页面打开
    if (newWindow || e.ctrlKey) {
      openNewRoute({
        name: item.name,
      });
      return;
    }
    // 设置 selectedKey
    if (route.name === item.name && !hideInMenu && !activeMenu) {
      selectedKey.value = [item.name as string];
      return;
    }
    // 触发跳转
    router.push({
      name: item.name,
    });
  };

  const findMenuOpenKeys = (target: string) => {
    const result: string[] = [];
    let isFind = false;
    const backtrack = (item: RouteRecordRaw, keys: string[]) => {
      if (item.name === target) {
        isFind = true;
        result.push(...keys);
        return;
      }
      if (item.children?.length) {
        item.children.forEach((el) => {
          backtrack(el, [...keys, el.name as string]);
        });
      }
    };
    menuTree.value.forEach((el: RouteRecordRaw) => {
      if (isFind) return;
      backtrack(el, [el.name as string]);
    });
    return result;
  };

  // 监听路由 设置打开的 key
  listenerRouteChange((newRoute) => {
    const { activeMenu, hideInMenu } = newRoute.meta;
    if (!hideInMenu || activeMenu) {
      const menuOpenKeys = findMenuOpenKeys(
        (activeMenu || newRoute.name) as string
      );

      const keySet = new Set([...menuOpenKeys, ...openKeys.value]);
      openKeys.value = [...keySet];

      selectedKey.value = [
        activeMenu || menuOpenKeys[menuOpenKeys.length - 1],
      ];
    }
  }, true);

  // 展开菜单
  const setCollapse = (val: boolean) => {
    if (appStore.device === 'desktop')
      appStore.updateSettings({ menuCollapse: val });
  };

</script>

<style lang="less" scoped>
  :deep(.arco-menu-inner) {
    .arco-menu-inline-header {
      display: flex;
      align-items: center;
    }

    .arco-icon {
      &:not(.arco-icon-down) {
        font-size: 18px;
      }
    }

    .arco-menu-icon {
      margin-right: 10px !important;
    }

    .arco-menu-indent-list {
      width: 28px;
      display: inline-block;
    }

    .arco-menu-title {
      user-select: none;
    }
  }
</style>
