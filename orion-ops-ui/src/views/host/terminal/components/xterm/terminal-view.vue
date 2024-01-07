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
          <span class="address-copy copy-left" title="复制" @click="copy(tab.address as string)">
            <icon-copy />
          </span>
        </span>
      </div>
      <!-- 右侧操作 -->
      <div class="terminal-header-right">
        <icon-actions class="bottom-actions"
                      :actions="bottomActions"
                      position="right" />
        <span @click="pl">
          粘贴
        </span>
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
  import type { TerminalTabItem } from '../../types/terminal.type';
  import { onMounted, onUnmounted, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import useCopy from '@/hooks/copy';
  import IconActions from '@/views/host/terminal/components/layout/icon-actions.vue';
  import { SidebarAction } from '@/views/host/terminal/types/terminal.const';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

  const { copy } = useCopy();
  const { preference, sessionManager } = useTerminalStore();

  const terminalRef = ref();

  // 底部操作
  const bottomActions: Array<SidebarAction> = [
    {
      icon: 'icon-command',
      content: '快捷键设置',
      click: () => {
      }
    },
    {
      icon: 'icon-palette',
      content: '外观设置',
      click: () => {
      }
    },
  ];

  // 调整颜色
  const adjustColor = (color: string, range: number) => {
    let newColor = '#';
    for (let i = 0; i < 3; i++) {
      let c = parseInt(color.substring(i * 2 + 1, i * 2 + 3), 16);
      c += range;
      if (c < 0) {
        c = 0;
      } else if (c > 255) {
        c = 255;
      }
      newColor += c.toString(16).padStart(2, '0');
    }
    return newColor;
  };

  const pl = () => {
  };

  // 初始化会话
  onMounted(async () => {
    // 创建终端处理器
    sessionManager.openSession(props.tab, terminalRef.value);
  });

  // 会话
  onUnmounted(() => {
    sessionManager.closeSession(props.tab.key);
  });

</script>

<style lang="less" scoped>
  @terminal-header-height: 32px;

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
      font-size: 12px;
      width: 100%;
      height: 100%;
    }

    &-left:hover {
      .address-copy {
        display: unset;
      }
    }

    &-right {
      justify-content: flex-end;
    }

    .address-wrapper:before {
      content: 'IP:';
      padding-right: 4px;
      user-select: none;
    }

    .address-copy {
      display: none;
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
