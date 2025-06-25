<template>
  <div class="ssh-container">
    <!-- 头部 -->
    <ssh-header :session="session" @handle="doTerminalHandle" />
    <!-- 终端右键菜单 -->
    <ssh-context-menu :session="session" @handle="doTerminalHandle">
      <!-- 终端容器 -->
      <div class="ssh-wrapper"
           :style="{ background: preference.theme.schema.background }">
        <!-- 终端实例 -->
        <div class="ssh-viewport" ref="viewport" />
        <!-- 搜索模态框 -->
        <xterm-search-modal ref="searchModal"
                            class="search-modal"
                            @find="findWords"
                            @close="focus" />
      </div>
    </ssh-context-menu>
    <!-- 上传文件模态框 -->
    <sftp-upload-modal ref="uploadModal" @closed="focus" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'sshView'
  };
</script>

<script lang="ts" setup>
  import type { ISshSession, TerminalSessionTabItem } from '@/views/terminal/interfaces';
  import { onMounted, onUnmounted, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { TerminalSessionTypes } from '@/views/terminal/types/const';
  import SshHeader from './ssh-header.vue';
  import SshContextMenu from './ssh-context-menu.vue';
  import SftpUploadModal from '../sftp/sftp-upload-modal.vue';
  import XtermSearchModal from '@/components/xterm/search-modal/index.vue';

  const props = defineProps<{
    item: TerminalSessionTabItem;
  }>();

  const { preference, sessionManager } = useTerminalStore();

  const viewport = ref();
  const searchModal = ref();
  const uploadModal = ref();
  const session = ref<ISshSession>();

  // 聚焦
  const focus = () => {
    session.value?.focus();
  };

  // 查询关键字
  const findWords = (word: string, next: boolean, options: any) => {
    session.value?.find(word, next, options);
  };

  // 执行终端操作
  const doTerminalHandle = (handle: string) => {
    session.value?.handler.invokeHandle.call(session.value?.handler, handle);
  };

  // 初始化会话
  onMounted(async () => {
    // 创建终端会话
    session.value = sessionManager.createSession<ISshSession>(props.item);
    // 获取 SSH 会话数量
    const sshSessionLen = sessionManager.sessions.filter(s => s?.type === TerminalSessionTypes.SSH.type).length;
    // 检查 webgl 数量
    const webglAvailable = (sshSessionLen || 0) <= (navigator.hardwareConcurrency || 0) / 2;
    // 打开终端会话
    await sessionManager.openSsh(props.item, {
      viewport: viewport.value,
      searchModal: searchModal.value,
      uploadModal: uploadModal.value,
      webglAvailable,
    });
  });

  // 关闭会话
  onUnmounted(() => {
    if (props.item.key) {
      sessionManager.closeSession(props.item.key);
    }
  });

</script>

<style lang="less" scoped>
  @ssh-header-height: 36px;

  .ssh-container {
    width: 100%;
    height: 100%;
    position: relative;
  }

  .ssh-wrapper {
    width: 100%;
    height: calc(100% - @ssh-header-height);
    position: relative;
    padding: 8px 4px 4px 8px;

    .ssh-viewport {
      width: 100%;
      height: 100%;

      ::-webkit-scrollbar-track {
        display: none;
      }
    }
  }

  .search-modal {
    --bg-focus: var(--search-bg-focus);
    --bg: var(--search-bg);
    --color-text: var(--search-color-text);
    --color-text-focus: var(--search-color-text-focus);
    --bg-icon-hover: var(--search-bg-icon-hover);
    --bg-icon-hover-focus: var(--search-bg-icon-hover-focus);
    --bg-icon-selected: var(--search-bg-icon-selected);
    --bg-icon-selected-focus: var(--search-bg-icon-selected-focus);
  }

</style>
