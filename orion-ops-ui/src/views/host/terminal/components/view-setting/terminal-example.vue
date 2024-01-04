<template>
  <div class="terminal-example" ref="terminal"></div>
</template>

<script lang="ts">
  export default {
    name: 'terminalExample'
  };
</script>

<script lang="ts" setup>
  import type { TerminalThemeSchema } from '@/store/modules/terminal/types';
  import { Terminal } from 'xterm';
  import { onMounted, onUnmounted, ref } from 'vue';

  const props = defineProps<{
    theme: TerminalThemeSchema | Record<string, any>
  }>();

  const terminal = ref();
  const term = ref();

  onMounted(() => {
    term.value = new Terminal({
      theme: props.theme,
      cols: 42,
      rows: 6,
      fontSize: 15,
      cursorInactiveStyle: 'none',
    });
    term.value.open(terminal.value);
    term.value.write(
      '[1;94m[root[0m@[1;96mOrionServer usr]#[0m\r\n' +
      '[92mdr-xr-xr-x.[0m   2 root root [96mbin[0m\r\n' +
      '[92mdr-xr-xr-x.[0m   2 root root [96msbin[0m\r\n' +
      '[92mdr-xr-xr-x.[0m  43 root root [96mlib[0m\r\n' +
      '[92mdr-xr-xr-x.[0m  62 root root [96mlib64[0m\r\n' +
      '[92mlrwxrwxrwx.[0m   1 root root [90;102mtmp[0m'
    );
  });

  defineExpose({ term });

  onUnmounted(() => {
    term.value?.dispose();
  });

</script>

<style lang="less" scoped>
  .terminal-example {
    padding: 16px;
    width: 100%;
    height: 100%;
  }

  :deep(.xterm-viewport) {
    overflow: hidden !important;
  }
</style>
