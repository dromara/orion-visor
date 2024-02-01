<template>
  <div class="terminal-panels-container">
    <!-- 面板 -->
    <terminal-panel v-for="panelIndex in panelManager.panels.length"
                    :key="panelIndex"
                    :panel="panelManager.panels[panelIndex - 1]" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalPanelView'
  };
</script>

<script lang="ts" setup>
  import { useTerminalStore } from '@/store';
  import TerminalPanel from './terminal-panel.vue';
  import { onUnmounted } from 'vue';

  const { panelManager } = useTerminalStore();
  // FIXME 全部关闭则关闭

  // 卸载清空
  onUnmounted(() => {
    panelManager.reset();
  });

</script>

<style lang="less" scoped>
  .terminal-panels-container {
    width: 100%;
    height: calc(100vh - var(--header-height));
    position: relative;
  }
</style>
