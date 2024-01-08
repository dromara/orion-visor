<template>
  <div class="terminal-container">
    <!-- 头部 -->
    <div class="terminal-header"
         :style="{
           background: adjustColor(themeSchema.background, -12)
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
        <!-- 代码输入框 -->
        <a-textarea class="command-input mr8"
                    v-model="commandInput"
                    :auto-size="{ minRows: 1, maxRows: 1 }"
                    placeholder="F8 发送命令"
                    allow-clear
                    @keyup="writeCommandInput" />
        <!-- 操作按钮 -->
        <icon-actions class="terminal-header-right-icon-actions"
                      :actions="rightActions"
                      position="bottom" />
        <!-- 状态 -->
        <a-badge style="margin: 0 2px 0 8px;"
                 :status="getDictValue(connectStatusKey, session ? session.status : 0, 'status')"
                 :text="getDictValue(connectStatusKey, session ? session.status : 0)" />
      </div>
    </div>
    <!-- 终端 -->
    <div class="terminal-wrapper"
         :style="{
           background: themeSchema.background
         }">
      <div class="terminal-inst" ref="terminalRef" />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalView'
  };
</script>

<script lang="ts" setup>
  import type { ITerminalSession, TerminalTabItem } from '../../types/terminal.type';
  import { computed, onMounted, onUnmounted, ref } from 'vue';
  import { useDictStore, useTerminalStore } from '@/store';
  import useCopy from '@/hooks/copy';
  import IconActions from '@/views/host/terminal/components/layout/icon-actions.vue';
  import { connectStatusKey, SidebarAction } from '@/views/host/terminal/types/terminal.const';
  import { adjustColor } from '@/utils';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

  const { copy, readText } = useCopy();
  const { getDictValue } = useDictStore();
  const { preference, sessionManager } = useTerminalStore();

  const commandInput = ref();
  const themeSchema = preference.themeSchema;
  const terminalRef = ref();
  const session = ref<ITerminalSession>();

  // FIXME
  // 命令编辑器 搜索
  // (改成可配置/拆分)
  // 自定义 font siderBar 颜色, 集成到主题里面, 现在的问题是切换主题字体颜色就变了
  // 右键菜单补充
  // 搜索插件, link插件

  // 发送命令
  const writeCommandInput = async (e: KeyboardEvent) => {
    const value = commandInput.value;
    if (value && e.code === 'F8' && session.value?.canWrite) {
      session.value.paste(value);
      commandInput.value = undefined;
    }
  };

  // 右侧操作
  const rightActions = computed<Array<SidebarAction>>(() => [
    {
      icon: 'icon-up',
      content: '去顶部',
      click: () => {
        session.value?.toTop();
      }
    }, {
      icon: 'icon-down',
      content: '去底部',
      click: () => {
        session.value?.toBottom();
      }
    }, {
      icon: 'icon-expand',
      content: '全选',
      click: () => {
        session.value?.selectAll();
      }
    }, {
      icon: 'icon-copy',
      content: '复制选中部分',
      click: () => {
        copy(session.value?.getSelection(), '已复制');
      }
    }, {
      icon: 'icon-paste',
      content: '粘贴',
      disabled: session.value?.canWrite,
      click: async () => {
        session.value?.paste(await readText());
      }
    }, {
      icon: 'icon-formula',
      content: 'ctrl + c',
      disabled: session.value?.canWrite,
      click: () => {
        session.value?.paste(String.fromCharCode(3));
      }
    }, {
      icon: 'icon-play-arrow-fill',
      content: '回车',
      disabled: session.value?.canWrite,
      click: () => {
        session.value?.paste(String.fromCharCode(13));
      }
    }, {
      icon: 'icon-code-square',
      content: '命令编辑器',
      disabled: session.value?.canWrite,
      click: () => {
      }
    }, {
      icon: 'icon-search',
      content: '搜索',
      click: () => {
      }
    }, {
      icon: 'icon-zoom-in',
      content: '增大字号',
      click: () => {
        if (session.value) {
          session.value.setOption('fontSize', session.value.getOption('fontSize') + 1);
          if (session.value.connected) {
            session.value.fit();
            session.value.focus();
          }
        }
      }
    }, {
      icon: 'icon-zoom-out',
      content: '减小字号',
      click: () => {
        if (session.value) {
          session.value.setOption('fontSize', session.value.getOption('fontSize') - 1);
          if (session.value.connected) {
            session.value.fit();
            session.value.focus();
          }
        }
      }
    }, {
      icon: 'icon-delete',
      content: '清空',
      click: () => {
        session.value?.clear();
      }
    }, {
      icon: 'icon-poweroff',
      content: '关闭',
      disabled: session.value?.connected,
      click: () => {
        session.value?.logout();
      }
    },
  ]);

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
      width: 100%;
      height: 100%;
    }

    &-left {
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
      justify-content: flex-end;

      .command-input {
        width: 36%;
      }
    }

    &-right-icon-actions {
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
  }

  .terminal-wrapper {
    width: 100%;
    height: calc(100% - @terminal-header-height);
    position: relative;
    padding: 6px 0 0 6px;

    .terminal-inst {
      width: 100%;
      height: 100%;

      ::-webkit-scrollbar {
        display: none;
      }
    }
  }

</style>
