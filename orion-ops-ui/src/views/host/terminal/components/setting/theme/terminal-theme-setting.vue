<template>
  <div class="terminal-setting-container">
    <div class="terminal-setting-wrapper">
      <!-- 主标题 -->
      <h2 class="terminal-setting-title">主题设置</h2>
      <!-- 主题设置 -->
      <terminal-theme-block />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalThemeSetting'
  };
</script>

<script lang="ts" setup>
  import TerminalThemeBlock from './terminal-theme-block.vue';

</script>

<style lang="less" scoped>

</style>
