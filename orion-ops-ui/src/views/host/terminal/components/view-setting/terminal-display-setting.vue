<template>
  <div class="terminal-setting-container">
    <div class="terminal-setting-wrapper">
      <!-- 主标题 -->
      <h2 class="terminal-setting-title">显示设置</h2>
      <!-- 显示偏好 -->
      <terminal-display-block />
      <!-- 顶部工具栏 -->
      <terminal-action-bar-block />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalDisplaySetting'
  };
</script>

<script lang="ts" setup>
  import TerminalDisplayBlock from './terminal-display-block.vue';
  import TerminalActionBarBlock from './terminal-action-bar-block.vue';

</script>

<style lang="less" scoped>

</style>
