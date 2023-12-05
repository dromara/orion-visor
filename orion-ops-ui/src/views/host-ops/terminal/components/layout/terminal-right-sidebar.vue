<template>
  <div class="terminal-right-sidebar">
    <!-- 操作按钮 -->
    <a-tooltip v-for="(action, index) in actions"
               :key="index"
               position="left"
               :mini="true"
               content-class="terminal-sidebar-tooltip-content"
               arrow-class="terminal-sidebar-tooltip-arrow"
               :content="action.content">
      <div class="terminal-sidebar-icon-wrapper" :style="action?.style">
        <div class="terminal-sidebar-icon" @click="action.event">
          <component :is="action.icon" />
        </div>
      </div>
    </a-tooltip>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalRightSidebar'
  };
</script>

<script lang="ts" setup>
  import { SidebarAction } from '../../types/terminal.type';

  const emits = defineEmits(['openSnippet', 'openSftp', 'openTransfer', 'openHistory']);

  // 操作
  const actions: Array<SidebarAction> = [
    {
      icon: 'icon-code-block',
      content: '打开命令片段',
      style: {},
      event: () => emits('openSnippet')
    },
    {
      icon: 'icon-folder',
      content: '打开 SFTP',
      style: {},
      event: () => emits('openSftp')
    },
    {
      icon: 'icon-swap',
      content: '文件传输列表',
      style: {
        transform: 'rotate(90deg)'
      },
      event: () => emits('openTransfer')
    },
    {
      icon: 'icon-history',
      content: '历史命令',
      event: () => emits('openHistory')
    },
  ];

</script>

<style lang="less" scoped>
  .terminal-right-sidebar {
    height: 100%;
  }
</style>
