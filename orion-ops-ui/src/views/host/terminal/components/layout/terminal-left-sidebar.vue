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
  import type { SidebarAction } from '../../types/terminal.const';
  import { InnerTabs } from '../../types/terminal.const';
  import { useTerminalStore } from '@/store';
  import IconActions from './icon-actions.vue';

  const terminalStore = useTerminalStore();

  // 顶部操作
  const topActions: Array<SidebarAction> = [
    {
      icon: 'icon-plus',
      content: '新建连接',
      click: () => terminalStore.switchTab(InnerTabs.NEW_CONNECTION)
    },
  ];

  // 底部操作
  const bottomActions: Array<SidebarAction> = [
    {
      icon: 'icon-command',
      content: '快捷键设置',
      click: () => terminalStore.switchTab(InnerTabs.SHORTCUT_SETTING)
    },
    {
      icon: 'icon-palette',
      content: '外观设置',
      click: () => terminalStore.switchTab(InnerTabs.VIEW_SETTING)
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
