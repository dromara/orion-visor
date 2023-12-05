<template>
  <div class="terminal-left-sidebar">
    <!-- 操作按钮 -->
    <a-tooltip v-for="(action, index) in actions"
               :key="index"
               position="right"
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
    name: 'TerminalLeftSidebar'
  };
</script>

<script lang="ts" setup>
  import { SidebarAction } from '../../types/terminal.type';

  const emits = defineEmits(['openAdd', 'openSetting']);

  // 操作
  const actions: Array<SidebarAction> = [
    {
      icon: 'icon-plus',
      content: '新建连接',
      event: () => emits('openAdd')
    },
    {
      icon: 'icon-settings',
      content: '设置',
      event: () => emits('openSetting')
    }
  ];

</script>

<style lang="less" scoped>
  .terminal-left-sidebar {
    height: 100%;
  }
</style>
