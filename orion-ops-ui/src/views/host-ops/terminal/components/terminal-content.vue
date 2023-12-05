<template>
  <div class="terminal-content">
    <slot />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalContent'
  };
</script>

<script lang="ts" setup>

</script>

<style lang="less" scoped>
  .terminal-content {
    width: 100%;
    height: 100%;
    position: relative;
  }
</style>
