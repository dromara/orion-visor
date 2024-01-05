<template>
  <div class="terminal-container">
    <!-- 头部 -->
    <div class="terminal-header">
      终端 {{ tab.key }} {{ tab.title }}
    </div>
    <!-- 终端 -->
    <div class="terminal-wrapper">
      <div class="terminal-inst" ref="terminalRef" />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalView'
  };
</script>

<script lang="ts" setup>
  import type { TerminalTabItem } from '../../types/terminal.type';
  import { onMounted, onUnmounted, ref } from 'vue';
  import { useTerminalStore } from '@/store';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

  const { sessionManager } = useTerminalStore();

  const terminalRef = ref();

  // 初始化回话
  onMounted(async () => {
    // 创建终端处理器
    sessionManager.openSession(props.tab, terminalRef.value);
  });

  // 关闭回话
  onUnmounted(() => {
    sessionManager.closeSession(props.tab.key);
  });

</script>

<style lang="less" scoped>
  @terminal-header-height: 30px;
  .terminal-container {
    width: 100%;
    height: calc(100vh - var(--header-height));
    position: relative;
  }

  .terminal-header {
    width: 100%;
    height: @terminal-header-height;
  }

  .terminal-wrapper {
    width: 100%;
    height: calc(100% - @terminal-header-height);
    position: relative;
  }

  .terminal-inst {
    width: 100%;
    height: 100%;
  }
</style>
