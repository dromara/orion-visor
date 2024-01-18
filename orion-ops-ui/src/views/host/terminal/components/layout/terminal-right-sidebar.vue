<template>
  <div class="terminal-right-sidebar">
    <!-- 顶部操作按钮 -->
    <icon-actions class="top-actions"
                  :actions="topActions"
                  position="left" />
    <!-- 底部操作按钮 -->
    <icon-actions class="bottom-actions"
                  :actions="bottomActions"
                  position="left" />
    <!-- 代码片段 -->
    <snippet-drawer ref="snippetRef" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalRightSidebar'
  };
</script>

<script lang="ts" setup>
  import type { SidebarAction } from '../../types/terminal.type';
  import { useTerminalStore } from '@/store';
  import { TerminalTabType } from '../../types/terminal.const';
  import { Message } from '@arco-design/web-vue';
  import { ref } from 'vue';
  import IconActions from './icon-actions.vue';
  import SnippetDrawer from '../snippet/snippet-drawer.vue';

  const emits = defineEmits(['openSftp', 'openTransfer']);

  const { tabManager, sessionManager } = useTerminalStore();

  const snippetRef = ref();

  // 顶部操作
  const topActions = [
    {
      icon: 'icon-code',
      content: '打开命令片段',
      click: () => snippetRef.value.open()
    },
    {
      icon: 'icon-folder',
      content: '打开 SFTP',
      click: () => emits('openSftp')
    },
    {
      icon: 'icon-swap',
      content: '文件传输列表',
      iconStyle: {
        transform: 'rotate(90deg)'
      },
      click: () => emits('openTransfer')
    },
  ];

  // 底部操作
  const bottomActions: Array<SidebarAction> = [
    {
      icon: 'icon-camera',
      content: '截图',
      click: () => screenshot()
    },
  ];

  // 终端截屏
  const screenshot = () => {
    const tab = tabManager.getCurrentTab();
    if (!tab || tab.type !== TerminalTabType.TERMINAL) {
      Message.warning('请切换到终端标签页');
      return;
    }
    // 获取处理器并截图
    sessionManager.getSession(tab.key)
      ?.handler
      ?.screenshot();
  };

</script>

<style lang="less" scoped>
  .terminal-right-sidebar {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
</style>
