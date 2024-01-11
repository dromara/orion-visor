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
    <!-- 终端 -->
    <div class="terminal-wrapper"
         :style="{
           background: preference.theme.schema.background
         }">
      <div class="terminal-inst" ref="terminalRef" />
    </div>
    <!-- 命令编辑器 -->
    <shell-editor-modal ref="modal"
                        :closable="false"
                        :body-style="{ padding: '16px' }"
                        :dark="preference.theme.dark"
                        cancel-text="关闭"
                        @ok="writeCommand(modal.getValue())"
                        @cancel="focus" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalView'
  };
</script>

<script lang="ts" setup>
  import type { ITerminalSession, TerminalTabItem, SidebarAction } from '../../types/terminal.type';
  import { computed, onMounted, onUnmounted, ref } from 'vue';
  import { useDictStore, useTerminalStore } from '@/store';
  import useCopy from '@/hooks/copy';
  import { ActionBarItems, connectStatusKey } from '../../types/terminal.const';
  import IconActions from '../layout/icon-actions.vue';
  import ShellEditorModal from '@/components/view/shell-editor/shell-editor-modal.vue';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

  const { copy, readText } = useCopy();
  const { getDictValue } = useDictStore();
  const { preference, tabManager, sessionManager } = useTerminalStore();

  const modal = ref();
  const commandInput = ref();
  const terminalRef = ref();
  const session = ref<ITerminalSession>();

  // FIXME
  // 右键菜单补充
  // 搜索 search color 配置
  // 截屏
  // 主机获取逻辑 最近连接逻辑 偏好逻辑


  // TODO
  // 交互设置
  // 右键选中词条
  // 右键粘贴
  // 启用右键菜单
  // 自动将选中内容复制到剪切板
  // 粘贴时删除空格
  // 复制时删除空格
  // 分隔符  /\()"'-.,:;<>~!@#$%^&*|+=[]{}~?│   在终端中双击文本将使用到这些符号
  // 自动检测 url 并可以点击
  // 支持显示图片 使用 sixel 打开图片

  // 终端设置
  // bell sound
  // terminal emulation type: xterm 256color
  // 回滚（ScrollBack）
  // 保存在缓冲区的行数

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
      session.value.paste(value);
    }
  };

  // 聚焦
  const focus = () => {
    session.value?.focus();
  };

  // 操作禁用状态
  const actionsDisableStatus = computed<Record<string, boolean | undefined>>(() => {
    return {
      paste: session.value?.canWrite,
      interrupt: session.value?.canWrite,
      enter: session.value?.canWrite,
      commandEditor: session.value?.canWrite,
      disconnect: session.value?.connected,
    };
  });

  // 操作点击逻辑
  const actionsClickHandler: Record<string, () => void> = {
    // 去顶部
    toTop: () => session.value?.toTop(),
    // 去底部
    toBottom: () => session.value?.toBottom(),
    // 全选
    checkAll: () => session.value?.selectAll(),
    // 复制选中部分
    copy: () => copy(session.value?.getSelection(), '已复制'),
    // 粘贴
    paste: async () => session.value?.paste(await readText()),
    // ctrl + c
    interrupt: () => session.value?.paste(String.fromCharCode(3)),
    // 回车
    enter: () => session.value?.paste(String.fromCharCode(13)),
    // 命令编辑器
    commandEditor: () => modal.value.open('', ''),
    // 搜索
    search: () => {
    },
    // 增大字号
    fontSizePlus: () => {
      if (session.value) {
        session.value.setOption('fontSize', session.value.getOption('fontSize') + 1);
        if (session.value.connected) {
          session.value.fit();
          session.value.focus();
        }
      }
    },
    // 减小字号
    fontSizeSubtract: () => {
      if (session.value) {
        session.value.setOption('fontSize', session.value.getOption('fontSize') - 1);
        if (session.value.connected) {
          session.value.fit();
          session.value.focus();
        }
      }
    },
    // 清空
    clear: () => session.value?.clear(),
    // 断开连接
    disconnect: () => session.value?.disconnect(),
    // 关闭
    close: () => tabManager.deleteTab(props.tab.key),
  };

  // 右侧操作
  const rightActions = computed<Array<SidebarAction>>(() => {
    return ActionBarItems.map(s => {
      return {
        icon: s.icon,
        content: s.content,
        visible: preference.actionBarSetting[s.item] !== false,
        disabled: actionsDisableStatus.value[s.item] !== false,
        click: () => {
          actionsClickHandler[s.item] && actionsClickHandler[s.item]();
        }
      };
    });
  });

  // 初始化会话
  onMounted(async () => {
    // 创建终端处理器
    session.value = await sessionManager.openSession(props.tab, terminalRef.value);
  });

  // 会话
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
