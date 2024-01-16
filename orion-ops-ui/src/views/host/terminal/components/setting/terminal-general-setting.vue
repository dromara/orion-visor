<template>
  <div class="terminal-setting-container">
    <div class="terminal-setting-wrapper">
      <!-- 主标题 -->
      <h2 class="terminal-setting-title">终端设置</h2>
      <!-- 交互设置 -->
      <terminal-interact-block />
      <!-- 插件设置 -->
      <terminal-plugins-block />
      <!-- 会话设置 -->
      <terminal-session-block />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalGeneralSetting'
  };
</script>

<script lang="ts" setup>
  import TerminalInteractBlock from './terminal-interact-block.vue';
  import TerminalPluginsBlock from './terminal-plugins-block.vue';
  import TerminalSessionBlock from './terminal-session-block.vue';

</script>

<style lang="less" scoped>

</style>
