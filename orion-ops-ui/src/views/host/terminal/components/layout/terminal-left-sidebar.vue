<template>
  <div class="terminal-left-sidebar">
    <!-- 顶部操作按钮 -->
    <icon-actions class="top-actions"
                  :actions="topActions"
                  position="left" />
    <!-- 底部操作按钮 -->
    <icon-actions class="bottom-actions"
                  :actions="bottomActions"
                  position="right" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalLeftSidebar'
  };
</script>

<script lang="ts" setup>
  import type { SidebarAction } from '../../types/terminal.type';
  import { InnerTabs } from '../../types/terminal.const';
  import { useTerminalStore } from '@/store';
  import IconActions from './icon-actions.vue';

  const { tabManager } = useTerminalStore();

  // 顶部操作
  const topActions: Array<SidebarAction> = [
    {
      icon: InnerTabs.NEW_CONNECTION.icon,
      content: InnerTabs.NEW_CONNECTION.title,
      click: () => tabManager.openTab(InnerTabs.NEW_CONNECTION)
    },
  ];

  // 底部操作
  const bottomActions: Array<SidebarAction> = [
    {
      icon: InnerTabs.SHORTCUT_SETTING.icon,
      content: InnerTabs.SHORTCUT_SETTING.title,
      click: () => tabManager.openTab(InnerTabs.SHORTCUT_SETTING)
    },
    {
      icon: InnerTabs.DISPLAY_SETTING.icon,
      content: InnerTabs.DISPLAY_SETTING.title,
      click: () => tabManager.openTab(InnerTabs.DISPLAY_SETTING)
    },
    {
      icon: InnerTabs.THEME_SETTING.icon,
      content: InnerTabs.THEME_SETTING.title,
      click: () => tabManager.openTab(InnerTabs.THEME_SETTING)
    },
    {
      icon: InnerTabs.TERMINAL_SETTING.icon,
      content: InnerTabs.TERMINAL_SETTING.title,
      click: () => tabManager.openTab(InnerTabs.TERMINAL_SETTING)
    },
  ];

</script>

<style lang="less" scoped>
  .terminal-left-sidebar {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
</style>
