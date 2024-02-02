<template>
  <div class="terminal-panels-container">
    <!-- 面板 -->
    <terminal-panel v-for="(panel, index) in panelManager.panels"
                    :key="index"
                    :index="index"
                    :panel="panel"
                    @close="closePanel" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalPanelView'
  };
</script>

<script lang="ts" setup>
  import { useTerminalStore } from '@/store';
  import { onUnmounted } from 'vue';
  import { TerminalTabs } from '../../types/terminal.const';
  import TerminalPanel from './terminal-panel.vue';

  const { tabManager, panelManager } = useTerminalStore();

  // 移除面板
  const closePanel = (index: number) => {
    panelManager.getPanel(index)?.clear();
    if (panelManager.panels.length == 1) {
      // 关闭 tab
      tabManager.deleteTab(TerminalTabs.TERMINAL_PANEL.key);
    } else {
      // 关闭面板
      panelManager.removePanel(index);
    }
  };

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
    display: flex;
  }
</style>
