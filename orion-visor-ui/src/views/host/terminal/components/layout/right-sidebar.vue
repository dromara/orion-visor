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
  </div>
</template>

<script lang="ts">
  export default {
    name: 'rightSidebar'
  };
</script>

<script lang="ts" setup>
  import type { SidebarAction } from '../../types/define';
  import IconActions from './icon-actions.vue';

  const emits = defineEmits(['openCommandSnippet', 'openPathBookmark', 'openTransferList', 'openCommandBar', 'screenshot']);

  // 顶部操作
  const topActions = [
    {
      icon: 'icon-code-block',
      content: '打开命令片段',
      click: () => emits('openCommandSnippet')
    }, {
      icon: 'icon-bookmark',
      content: '打开路径书签',
      click: () => emits('openPathBookmark')
    }, {
      icon: 'icon-swap',
      content: '文件传输列表',
      iconStyle: {
        transform: 'rotate(90deg)'
      },
      click: () => emits('openTransferList')
    },
  ];

  // 底部操作
  const bottomActions: Array<SidebarAction> = [
    {
      icon: 'icon-send',
      content: '发送命令',
      click: () => emits('openCommandBar')
    }, {
      icon: 'icon-camera',
      content: '截图',
      click: () => emits('screenshot')
    },
  ];

</script>

<style lang="less" scoped>
  .terminal-right-sidebar {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
</style>
