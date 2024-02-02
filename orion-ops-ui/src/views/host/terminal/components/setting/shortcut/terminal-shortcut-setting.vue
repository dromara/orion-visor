<template>
  <div class="terminal-setting-container">
    <div class="terminal-setting-wrapper">
      <!-- 主标题 -->
      <h2 class="terminal-setting-title">快捷键设置</h2>
      <!-- 加载中 -->
      <a-skeleton v-if="!render"
                  class="skeleton-wrapper"
                  :animation="true">
        <a-skeleton-line :rows="8"
                         :line-height="42"
                         :line-spacing="12" />
      </a-skeleton>
      <!-- 设置 -->
      <a-spin v-else
              class="full"
              :loading="loading">
        <!-- 快捷键操作 -->
        <terminal-shortcut-action-block v-model:enabled="enabled"
                                        @reset="loadDefaultPreference"
                                        @save="savePreference" />
        <!-- 全局快捷键 -->
        <terminal-shortcut-keys-block title="全局快捷键"
                                      :type="TerminalShortcutType.GLOBAL"
                                      :items="shortcutKeys"
                                      @set-editable="setEditableStatus"
                                      @clear-editable="clearEditableStatus"
                                      @update-enabled="updateEnabledStatus" />
        <!-- 终端面板快捷键 -->
        <terminal-shortcut-keys-block title="终端面板快捷键"
                                      :type="TerminalShortcutType.PANEL"
                                      :items="shortcutKeys"
                                      @set-editable="setEditableStatus"
                                      @clear-editable="clearEditableStatus"
                                      @update-enabled="updateEnabledStatus" />
        <!-- 终端会话快捷键 -->
        <terminal-shortcut-keys-block title="终端会话快捷键"
                                      :type="TerminalShortcutType.TERMINAL"
                                      :items="shortcutKeys"
                                      @set-editable="setEditableStatus"
                                      @clear-editable="clearEditableStatus"
                                      @update-enabled="updateEnabledStatus" />
      </a-spin>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalShortcutSetting'
  };
</script>

