<template>
  <div class="terminal-container">
    <!-- 头部 -->
    <div class="terminal-header"
         :style="{
           background: preference.theme.headerBackgroundColor
         }">
      <!-- 左侧操作 -->
      <div class="terminal-header-left">
        <!-- 主机地址 -->
        <span class="address-wrapper">
          {{ tab.address }}
          <span class="address-copy copy-right" title="复制" @click="copy(tab.address as string)">
            <icon-copy />
          </span>
        </span>
      </div>
      <!-- 右侧操作 -->
      <div class="terminal-header-right">
        <!-- 命令输入框 -->
        <a-textarea class="command-input mr8"
                    v-if="preference.actionBarSetting.commandInput !== false"
                    v-model="commandInput"
                    :auto-size="{ minRows: 1, maxRows: 1 }"
                    placeholder="F8 发送命令"
                    allow-clear
                    @keyup="writeCommandInput" />
        <!-- 操作按钮 -->
        <icon-actions class="terminal-header-right-action-bar"
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
    <terminal-context-menu :session="session"
                           @click="doTerminalHandle">
      <!-- 终端容器 -->
      <div class="terminal-wrapper"
           :style="{ background: preference.theme.schema.background }">
        <!-- 终端实例 -->
        <div class="terminal-inst" ref="terminalRef" />
        <!-- 搜索模态框 -->
        <terminal-search-modal ref="searchModal"
                               @find="findWords"
                               @close="focus" />
      </div>
    </terminal-context-menu>
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
    name: 'terminalView'
  };
</script>

<script lang="ts" setup>
  import type { ITerminalSession, TerminalTabItem, SidebarAction, ITerminalSessionHandler } from '../../types/terminal.type';
  import { computed, onMounted, onUnmounted, ref } from 'vue';
  import { useDictStore, useTerminalStore } from '@/store';
  import useCopy from '@/hooks/copy';
  import { ActionBarItems, connectStatusKey } from '../../types/terminal.const';
  import IconActions from '../layout/icon-actions.vue';
  import ShellEditorModal from '@/components/view/shell-editor/shell-editor-modal.vue';
  import TerminalSearchModal from './terminal-search-modal.vue';
  import TerminalContextMenu from './terminal-context-menu.vue';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

  const { copy } = useCopy();
  const { getDictValue } = useDictStore();
  const { preference, sessionManager } = useTerminalStore();

  const editorModal = ref();
  const searchModal = ref();
  const commandInput = ref();
  const terminalRef = ref();
  const session = ref<ITerminalSession>();

  // TODO
  // 设置快捷键
  // 截屏
  // sftp

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
    session.value = await sessionManager.openSession(props.tab, {
      el: terminalRef.value,
      editorModal: editorModal.value,
      searchModal: searchModal.value
    });
  });

  // 关闭会话
  onUnmounted(() => {
    sessionManager.closeSession(props.tab.key);
  });

</script>

<style lang="less" scoped>
  @terminal-header-height: 36px;

  .terminal-container {
    width: 100%;
    height: calc(100vh - var(--header-height));
    position: relative;
  }

  .terminal-header {
    width: 100%;
    height: @terminal-header-height;
    padding: 0 6px;
    display: flex;
    align-items: center;
    justify-content: space-between;

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
      }
    }

    &-right-action-bar {
      display: flex;

      :deep(.terminal-sidebar-icon-wrapper) {
        width: 30px;
        height: 30px;
      }

      :deep(.terminal-sidebar-icon) {
        width: 28px;
        height: 28px;
        font-size: 20px;
      }
    }

    .status-bridge {
      margin: 0 2px 0 8px;
      user-select: none;

      :deep(.arco-badge-status-text) {
        width: 36px;
      }
    }
  }

  .terminal-wrapper {
    width: 100%;
    height: calc(100% - @terminal-header-height);
    position: relative;
    padding: 6px 0 0 6px;

    .terminal-inst {
      width: 100%;
      height: 100%;

      ::-webkit-scrollbar-track {
        display: none;
      }
    }
  }

</style>
