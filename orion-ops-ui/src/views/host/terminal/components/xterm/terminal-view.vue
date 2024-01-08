<template>
  <div class="terminal-container">
    <!-- 头部 -->
    <div class="terminal-header"
         :style="{
           background: adjustColor(preference.themeSchema.background, -10)
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
        <!-- 操作按钮 -->
        <icon-actions class="terminal-header-right-icon-actions"
                      :actions="rightActions"
                      position="bottom" />
      </div>
    </div>
    <!-- 终端 -->
    <div class="terminal-wrapper"
         :style="{
           background: preference.themeSchema.background
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
  import { onMounted, onUnmounted, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import useCopy from '@/hooks/copy';
  import IconActions from '@/views/host/terminal/components/layout/icon-actions.vue';
  import { SidebarAction } from '@/views/host/terminal/types/terminal.const';
  import { adjustColor } from '@/utils';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

  const { copy, readText } = useCopy();
  const { preference, sessionManager } = useTerminalStore();

  const terminalRef = ref();
  const session = ref<ITerminalSession>();

  // FIXME
  // 最近连接记录
  // 防止 background 自动变更
  // 去顶部 去底部 ctrl+c 重新连接 command-input 状态
  // (未连接禁用点击)
  // (改成可配置)

  // 右侧操作
  const rightActions: Array<SidebarAction> = [
    {
      icon: 'icon-expand',
      content: '全选',
      click: () => {
        session.value?.selectAll();
        session.value?.focus();
      }
    }, {
      icon: 'icon-copy',
      content: '复制选中部分',
      click: () => {
        copy(session.value?.getSelection(), '已复制');
        session.value?.focus();
      }
    }, {
      icon: 'icon-paste',
      content: '粘贴',
      click: async () => {
        if (session.value?.canWrite) {
          session.value?.paste(await readText());
        }
      }
    }, {
      icon: 'icon-zoom-in',
      content: '增大字号',
      click: () => {
        if (session.value?.connected) {
          session.value.setOption('fontSize', session.value.getOption('fontSize') + 1);
          session.value.fit();
          session.value.focus();
        }
      }
    }, {
      icon: 'icon-zoom-out',
      content: '减小字号',
      click: () => {
        if (session.value?.connected) {
          session.value.setOption('fontSize', session.value.getOption('fontSize') - 1);
          session.value.fit();
          session.value.focus();
        }
      }
    }, {
      icon: 'icon-eraser',
      content: '清空',
      click: () => {
        session.value?.clear();
        session.value?.focus();
      }
    }, {
      icon: 'icon-poweroff',
      content: '关闭',
      click: () => {
        if (session.value?.connected) {
          session.value.logout();
        }
      }
    },
  ];

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
