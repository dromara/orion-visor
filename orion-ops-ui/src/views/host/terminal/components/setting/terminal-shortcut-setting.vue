<template>
  <div class="terminal-setting-container">
    <div class="terminal-setting-wrapper">
      <!-- 主标题 -->
      <h2 class="terminal-setting-title">快捷键设置</h2>
      <!-- 快捷键设置 -->
      <terminal-shortcut-block />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalShortcutSetting'
  };
</script>

<script lang="ts" setup>
  import TerminalShortcutBlock from './terminal-shortcut-block.vue';

</script>

<style lang="less" scoped>

</style>
