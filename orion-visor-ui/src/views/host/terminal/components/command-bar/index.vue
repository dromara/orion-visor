<template>
  <div class="command-bar">
    <div class="command-header">
      <!-- 左侧按钮 -->
      <div class="command-header-left">
        <!-- 发送 -->
        <a-button size="mini"
                  class="mr8"
                  title="直接发送到终端"
                  @click="write(false)">
          发送
          <template #icon>
            <icon-send />
          </template>
        </a-button>
        <!-- 执行 -->
        <a-button size="mini"
                  class="mr8"
                  title="拼接 \r\n 后发送到终端"
                  @click="write(true)">
          执行
          <template #icon>
            <icon-thunderbolt />
          </template>
        </a-button>
        <!-- 清空 -->
        <a-button size="mini" @click="clear">
          清空
          <template #icon>
            <icon-delete />
          </template>
        </a-button>
      </div>
      <!-- 右侧按钮 -->
      <div class="command-header-right">
        <!-- 关闭 -->
        <a-button size="mini" @click="close">
          <template #icon>
            <icon-down />
          </template>
        </a-button>
      </div>
    </div>
    <div class="command-body">
      <!-- 命令框 -->
      <div class="command-input">
        <a-textarea v-model="text"
                    placeholder="输入命令, F8 发送"
                    :auto-size="{ minRows: 3, maxRows: 3 }"
                    @keyup="checkCommandKey" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'commandBar'
  };
</script>

<script lang="ts" setup>
  import type { ISshSession } from '@/views/host/terminal/types/define';
  import { ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { PanelSessionType } from '@/views/host/terminal/types/const';
  import { Message } from '@arco-design/web-vue';

  const { layoutState, sessionManager, getCurrentSession, pasteCommandToSession } = useTerminalStore();

  const text = ref('');

  // 写入
  const write = (newLine: boolean) => {
    if (!text.value) {
      Message.warning('请选择输入命令');
      return;
    }
    // 写入
    writeToSession(text.value, newLine);
    text.value = '';
  };

  // 写入到会话
  const writeToSession = (command: string, newLine: boolean) => {
    // 当前会话
    const session = getCurrentSession(PanelSessionType.SSH.type, false);
    // 粘贴
    if (session) {
      pasteCommandToSession((session as ISshSession), command, newLine);
    }
  };

  // 清空
  const clear = () => {
    text.value = '';
  };

  // 检查命令快捷键
  const checkCommandKey = async (e: KeyboardEvent) => {
    if (text.value && e.code === 'F8') {
      write(false);
    }
  };

  // 关闭
  const close = () => {
    // 隐藏
    layoutState.commandBar = false;
  };

</script>

<style lang="less" scoped>
  .command-bar {
    height: 128px;
    overflow: hidden;
  }

  .command-header {
    border-top: 1px var(--color-bg-sidebar) solid;
    width: 100%;
    height: 36px;
    padding: 0 12px;
    display: flex;
    justify-content: space-between;

    &-left, &-right {
      display: flex;
      align-items: center;
    }
  }

  .command-body {
    width: 100%;
    height: 92px;
    padding: 0 12px 12px 12px;
    display: flex;

    .command-input {
      width: 100%;

      :deep(textarea) {
        border-radius: 4px;
      }
    }
  }

</style>
