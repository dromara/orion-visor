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

  const { preference, tabManager, sessionManager } = useTerminalStore();

  // fixme title逻辑 失焦逻辑
  // 监听 tab 修改
  watch(() => tabManager.active, (active, before) => {
    if (before) {
      // 失焦已经切换的终端
      const beforeTab = tabManager.items.find(s => s.key === before);
      if (beforeTab && beforeTab?.type === 'TerminalTabType.TERMINAL') {
        sessionManager.getSession(before)?.blur();
      }
    }
    if (active) {
      // 获取 activeTab
      const activeTab = tabManager.getCurrentTab();
      if (!activeTab) {
        return;
      }
      // 修改标题
      document.title = activeTab.title;
      // 终端自动聚焦
      if (activeTab?.type === 'TerminalTabType.TERMINAL') {
        sessionManager.getSession(active)?.focus();
      }
    } else {
      // 修改标题
      document.title = '主机终端';
    }
  });

  // 处理快捷键逻辑
  const handlerKeyboard = (event: Event) => {
    // 当前页面非 terminal 的时候再触发快捷键 (terminal 有内置逻辑)
    // fixme panel 无数据继续触发
    if (tabManager.getCurrentTab()?.key === TerminalTabs.TERMINAL_PANEL.key) {
      return;
    }
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
