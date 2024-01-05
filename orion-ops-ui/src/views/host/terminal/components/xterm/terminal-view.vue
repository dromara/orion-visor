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
  import type { TerminalTabItem } from '@/store/modules/terminal/types';
  import { onMounted, onUnmounted, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import TerminalHandler from '@/views/host/terminal/handler/TerminalHandler';
  import { sleep } from '@/utils';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

  const { preference, dispatcher } = useTerminalStore();

  const terminalRef = ref();

  // 初始化
  const init = async () => {
    // 创建终端处理器
    const handler = new TerminalHandler(props.tab.key, terminalRef.value);
    // 等待前端渲染完成
    await sleep(100);
    // 注册处理器
    dispatcher.registerTerminalHandler(props.tab, handler);
  };

  onMounted(init);

  onUnmounted(() => {
    // 发送关闭
    console.log('12312312');
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
