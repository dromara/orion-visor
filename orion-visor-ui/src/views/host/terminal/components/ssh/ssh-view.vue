<template>
  <div class="ssh-container">
    <!-- 头部 -->
    <ssh-header :address="tab.address"
                :session="session"
                @handle="doTerminalHandle" />
    <!-- 终端右键菜单 -->
    <ssh-context-menu :session="session"
                      @handle="doTerminalHandle">
      <!-- 终端容器 -->
      <div class="ssh-wrapper"
           :style="{ background: preference.theme.schema.background }">
        <!-- 终端实例 -->
        <div class="ssh-inst" ref="terminalRef" />
        <!-- 搜索模态框 -->
        <xterm-search-modal ref="searchModal"
                            class="search-modal"
                            @find="findWords"
                            @close="focus" />

      </div>
    </ssh-context-menu>
    <!-- 上传文件模态框 -->
    <sftp-upload-modal ref="uploadModal" @closed="focus" />
    <!-- 命令编辑器 -->
    <shell-editor-modal ref="editorModal"
                        :closable="false"
                        :body-style="{ padding: '16px' }"
                        :dark="preference.theme.dark"
                        cancel-text="关闭"
                        @ok="writeCommand(editorModal.getValue())"
                        @cancel="focus" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'sshView'
  };
</script>

<script lang="ts" setup>
  import type { ISshSession, TerminalPanelTabItem } from '../../types/define';
  import { onMounted, onUnmounted, ref } from 'vue';
  import { useDictStore, useTerminalStore } from '@/store';
  import SshHeader from './ssh-header.vue';
  import ShellEditorModal from '@/components/view/shell-editor/modal/index.vue';
  import SshContextMenu from './ssh-context-menu.vue';
  import SftpUploadModal from '../sftp/sftp-upload-modal.vue';
  import XtermSearchModal from '@/components/xterm/search-modal/index.vue';

  const props = defineProps<{
    tab: TerminalPanelTabItem;
  }>();

  const { getDictValue } = useDictStore();
  const { preference, sessionManager } = useTerminalStore();

  const editorModal = ref();
  const searchModal = ref();
  const uploadModal = ref();
  const terminalRef = ref();
  const session = ref<ISshSession>();

  // 发送命令
  const writeCommand = (value: string) => {
    if (session.value?.canWrite) {
      session.value?.handler.pasteTrimEnd(value);
    }
  };

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
    // 创建终端处理器
    session.value = await sessionManager.openSsh(props.tab, {
      el: terminalRef.value,
      editorModal: editorModal.value,
      searchModal: searchModal.value,
      uploadModal: uploadModal.value,
    });
  });

  // 关闭会话
  onUnmounted(() => {
    sessionManager.closeSession(props.tab.sessionId);
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

    .ssh-inst {
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
