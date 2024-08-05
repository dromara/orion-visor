<template>
  <div class="ssh-container">
    <!-- 头部 -->
    <div class="ssh-header">
      <!-- 左侧操作 -->
      <div class="ssh-header-left">
        <!-- 主机地址 -->
        <span class="address-wrapper">
          {{ tab.address }}
          <span class="address-copy copy-right" title="复制" @click="copy(tab.address as string)">
            <icon-copy />
          </span>
        </span>
      </div>
      <!-- 右侧操作 -->
      <div class="ssh-header-right">
        <!-- 命令输入框 -->
        <a-textarea class="command-input mr8"
                    v-if="preference.actionBarSetting.commandInput !== false"
                    v-model="commandInput"
                    :auto-size="{ minRows: 1, maxRows: 1 }"
                    placeholder="F8 发送命令"
                    allow-clear
                    @keyup="writeCommandInput" />
        <!-- 操作按钮 -->
        <icon-actions class="ssh-header-right-action-bar"
                      wrapper-class="ssh-header-icon-wrapper"
                      icon-class="ssh-header-icon"
                      :actions="rightActions"
                      position="bottom" />
        <!-- 连接状态 -->
        <a-badge v-if="preference.actionBarSetting.connectStatus !== false"
                 class="status-bridge"
                 :status="getDictValue(connectStatusKey, session ? session.status : 0, 'status')"
                 :text="getDictValue(connectStatusKey, session ? session.status : 0)" />
      </div>
    </div>
    <!-- 终端右键菜单 -->
    <ssh-context-menu :session="session"
                      @click="doTerminalHandle">
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
        <!-- 上传文件模态框 -->
        <sftp-upload-modal ref="uploadModal" @closed="focus" />
      </div>
    </ssh-context-menu>
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
  import type { ISshSession, TerminalPanelTabItem, SidebarAction } from '../../types/define';
  import { computed, onMounted, onUnmounted, ref } from 'vue';
  import { useDictStore, useTerminalStore } from '@/store';
  import { copy } from '@/hooks/copy';
  import { ActionBarItems, connectStatusKey } from '../../types/const';
  import ShellEditorModal from '@/components/view/shell-editor/modal/index.vue';
  import IconActions from '../layout/icon-actions.vue';
  import SshContextMenu from './ssh-context-menu.vue';
  import SftpUploadModal from '../sftp/sftp-upload-modal.vue';
  import XtermSearchModal from '@/components/xtrem/search-modal/index.vue';

  const props = defineProps<{
    tab: TerminalPanelTabItem;
  }>();

  const { getDictValue } = useDictStore();
  const { preference, sessionManager } = useTerminalStore();

  const editorModal = ref();
  const searchModal = ref();
  const uploadModal = ref();
  const commandInput = ref();
  const terminalRef = ref();
  const session = ref<ISshSession>();

  // 发送命令
  const writeCommandInput = async (e: KeyboardEvent) => {
    const value = commandInput.value;
    if (value && e.code === 'F8') {
      writeCommand(value);
      commandInput.value = undefined;
    }
  };

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

  // 右侧操作
  const rightActions = computed<Array<SidebarAction>>(() => {
    return ActionBarItems.map(s => {
      return {
        icon: s.icon,
        content: s.content,
        visible: preference.actionBarSetting[s.item] !== false,
        disabled: session.value?.handler.enabledStatus(s.item) === false,
        click: () => doTerminalHandle(s.item)
      };
    });
  });

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

  .ssh-header {
    width: 100%;
    height: @ssh-header-height;
    padding: 0 8px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: var(--color-bg-panel-bar);

    &-left, &-right {
      display: flex;
      align-items: center;
      height: 100%;
    }

    &-left {
      width: 34%;

      .address-wrapper {
        height: 100%;
        display: inline-flex;
        align-items: center;
        user-select: none;

        .address-copy {
          display: none;
        }

        &:hover {
          .address-copy {
            display: unset;
          }
        }

        &:before {
          content: 'IP:';
          padding-right: 4px;
        }
      }
    }

    &-right {
      width: 66%;
      justify-content: flex-end;

      .command-input {
        width: 36%;
        user-select: none;
      }
    }

    &-right-action-bar {
      display: flex;

      :deep(.ssh-header-icon-wrapper) {
        width: 28px;
        height: 28px;
        margin: 0 2px;
      }

      :deep(.ssh-header-icon) {
        width: 28px;
        height: 28px;
        font-size: 20px;
      }
    }

    .status-bridge {
      height: 100%;
      margin: 0 2px 0 8px;
      display: flex;
      align-items: center;
      user-select: none;

      :deep(.arco-badge-status-text) {
        width: 36px;
      }

      &::before {
        content: "";
        height: 56%;
        margin: 0 12px 0 6px;
        border-left: 2px solid var(--color-fill-4);
        border-radius: 2px;
      }
    }
  }

  .ssh-wrapper {
    width: 100%;
    height: calc(100% - @ssh-header-height);
    position: relative;
    padding: 8px 2px 2px 8px;

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
