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
    <!-- 命令片段列表抽屉 -->
    <command-snippet-list-drawer ref="snippetRef" />
    <!-- 路径书签列表抽屉 -->
    <path-bookmark-list-drawer ref="pathRef" />
    <!-- 传输列表 -->
    <transfer-drawer ref="transferRef" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'rightSidebar'
  };
</script>

<script lang="ts" setup>
  import type { ISshSession, SidebarAction } from '../../types/terminal.type';
  import { useTerminalStore } from '@/store';
  import { ref } from 'vue';
  import { PanelSessionType } from '../../types/terminal.const';
  import IconActions from './icon-actions.vue';
  import CommandSnippetListDrawer from '@/views/host/command-snippet/components/command-snippet-list-drawer.vue';
  import PathBookmarkListDrawer from '@/views/host/path-bookmark/components/path-bookmark-list-drawer.vue';
  import TransferDrawer from '@/views/host/terminal/components/transfer/transfer-drawer.vue';

  const { getCurrentSession } = useTerminalStore();

  const snippetRef = ref();
  const pathRef = ref();
  const transferRef = ref();

  // 顶部操作
  const topActions = [
    {
      icon: 'icon-code-block',
      content: '打开命令片段',
      click: () => snippetRef.value.open()
    }, {
      icon: 'icon-bookmark',
      content: '打开路径书签',
      click: () => pathRef.value.open()
    }, {
      icon: 'icon-swap',
      content: '文件传输列表',
      iconStyle: {
        transform: 'rotate(90deg)'
      },
      click: () => transferRef.value.open()
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
    const handler = getCurrentSession<ISshSession>(PanelSessionType.SSH.type, true)?.handler;
    if (handler && handler.enabledStatus('screenshot')) {
      handler.screenshot();
    }
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
