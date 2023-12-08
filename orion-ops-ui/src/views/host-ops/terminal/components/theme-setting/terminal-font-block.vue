<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        字体设置
      </h3>
    </div>
    <!-- 内容区域 -->
    <div class="terminal-setting-body">
      <div class="terminal-setting-form">
        123
      </div>
      <!-- 预览区域 -->
      <div class="terminal-example">
        <div class="terminal-example-wrapper"
             :style="{ background: terminalStore.preference.terminalTheme.background }">
          <terminal-example :theme="terminalStore.preference.terminalTheme"
                            ref="previewTerminal" />
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalFontBlock'
  };
</script>

<script lang="ts" setup>
  import { ref, watch } from 'vue';
  import TerminalExample from '../theme-setting/terminal-example.vue';
  import { useTerminalStore } from '@/store';

  const terminalStore = useTerminalStore();

  const previewTerminal = ref();

  watch(() => terminalStore.preference.terminalTheme, (v) => {
    if (!v) {
      return;
    }
    const options = previewTerminal.value?.term?.options;
    options && (options.theme = v);
  });

</script>

<style lang="less" scoped>
  @terminal-width: 458px;
  @terminal-height: 138px;

  .terminal-setting-body {
    height: 248px;
    width: 100%;
    border: 1px solid var(--color-border-2);
    border-radius: 4px;
    display: flex;
    justify-content: space-between;
  }

  .terminal-example {
    margin: auto 16px 16px 0;

    &-wrapper {
      border-radius: 4px;
      width: calc(@terminal-width - 16px);
      height: @terminal-height;
    }
  }

</style>
