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
  import { onMounted, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { FitAddon } from 'xterm-addon-fit';
  import { WebglAddon } from 'xterm-addon-webgl';
  import { Terminal } from 'xterm';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

  const { preference, dispatcher } = useTerminalStore();

  const terminalRef = ref();

  // 初始化
  const init = () => {
    // FIXME fontfamily
    // 初始化终端
    const term = new Terminal({
      theme: preference.themeSchema,
      fastScrollModifier: 'shift',
      ...(preference.displaySetting as any),
    });
    // 注册插件
    const fitAddon = new FitAddon();
    const webglAddon = new WebglAddon();
    term.loadAddon(fitAddon);
    term.loadAddon(webglAddon);
    // 打开终端
    term.open(terminalRef.value);
    // 自适应
    fitAddon.fit();
    // 注册钩子
    dispatcher.registerTerminalHook(props.tab);
    // 初始化终端

  };

  onMounted(init);

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
