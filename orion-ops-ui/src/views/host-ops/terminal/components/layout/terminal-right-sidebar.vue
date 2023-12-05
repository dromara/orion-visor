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
      <div class="terminal-sidebar-icon-wrapper">
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
  const emits = defineEmits(['openSnippet']);

  // 操作
  const actions = [
    {
      icon: 'icon-code-block',
      content: '打开命令片段',
      event: () => emits('openSnippet')
    },
  ];

</script>

<style lang="less" scoped>
  .terminal-right-sidebar {
    height: 100%;
  }
</style>
