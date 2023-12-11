<template>
  <div class="terminal-setting-container view-setting-container">
    <div class="view-setting-wrapper">
      <!-- 主标题 -->
      <h2 class="terminal-setting-title">外观设置</h2>
      <!-- 显示设置 -->
      <terminal-display-block />
      <!-- 主题设置 -->
      <terminal-theme-block />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalViewSetting'
  };
</script>

<script lang="ts" setup>
  import TerminalDisplayBlock from './terminal-display-block.vue';
  import TerminalThemeBlock from './terminal-theme-block.vue';

</script>

<style lang="less" scoped>
  .view-setting-wrapper {
    width: 932px;
  }

</style>
