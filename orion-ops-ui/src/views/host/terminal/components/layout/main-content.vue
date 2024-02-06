<template>
  <div class="terminal-content">
    <!-- 内容 tabs -->
    <a-tabs v-if="tabManager.active"
            v-model:active-key="tabManager.active"
            class="main-tabs">
      <a-tab-pane v-for="tab in tabManager.items"
                  :key="tab.key"
                  :title="tab.title">
        <!-- 新建连接 -->
        <new-connection-view v-if="tab.key === TerminalTabs.NEW_CONNECTION.key" />
        <!-- 快捷键设置 -->
        <terminal-shortcut-setting v-else-if="tab.key === TerminalTabs.SHORTCUT_SETTING.key" />
        <!-- 显示设置 -->
        <terminal-display-setting v-else-if="tab.key === TerminalTabs.DISPLAY_SETTING.key" />
        <!-- 主题设置 -->
        <terminal-theme-setting v-else-if="tab.key === TerminalTabs.THEME_SETTING.key" />
        <!-- 终端设置 -->
        <terminal-general-setting v-else-if="tab.key === TerminalTabs.TERMINAL_SETTING.key" />
        <!-- 终端面板 -->
        <terminal-panels-view v-else-if="tab.key === TerminalTabs.TERMINAL_PANEL.key" />
      </a-tab-pane>
    </a-tabs>
    <!-- 承载页推荐 -->
    <empty-recommend v-else />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'mainContent'
  };
</script>

<script lang="ts" setup>
  import { TerminalTabs, TerminalShortcutKeys } from '../../types/terminal.const';
  import { useTerminalStore } from '@/store';
  import { onMounted, onUnmounted, watch } from 'vue';
  import { addEventListen, removeEventListen } from '@/utils/event';
  import EmptyRecommend from './empty-recommend.vue';
  import NewConnectionView from '../new-connection/new-connection-view.vue';
  import TerminalDisplaySetting from '../setting/display/terminal-display-setting.vue';
  import TerminalThemeSetting from '../setting/theme/terminal-theme-setting.vue';
  import TerminalGeneralSetting from '../setting/general/terminal-general-setting.vue';
  import TerminalShortcutSetting from '../setting/shortcut/terminal-shortcut-setting.vue';
  import TerminalPanelsView from '@/views/host/terminal/components/layout/terminal-panels-view.vue';

  const { preference, tabManager, getCurrentSshSession } = useTerminalStore();

  // 监听 tab 切换
  watch(() => tabManager.active, (active, before) => {
    // 失焦 tab
    if (before === TerminalTabs.TERMINAL_PANEL.key) {
      getCurrentSshSession()?.blur();
    }
    // 聚焦 tab
    if (active === TerminalTabs.TERMINAL_PANEL.key) {
      getCurrentSshSession()?.focus();
    }
    // 修改标题
    document.title = Object.values(TerminalTabs)
        .find(s => s.key === active)?.title
      || TerminalTabs.TERMINAL_PANEL.title;
  });

  // 处理全局快捷键逻辑
  const handlerKeyboard = (event: Event) => {
    const e = event as KeyboardEvent;
    // 检测触发的快捷键
    const key = preference.shortcutSetting.keys.find(key => {
      return key.code === e.code
        && key.altKey === e.altKey
        && key.shiftKey === e.shiftKey
        && key.ctrlKey === e.ctrlKey;
    });
    if (!key) {
      return;
    }
    // 触发逻辑
    switch (key.item) {
      case TerminalShortcutKeys.CLOSE_TAB:
        // 关闭 tab
        if (tabManager.active) {
          tabManager.deleteTab(tabManager.active);
        }
        break;
      case TerminalShortcutKeys.CHANGE_TO_PREV_TAB:
        // 切换至前一个 tab
        tabManager.changeToPrevTab();
        break;
      case TerminalShortcutKeys.CHANGE_TO_NEXT_TAB:
        // 切换至后一个 tab
        tabManager.changeToNextTab();
        break;
      case TerminalShortcutKeys.OPEN_NEW_CONNECT_TAB:
        // 切换到新建连接 tab
        tabManager.openTab(TerminalTabs.NEW_CONNECTION);
        break;
      default:
        break;
    }
  };

  // 监听键盘事件
  onMounted(() => {
    if (preference.shortcutSetting.enabled) {
      addEventListen(window, 'keydown', handlerKeyboard);
    }
  });

  // 移除键盘事件
  onUnmounted(() => {
    if (preference.shortcutSetting.enabled) {
      removeEventListen(window, 'keydown', handlerKeyboard);
    }
  });

</script>

<style lang="less" scoped>
  .terminal-content {
    width: 100%;
    height: 100%;
    position: relative;

    :deep(.main-tabs) {
      width: 100%;
      height: 100%;

      > .arco-tabs-nav {
        display: none;
      }

      > .arco-tabs-content {
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
