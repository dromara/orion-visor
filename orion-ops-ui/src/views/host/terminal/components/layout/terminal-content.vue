<template>
  <div class="terminal-content">
    <!-- 内容 tabs -->
    <a-tabs v-model:active-key="terminalStore.dispatcher.active">
      <a-tab-pane v-for="tab in terminalStore.dispatcher.items"
                  :key="tab.key"
                  :title="tab.title">
        <!-- 设置 -->
        <template v-if="tab.type === TabType.SETTING">
          <!-- 新建连接 -->
          <new-connection-view v-if="tab.key === InnerTabs.NEW_CONNECTION.key" />
          <!-- 显示设置 -->
          <terminal-view-setting v-else-if="tab.key === InnerTabs.VIEW_SETTING.key" />
        </template>
        <!-- 终端 -->
        <template v-else-if="tab.type === TabType.TERMINAL">
          <terminal-view :tab="tab" />
        </template>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalContent'
  };
</script>

<script lang="ts" setup>
  import { TabType, InnerTabs } from '../../types/terminal.const';
  import { useTerminalStore } from '@/store';
  import TerminalViewSetting from '../view-setting/terminal-view-setting.vue';
  import NewConnectionView from '../new-connection/new-connection-view.vue';
  import TerminalView from '../xterm/terminal-view.vue';

  const terminalStore = useTerminalStore();

</script>

<style lang="less" scoped>
  .terminal-content {
    width: 100%;
    height: 100%;
    position: relative;

    :deep(.arco-tabs) {
      width: 100%;
      height: 100%;

      .arco-tabs-nav {
        display: none;
      }

      .arco-tabs-content {
        padding: 0;
        width: 100%;
        height: 100%;
        overflow: auto;

        &::-webkit-scrollbar {
          display: none;
        }
      }
    }
  }
</style>
