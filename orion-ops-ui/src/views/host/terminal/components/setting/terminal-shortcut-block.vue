<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        系统快捷键
      </h3>
    </div>
    <!-- 加载中 -->
    <a-skeleton v-if="loading"
                class="skeleton-wrapper"
                :animation="true">
      <a-skeleton-line :rows="4" />
    </a-skeleton>
    <!-- 内容区域 -->
    <div v-else class="terminal-setting-body terminal-shortcut-container">
      <!-- 提示 -->
      <a-alert class="mb16">刷新页面后生效 (设置时需要避免浏览器内置快捷键)</a-alert>
      <template v-for="item in shortcutKeys">
        <div class="shortcut-row" v-if="item.type === TerminalShortcutType.SYSTEM">
          <!-- label -->
          <span class="shortcut-label">{{ item.content }}</span>
          <!-- 快捷键 -->
          <a-space class="shortcut-key-container"
                   :class="[item.edit ? 'edit-container' : '']">
            <a-tag v-if="item.edit || item.ctrlKey"
                   v-model:checked="item.ctrlKey"
                   :checkable="item.edit">
              ctrl
            </a-tag>
            <a-tag v-if="item.edit || item.shiftKey"
                   v-model:checked="item.shiftKey"
                   :checkable="item.edit">
              shift
            </a-tag>
            <a-tag v-if="item.edit || item.altKey"
                   v-model:checked="item.altKey"
                   :checkable="item.edit">
              alt
            </a-tag>
            <!-- 触发按键-->
            <a-tag v-if="item.code && !item.edit" :checkable="item.edit">{{ item.code }}</a-tag>
            <a-input v-if="item.edit"
                     v-model="item.code"
                     :data-item="item.item"
                     :ref="setEditRef"
                     class="trigger-input"
                     size="small"
                     @keyup="e => item.code = e.code" />
          </a-space>
          <!-- 操作 -->
          <a-space class="shortcut-actions-container">
            <!-- 屏蔽 -->
            <div class="click-icon-wrapper" title="屏蔽">
              <icon-stop />
            </div>
            <!-- 设置 -->
            <div class="click-icon-wrapper" v-if="!item.edit"
                 title="设置"
                 @click="() => item.edit = true">
              <icon-settings />
            </div>
            <!-- 保存 -->
            <div class="click-icon-wrapper" v-else
                 title="保存"
                 @click="() => item.edit = false">
              <icon-check />
            </div>
          </a-space>
        </div>
      </template>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalShortcutBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalShortcutSetting, TerminalShortcutKey } from '@/store/modules/terminal/types';
  import { useTerminalStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { nextTick, onMounted, ref } from 'vue';
  import { getPreference } from '@/api/user/preference';
  import useLoading from '@/hooks/loading';
  import { TerminalShortcutType, TerminalShortcutItems } from '../../types/terminal.const';

  const { updateTerminalPreference } = useTerminalStore();
  const { loading, setLoading } = useLoading(true);

  const shortcutKeys = ref<Array<TerminalShortcutKey>>([]);

  // 设置 ref
  const setEditRef = (el: HTMLElement) => {
    // 自动聚焦
    nextTick(() => {
      el && el.focus();
    });
  };

  // 加载用户快捷键
  onMounted(async () => {
    setLoading(true);
    try {
      // 加载偏好
      const { data } = await getPreference<Record<string, any>>('TERMINAL', [TerminalPreferenceItem.SHORTCUT_SETTING]);
      const setting = data[TerminalPreferenceItem.SHORTCUT_SETTING] as TerminalShortcutSetting;
      // 设置快捷键
      const keys: Array<TerminalShortcutKey> = [];
      for (const shortcutItem of TerminalShortcutItems) {
        const shortcutKey = setting.keys?.find(s => s.item === shortcutItem.item);
        if (shortcutKey) {
          // 存在
          keys.push({
            ...shortcutItem,
            ...shortcutKey,
            edit: false,
          });
        } else {
          // 不存在
          keys.push({
            ...shortcutItem,
            ctrlKey: false,
            shiftKey: false,
            altKey: false,
            code: '',
            enabled: false,
            edit: false,
          });
        }
      }
      shortcutKeys.value = keys;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>

  .terminal-shortcut-container {
    flex-direction: column;

    .shortcut-row {
      display: flex;
      align-items: center;
      margin-bottom: 8px;
      background: var(--color-neutral-1);
      height: 42px;
      padding: 8px 16px;
      border-radius: 4px;
      user-select: none;
      transition: .3s;

      &:hover {
        background: var(--color-neutral-2);

        .shortcut-actions-container {
          display: flex;
        }
      }
    }

    .shortcut-label {
      font-size: 14px;
      width: 218px;
    }

    .shortcut-key-container {
      width: 268px;
    }

    .trigger-input {
      width: 68px;
    }

    .shortcut-actions-container {
      display: none;

      .click-icon-wrapper {
        font-size: 16px;
        padding: 4px;
      }
    }

    :deep(.arco-tag) {
      background: var(--color-neutral-2);
      color: var(--color-text-3);

      width: 44px;
      display: flex;
      justify-content: center;
      height: 26px;
      font-size: 14px;
    }

    :deep(.arco-tag-checked) {
      background: var(--color-neutral-3);
      color: var(--color-text-1);
    }
  }

</style>
