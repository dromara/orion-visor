<template>
  <div id="" class="terminal-example" ref="terminal"></div>
</template>

<script lang="ts">
  export default {
    name: 'terminalExample'
  };
</script>

<script lang="ts" setup>
  import { Terminal } from '@xterm/xterm';
  import { onMounted, ref } from 'vue';
  import { TerminalTheme } from '../types/terminal.theme';

  const props = defineProps<{
    theme: TerminalTheme
  }>();

  const terminal = ref();

  onMounted(() => {
    const term = new Terminal({
      theme: props.theme,
      cols: 47,
      rows: 6,
      fontSize: 15,
      convertEol: true,
      cursorBlink: false,
      cursorInactiveStyle: 'none'
    });
    term.open(terminal.value);

    term.write(
      '[94m[root[0m@[96mOrionServer usr]#[0m\n' +
      '[92mdr-xr-xr-x.[0m   2 root root [96mbin[0m\n' +
      '[92mdr-xr-xr-x.[0m   2 root root [96msbin[0m\n' +
      '[92mdr-xr-xr-x.[0m  43 root root [96mlib[0m\n' +
      '[92mdr-xr-xr-x.[0m  62 root root [96mlib64[0m\n' +
      '[92mlrwxrwxrwx.[0m   1 root root [90;42mtmp[0m'
    );
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
