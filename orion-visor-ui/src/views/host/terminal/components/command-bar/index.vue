<template>
  <div class="command-bar">
    <div class="command-header">
      <!-- 左侧按钮 -->
      <div class="command-header-left">
        <!-- 粘贴 -->
        <a-button size="mini"
                  class="mr8"
                  @click="paste">
          粘贴
          <template #icon>
            <icon-send />
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
                    :auto-size="{ minRows: 3, maxRows: 3 }"
                    placeholder="输入命令, F8 发送"
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
  import { ref } from 'vue';
  import { useTerminalStore } from '@/store';

  const { layoutState, appendCommandToCurrentSession } = useTerminalStore();

  const text = ref('');

  // 粘贴
  const paste = () => {
    appendCommandToCurrentSession(text.value);
    text.value = '';
  };

  // 清空
  const clear = () => {
    text.value = '';
  };

  // 检查命令快捷键
  const checkCommandKey = async (e: KeyboardEvent) => {
    if (text.value && e.code === 'F8') {
      paste();
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
