<template>
  <div class="terminal-right-sidebar">
    <!-- 操作按钮 -->
    <icon-actions class="terminal-actions"
                  :actions="actions"
                  position="left" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalRightSidebar'
  };
</script>

<script lang="ts" setup>
  import { SidebarAction } from '../../types/terminal.type';
  import IconActions from './icon-actions.vue';

  const emits = defineEmits(['openSnippet', 'openSftp', 'openTransfer', 'openHistory']);

  // 操作
  const actions: Array<SidebarAction> = [
    {
      icon: 'icon-code-block',
      content: '打开命令片段',
      style: {},
      click: () => emits('openSnippet')
    },
    {
      icon: 'icon-folder',
      content: '打开 SFTP',
      style: {},
      click: () => emits('openSftp')
    },
    {
      icon: 'icon-swap',
      content: '文件传输列表',
      style: {
        transform: 'rotate(90deg)'
      },
      click: () => emits('openTransfer')
    },
    {
      icon: 'icon-history',
      content: '历史命令',
      visible: false,
      click: () => emits('openHistory')
    },
  ];

</script>

<style lang="less" scoped>
  .terminal-right-sidebar {
    height: 100%;
  }
</style>
