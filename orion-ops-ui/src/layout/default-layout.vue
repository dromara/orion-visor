<template>
  <a-layout class="layout" :class="{ mobile: appStore.hideMenu }">
    <!-- tab bar -->
    <div v-if="navbar" class="layout-navbar">
      <nav-bar />
    </div>
    <a-layout style="flex-direction: row;">
      <!-- 左侧菜单栏 -->
      <a-layout-sider v-if="renderMenu"
                      v-show="!hideMenu"
                      class="layout-sider"
                      breakpoint="xl"
                      :collapsed="collapsed"
                      :collapsible="true"
                      :width="menuWidth"
                      :style="{ paddingTop: navbar ? '60px' : '' }"
                      :hide-trigger="true"
                      @collapse="setCollapsed">
        <div class="menu-wrapper">
          <system-menu-tree />
        </div>
      </a-layout-sider>
      <!-- 顶部菜单栏 -->
      <a-drawer v-if="hideMenu"
                :visible="drawerVisible"
                placement="left"
                :header="false"
                :footer="false"
                mask-closable
                :closable="false"
                @cancel="drawerCancel">
        <system-menu-tree style="padding: 12px 16px;" />
      </a-drawer>
      <!-- body -->
      <a-layout class="layout-content" :style="paddingStyle">
        <!-- 页签 -->
        <tab-bar v-if="appStore.tabBar" />
        <!-- 页面 -->
        <a-layout-content style="height: 100%; width: 100%;">
          <!-- 水印 -->
          <a-watermark :grayscale="true"
                       :alpha=".6"
                       :z-index="9999"
                       style="width: 100%; height: 100%;"
                       :content="userStore.username || ''">
            <page-layout />
          </a-watermark>
        </a-layout-content>
        <!-- 页脚 -->
        <app-footer v-if="visibleFooter" />
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script lang="ts" setup>
  import { computed, onMounted, provide, ref } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { useAppStore, useUserStore } from '@/store';
  import useResponsive from '@/hooks/responsive';
  import { toggleDrawerMenuKey } from '@/types/symbol';
  import PageLayout from './page-layout.vue';
  import NavBar from '@/components/app/navbar/index.vue';
  import TabBar from '@/components/app/tab-bar/index.vue';
  import AppFooter from '@/components/app/app-footer/index.vue';
  import SystemMenuTree from '@/components/system/menu/tree/index.vue';

  const appStore = useAppStore();
  const userStore = useUserStore();
  const router = useRouter();
  const route = useRoute();
  useResponsive(true);

  const isInit = ref(false);
  const navbarHeight = `60px`;
  const navbar = computed(() => appStore.navbar);
  const renderMenu = computed(() => appStore.menu && !appStore.topMenu);
  const hideMenu = computed(() => appStore.hideMenu);
  const visibleFooter = computed(() => appStore.footer);
  const menuWidth = computed(() => {
    return appStore.menuCollapse ? 48 : appStore.menuWidth;
  });

  const collapsed = computed(() => {
    return appStore.menuCollapse;
  });

  const paddingStyle = computed(() => {
    const paddingLeft = renderMenu.value && !hideMenu.value
      ? { paddingLeft: `${menuWidth.value}px` }
      : {};
    const paddingTop = navbar.value ? { paddingTop: navbarHeight } : {};
    return { ...paddingLeft, ...paddingTop };
  });

  // 设置菜单展开状态
  const setCollapsed = (val: boolean) => {
    if (!isInit.value) return;
    appStore.updateSettings({ menuCollapse: val });
  };

  const drawerVisible = ref(false);
  const drawerCancel = () => {
    drawerVisible.value = false;
  };

  // 对外暴露触发收缩菜单
  provide(toggleDrawerMenuKey, () => {
    drawerVisible.value = !drawerVisible.value;
  });

  onMounted(() => {
    isInit.value = true;
  });
</script>

<style lang="less" scoped>
  @nav-size-height: 60px;
  @layout-max-width: 1100px;

  .layout {
    width: 100%;
    height: 100%;
  }

  .layout-navbar {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 120;
    width: 100%;
    height: @nav-size-height;
  }

  .layout-sider {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 99;
    height: 100%;
    transition: all 0.2s cubic-bezier(0.34, 0.69, 0.1, 1);

    &::after {
      position: absolute;
      top: 0;
      right: -1px;
      display: block;
      width: 1px;
      height: 100%;
      background-color: var(--color-border);
      content: '';
    }

    > :deep(.arco-layout-sider-children) {
      overflow-y: hidden;
    }
  }

  .menu-wrapper {
    height: 100%;
    overflow: auto;
    overflow-x: hidden;
  }

  .layout-content {
    min-height: 100vh;
    overflow-y: hidden;
    background-color: var(--color-fill-2);
    transition: padding 0.2s cubic-bezier(0.34, 0.69, 0.1, 1);
  }
</style>