<script lang="ts" setup>
  import type { TerminalShortcutKeyEditable, TerminalShortcutSetting } from '@/store/modules/terminal/types';
  import { ref, onMounted, onUnmounted } from 'vue';
  import { getDefaultPreference, getPreference } from '@/api/user/preference';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { TerminalShortcutItems, TerminalShortcutType } from '../../../types/terminal.const';
  import { useTerminalStore } from '@/store';
  import useLoading from '@/hooks/loading';
  import { useDebounceFn } from '@vueuse/core';
  import { addEventListen, removeEventListen } from '@/utils/event';
  import { Message } from '@arco-design/web-vue';
  import TerminalShortcutKeysBlock from './terminal-shortcut-keys-block.vue';
  import TerminalShortcutActionBlock from './terminal-shortcut-action-block.vue';

  const { updateTerminalPreference } = useTerminalStore();
  const { loading, setLoading } = useLoading();

  const render = ref(false);
  const enabled = ref(false);
  const editable = ref(false);
  const currentItem = ref<TerminalShortcutKeyEditable>();
  const shortcutKeys = ref<Array<TerminalShortcutKeyEditable>>([]);

  // 修改快捷键状态
  const updateEnabledStatus = (item: TerminalShortcutKeyEditable, enabled: boolean) => {
    clearEditableStatus();
    item.editable = false;
    item.enabled = enabled;
  };

  // 设置可编辑
  const setEditableStatus = (item: TerminalShortcutKeyEditable) => {
    item.editable = true;
    editable.value = true;
    currentItem.value = item;
  };

  // 清除可编辑状态
  const clearEditableStatus = () => {
    editable.value = false;
    if (currentItem.value) {
      currentItem.value.editable = false;
      currentItem.value = undefined;
    }
  };

  // 计算显示的快捷键
  const computeShortcutKey = (item: TerminalShortcutKeyEditable): string => {
    const keys = [];
    if (item.ctrlKey) {
      keys.push('Ctrl');
    }
    if (item.altKey) {
      keys.push('Alt');
    }
    if (item.shiftKey) {
      keys.push('Shift');
    }
    let code = item.code;
    if (code) {
      if (code.startsWith('Key')) {
        code = code.substring(3);
      } else if (code.startsWith('Digit')) {
        code = code.substring(5);
      } else {
        const keyMap: Record<string, any> = {
          'Backquote': '`',
          'Minus': '-',
          'Equal': '=',
          'BracketLeft': '[',
          'BracketRight': ']',
          'Backslash': '\\',
          'Semicolon': ';',
          'Quote': '\'',
          'Comma': ',',
          'Period': '.',
          'Slash': '/',
          'ArrowUp': '↑',
          'ArrowDown': '↓',
          'ArrowLeft': '←',
          'ArrowRight': '→',
        };
        if (Object.keys(keyMap).includes(code)) {
          code = keyMap[code] as string;
        }
      }
      keys.push(code);
    }
    return keys.join(' + ');
  };

  // 处理快捷键逻辑 防抖函数
  const handlerKeyboardFn = useDebounceFn((e: KeyboardEvent, item: TerminalShortcutKeyEditable) => {
    item.ctrlKey = e.ctrlKey;
    item.shiftKey = e.shiftKey;
    item.altKey = e.altKey;
    if (e.key !== 'Control' && e.key !== 'Shift' && e.key !== 'Alt') {
      item.code = e.code;
    } else {
      item.code = '';
    }
    item.shortcutKey = computeShortcutKey(item);
  });

  // 处理快捷键逻辑
  const handlerKeyboard = (event: Event) => {
    if (editable.value && !!currentItem.value) {
      const e = event as KeyboardEvent;
      event.preventDefault();
      event.stopPropagation();
      // 修改快捷键
      handlerKeyboardFn(e, currentItem.value);
    }
  };

  // 保存
  const savePreference = async () => {
    setLoading(true);
    try {
      await updateTerminalPreference(TerminalPreferenceItem.SHORTCUT_SETTING, {
        enabled: enabled.value,
        keys: shortcutKeys.value
      } as TerminalShortcutSetting);
      Message.success('保存成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 恢复默认设置
  const loadDefaultPreference = async () => {
    const { data } = await getDefaultPreference<Record<string, any>>('TERMINAL', [TerminalPreferenceItem.SHORTCUT_SETTING]);
    const setting = data[TerminalPreferenceItem.SHORTCUT_SETTING] as TerminalShortcutSetting;
    renderShortcutKeys(setting);
  };

  // 加载用户设置
  const loadUserPreference = async () => {
    // 加载偏好
    const { data } = await getPreference<Record<string, any>>('TERMINAL', [TerminalPreferenceItem.SHORTCUT_SETTING]);
    const setting = data[TerminalPreferenceItem.SHORTCUT_SETTING] as TerminalShortcutSetting;
    renderShortcutKeys(setting);
  };

  // 渲染快捷键
  const renderShortcutKeys = (setting: TerminalShortcutSetting) => {
    // 设置快捷键
    const keys: Array<TerminalShortcutKeyEditable> = [];
    for (const shortcutItem of TerminalShortcutItems) {
      const shortcutKey = setting.keys?.find(s => s.item === shortcutItem.item);
      if (shortcutKey) {
        // 存在
        keys.push({
          ...shortcutItem,
          ...shortcutKey,
          editable: false,
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
          editable: false,
        });
      }
    }
    // 计算快捷键
    keys.forEach(key => key.shortcutKey = computeShortcutKey(key));
    shortcutKeys.value = keys;
    enabled.value = setting.enabled;
  };

  // 加载用户快捷键
  onMounted(async () => {
    try {
      await loadUserPreference();
    } catch (e) {
    } finally {
      render.value = true;
    }
  });

  // 监听键盘事件
  onMounted(() => {
    addEventListen(window, 'keydown', handlerKeyboard, true);
  });

  // 移除键盘事件
  onUnmounted(() => {
    removeEventListen(window, 'keydown', handlerKeyboard, true);
  });

</script>

<style lang="less" scoped>

</style>
