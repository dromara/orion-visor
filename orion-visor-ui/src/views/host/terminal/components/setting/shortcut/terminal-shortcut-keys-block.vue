<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        {{ title }}
      </h3>
    </div>
    <!-- 内容区域 -->
    <div class="terminal-setting-body terminal-shortcut-container">
      <template v-for="item in items">
        <div class="shortcut-row" v-if="item.type === type">
          <!-- 名称 -->
          <span class="shortcut-name">{{ item.content }}</span>
          <!-- 快捷键 -->
          <div class="shortcut-key-container">
            <!-- 启用-修改中 -->
            <a-input v-if="item.editable && item.enabled"
                     v-model="item.shortcutKey"
                     :ref="setAutoFocus as unknown as VNodeRef"
                     class="trigger-input"
                     size="small"
                     placeholder="请按下快捷键"
                     readonly
                     @blur="clearEditableStatus" />
            <!-- 启用-未修改 -->
            <span v-else-if="item.enabled">{{ item.shortcutKey }}</span>
            <!-- 禁用 -->
            <span v-else class="disabled-key">已禁用</span>
          </div>
          <!-- 操作 -->
          <a-space class="shortcut-actions">
            <!-- 屏蔽 -->
            <a-button class="shortcut-action icon-button"
                      v-if="item.enabled"
                      title="屏蔽"
                      @click="updateEnabledStatus(item, false)">
              <icon-message-banned />
            </a-button>
            <!-- 恢复 -->
            <a-button class="shortcut-action icon-button"
                      v-if="!item.enabled"
                      title="恢复"
                      @click="updateEnabledStatus(item, true)">
              <icon-message />
            </a-button>
            <!-- 设置 -->
            <a-button class="shortcut-action icon-button"
                      v-if="!item.editable && item.enabled"
                      title="设置"
                      @click="setEditableStatus(item)">
              <icon-settings />
            </a-button>
          </a-space>
          <!-- 描述 -->
          <div class="shortcut-desc">
            <!-- 粘贴描述 -->
            <template v-if="item.item === TerminalShortcutKeys.PASTE">
              Ctrl + Shift + V, Shift + Insert 为浏览器内置快捷键, 不受环境影响
            </template>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalShortcutKeysBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalShortcutKeyEditable } from '@/store/modules/terminal/types';
  import type { VNodeRef } from 'vue';
  import { setAutoFocus } from '@/utils/dom';
  import { TerminalShortcutKeys } from '../../../types/const';

  defineProps<{
    title: string;
    type: number;
    items: Array<TerminalShortcutKeyEditable>;
  }>();

  const emits = defineEmits(['setEditable', 'clearEditable', 'updateEnabled']);

  // 修改启用状态
  const updateEnabledStatus = (item: TerminalShortcutKeyEditable, enabled: boolean) => {
    emits('updateEnabled', item, enabled);
  };

  // 设置可编辑状态
  const setEditableStatus = (item: TerminalShortcutKeyEditable) => {
    emits('setEditable', item);
  };

  // 清除可编辑状态
  const clearEditableStatus = () => {
    emits('clearEditable');
  };

</script>

<style lang="less" scoped>

  .terminal-shortcut-container {
    flex-direction: column;

    .shortcut-row {
      display: flex;
      align-items: center;
      margin-bottom: 8px;
      background: var(--color-neutral-2);
      height: 42px;
      padding: 8px 16px;
      border-radius: 4px;
      user-select: none;
      transition: .3s;

      &:hover {
        background: var(--color-neutral-3);

        .shortcut-actions {
          display: flex;
        }

        .shortcut-desc {
          display: none;
        }
      }

      .shortcut-name {
        font-size: 14px;
        width: 238px;
      }

      .shortcut-key-container {
        width: 268px;

        .trigger-input {
          width: 188px;
        }
      }
    }

    .shortcut-desc {
      display: flex;
      font-size: 12px;
    }

    .shortcut-actions {
      display: none;
    }

    .shortcut-action {
      width: 28px;
      height: 28px;
      font-size: 18px;

      &:hover {
        background: var(--color-neutral-4);
      }
    }

    .disabled-key {
      color: var(--color-neutral-6);
    }
  }
</style>
