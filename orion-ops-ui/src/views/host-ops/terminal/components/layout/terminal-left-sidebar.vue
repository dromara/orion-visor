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
  import { SidebarAction } from '../../types/terminal.type';
  import IconActions from './icon-actions.vue';

  const emits = defineEmits(['openAdd', 'copyAddress', 'openShortcutSetting', 'openViewSetting', 'openThemeSetting']);

  // 顶部操作
  const topActions: Array<SidebarAction> = [
    {
      icon: 'icon-plus',
      content: '新建连接',
      click: () => emits('openAdd')
    },
    {
      icon: 'icon-copy',
      content: '复制主机IP',
      click: () => emits('copyAddress')
    },
  ];

  // 底部操作
  const bottomActions: Array<SidebarAction> = [
    {
      icon: 'icon-command',
      content: '快捷键设置',
      click: () => emits('openShortcutSetting')
    },
    {
      icon: 'icon-palette',
      content: '主题设置',
      click: () => emits('openThemeSetting')
    },
    {
      icon: 'icon-tool',
      content: '显示设置',
      click: () => emits('openViewSetting')
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
