<template>
  <div class="terminal-content">
    <!-- 内容 tabs -->
    <a-tabs v-if="tabManager.active"
            v-model:active-key="tabManager.active">
      <a-tab-pane v-for="tab in tabManager.items"
                  :key="tab.key"
                  :title="tab.title">
        <!-- 设置 -->
        <template v-if="tab.type === TerminalTabType.SETTING">
          <!-- 新建连接 -->
          <new-connection-view v-if="tab.key === InnerTabs.NEW_CONNECTION.key" />
          <!-- 显示设置 -->
          <terminal-display-setting v-else-if="tab.key === InnerTabs.DISPLAY_SETTING.key" />
          <!-- 主题设置 -->
          <terminal-theme-setting v-else-if="tab.key === InnerTabs.THEME_SETTING.key" />
          <!-- 终端设置 -->
          <terminal-general-setting v-else-if="tab.key === InnerTabs.TERMINAL_SETTING.key" />
        </template>
        <!-- 终端 -->
        <template v-else-if="tab.type === TerminalTabType.TERMINAL">
          <terminal-view :tab="tab" />
        </template>
      </a-tab-pane>
    </a-tabs>
    <!-- 承载页推荐 -->
    <empty-recommend v-else />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalContent'
  };
</script>

<script lang="ts" setup>
  import { TerminalTabType, InnerTabs } from '../../types/terminal.const';
  import { useTerminalStore } from '@/store';
  import { watch } from 'vue';
  import EmptyRecommend from './empty-recommend.vue';
  import NewConnectionView from '../new-connection/new-connection-view.vue';
  import TerminalDisplaySetting from '../view-setting/terminal-display-setting.vue';
  import TerminalThemeSetting from '../view-setting/terminal-theme-setting.vue';
  import TerminalGeneralSetting from '../view-setting/terminal-general-setting.vue';
  import TerminalView from '../xterm/terminal-view.vue';

  const { tabManager, sessionManager } = useTerminalStore();

  // 监听 tab 修改
  watch(() => tabManager.active, active => {
    if (!active) {
      // 修改标题
      document.title = '主机终端';
      return;
    }
    // 获取 tab
    const tab = tabManager.items.find(s => s.key === active);
    if (!tab) {
      return;
    }
    // 修改标题
    document.title = tab.title;
    // terminal 自动聚焦
    if (tab?.type === TerminalTabType.TERMINAL) {
      sessionManager.getSession(active)?.focus();
    }
  });

  // TODO 快捷键逻辑 主机加载逻辑 加载中逻辑

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
