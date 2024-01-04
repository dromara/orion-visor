<template>
  <div class="terminal-right-sidebar">
    <!-- 顶部操作按钮 -->
    <icon-actions class="top-actions"
                  :actions="topActions"
                  position="left" />
    <!-- 底部操作按钮 -->
    <icon-actions class="bottom-actions"
                  :actions="bottomActions"
                  position="left" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalRightSidebar'
  };
</script>

<script lang="ts" setup>
  import type { SidebarAction } from '../../types/terminal.const';
  import IconActions from './icon-actions.vue';
  import { computed } from 'vue';
  import { useTerminalStore } from '@/store';
  import { DarkTheme } from '@/store/modules/terminal';

  const emits = defineEmits(['openSnippet', 'openSftp', 'openTransfer', 'screenshot']);

  const terminalStore = useTerminalStore();

  // 顶部操作
  const topActions = computed<Array<SidebarAction>>(() => [
    {
      icon: 'icon-code-block',
      content: '打开命令片段',
      click: () => emits('openSnippet')
    },
    {
      icon: 'icon-folder',
      content: '打开 SFTP',
      click: () => emits('openSftp')
    },
    {
      icon: 'icon-swap',
      content: '文件传输列表',
      iconStyle: {
        transform: 'rotate(90deg)'
      },
      click: () => emits('openTransfer')
    },
    {
      icon: terminalStore.isDarkTheme ? 'icon-sun-fill' : 'icon-moon-fill',
      content: terminalStore.isDarkTheme ? '点击切换为亮色模式' : '点击切换为暗色模式',
      click: () => terminalStore.changeDarkTheme(terminalStore.isDarkTheme ? DarkTheme.LIGHT : DarkTheme.DARK)
    },
  ]);

  // 底部操作
  const bottomActions: Array<SidebarAction> = [
    {
      icon: 'icon-camera',
      content: '截图',
      click: () => emits('screenshot')
    },
  ];

</script>

<style lang="less" scoped>
  .terminal-right-sidebar {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
</style>
