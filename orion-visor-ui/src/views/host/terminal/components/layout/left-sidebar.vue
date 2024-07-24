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
    name: 'leftSidebar'
  };
</script>

<script lang="ts" setup>
  import type { SidebarAction } from '../../types/define';
  import { TerminalTabs } from '../../types/const';
  import { useTerminalStore } from '@/store';
  import IconActions from './icon-actions.vue';

  const { tabManager } = useTerminalStore();

  // 顶部操作
  const topActions: Array<SidebarAction> = [
    {
      icon: TerminalTabs.NEW_CONNECTION.icon,
      content: TerminalTabs.NEW_CONNECTION.title,
      click: () => tabManager.openTab(TerminalTabs.NEW_CONNECTION)
    },
  ];

  // 底部操作
  const bottomActions: Array<SidebarAction> = [
    {
      icon: TerminalTabs.SHORTCUT_SETTING.icon,
      content: TerminalTabs.SHORTCUT_SETTING.title,
      click: () => tabManager.openTab(TerminalTabs.SHORTCUT_SETTING)
    },
    {
      icon: TerminalTabs.DISPLAY_SETTING.icon,
      content: TerminalTabs.DISPLAY_SETTING.title,
      click: () => tabManager.openTab(TerminalTabs.DISPLAY_SETTING)
    },
    {
      icon: TerminalTabs.THEME_SETTING.icon,
      content: TerminalTabs.THEME_SETTING.title,
      click: () => tabManager.openTab(TerminalTabs.THEME_SETTING)
    },
    {
      icon: TerminalTabs.TERMINAL_SETTING.icon,
      content: TerminalTabs.TERMINAL_SETTING.title,
      click: () => tabManager.openTab(TerminalTabs.TERMINAL_SETTING)
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
