<template>
  <div class="main-content">
    <!-- 内容部分 -->
    <div class="main-content-wrapper" :style="{ height: `calc(100% - ${mainSubtractHeight})` }">
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
    <!-- 底部发送命令 -->
    <command-bar v-if="layoutState.commandBar && tabManager.active === TerminalTabs.TERMINAL_PANEL.key" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'mainContent'
  };
</script>

<script lang="ts" setup>
  import type { ISshSession } from '../../types/define';
  import { TerminalTabs, TerminalShortcutKeys, PanelSessionType } from '../../types/const';
  import { useTerminalStore } from '@/store';
  import { computed, onMounted, onUnmounted, watch } from 'vue';
  import { addEventListen, removeEventListen } from '@/utils/event';
  import EmptyRecommend from './empty-recommend.vue';
  import TerminalPanelsView from './terminal-panels-view.vue';
  import NewConnectionView from '../new-connection/new-connection-view.vue';
  import TerminalDisplaySetting from '../setting/display/terminal-display-setting.vue';
  import TerminalThemeSetting from '../setting/theme/terminal-theme-setting.vue';
  import TerminalGeneralSetting from '../setting/general/terminal-general-setting.vue';
  import TerminalShortcutSetting from '../setting/shortcut/terminal-shortcut-setting.vue';
  import CommandBar from '../command-bar/index.vue';

  const emits = defineEmits(['openCommandSnippet', 'openPathBookmark', 'openTransferList', 'openCommandBar', 'screenshot']);

  const { layoutState, preference, tabManager, getCurrentSession, sessionManager } = useTerminalStore();

  // 内容部分减去的高度
  const mainSubtractHeight = computed(() => {
    let height = 0;
    // 底部发送命令高度
    if (layoutState.commandBar && tabManager.active === TerminalTabs.TERMINAL_PANEL.key) {
      height += 128;
    }
    // 自适应
    setTimeout(() => {
      sessionManager.dispatchResize();
    }, 200);
    return `${height}px`;
  });

  // 监听 tab 切换
  watch(() => tabManager.active, (active, before) => {
    // 失焦 tab
    if (before === TerminalTabs.TERMINAL_PANEL.key) {
      getCurrentSession<ISshSession>(PanelSessionType.SSH.type)?.blur();
    }
    // 聚焦 tab
    if (active === TerminalTabs.TERMINAL_PANEL.key) {
      getCurrentSession<ISshSession>(PanelSessionType.SSH.type)?.focus();
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
      case TerminalShortcutKeys.OPEN_COMMAND_SNIPPET:
        // 打开命令片段
        emits('openCommandSnippet');
        break;
      case TerminalShortcutKeys.OPEN_PATH_BOOKMARK:
        // 打开书签路径
        emits('openPathBookmark');
        break;
      case TerminalShortcutKeys.OPEN_TRANSFER_LIST:
        // 打开文件传输列表
        emits('openTransferList');
        break;
      case TerminalShortcutKeys.OPEN_COMMAND_BAR:
        // 打开发送命令
        emits('openCommandBar');
        break;
      case TerminalShortcutKeys.SCREENSHOT:
        // 截图
        emits('screenshot');
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
  .main-content {
    width: 100%;
    height: 100%;
    position: relative;

    &-wrapper {
      width: 100%;
      height: 100%;
    }

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

        .arco-tabs-content-list, .arco-tabs-pane {
          width: 100%;
          height: 100%;
        }
      }
    }
  }

</style>
