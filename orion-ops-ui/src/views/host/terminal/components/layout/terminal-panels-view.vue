<template>
  <div class="terminal-panels-container">
    <!-- 终端面板 -->
    <terminal-panel v-for="(panel, index) in panelManager.panels"
                    :key="index"
                    :index="index"
                    :panel="panel"
                    @open-new-connect="openHostModal"
                    @close="closePanel" />
    <!-- 新建连接模态框 -->
    <host-list-modal ref="hostModal" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalPanelView'
  };
</script>

<script lang="ts" setup>
  import { useTerminalStore } from '@/store';
  import { ref, onMounted, onUnmounted } from 'vue';
  import { TerminalShortcutKeys, TerminalTabs } from '../../types/terminal.const';
  import { addEventListen, removeEventListen } from '@/utils/event';
  import TerminalPanel from './terminal-panel.vue';
  import HostListModal from '../new-connection/host-list-modal.vue';

  const { preference, tabManager, panelManager, copyTerminalSession } = useTerminalStore();

  const hostModal = ref();

  // 打开主机模态框
  const openHostModal = (index: number) => {
    hostModal.value.open(index);
  };

  // 移除面板
  const closePanel = (index: number) => {
    panelManager.getPanel(index)?.clear();
    if (panelManager.panels.length == 1) {
      // 关闭 tab
      tabManager.deleteTab(TerminalTabs.TERMINAL_PANEL.key);
    } else {
      // 关闭面板
      panelManager.removePanel(index);
    }
  };

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
      case TerminalShortcutKeys.OPEN_NEW_CONNECT_MODAL:
        // 打开新建连接弹框
        hostModal.value.open(panelManager.active);
        break;
      case TerminalShortcutKeys.COPY_TERMINAL:
        // 复制终端
        const hostId = panelManager.getCurrentPanel().getCurrentTab()?.hostId;
        if (hostId) {
          copyTerminalSession(hostId, panelManager.active);
        }
        break;
      case TerminalShortcutKeys.CLOSE_TERMINAL:
        // 关闭终端
        const panel = panelManager.getCurrentPanel();
        if (panel.active) {
          panel.deleteTab(panel.active);
        }
        break;
      case TerminalShortcutKeys.CHANGE_TO_PREV_TERMINAL:
        // 切换至前一个终端
        panelManager.getCurrentPanel().changeToPrevTab();
        break;
      case TerminalShortcutKeys.CHANGE_TO_NEXT_TERMINAL:
        // 切换至后一个终端
        panelManager.getCurrentPanel().changeToNextTab();
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

  // 卸载回调
  onUnmounted(() => {
    // 清空面板
    panelManager.reset();
    // 移除键盘事件
    if (preference.shortcutSetting.enabled) {
      removeEventListen(window, 'keydown', handlerKeyboard);
    }
  });

</script>

<style lang="less" scoped>
  .terminal-panels-container {
    width: 100%;
    height: calc(100vh - var(--header-height));
    position: relative;
    display: flex;
  }
</style>
