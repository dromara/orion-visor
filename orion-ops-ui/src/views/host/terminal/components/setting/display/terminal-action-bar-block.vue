<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        操作栏设置
      </h3>
    </div>
    <!-- 提示 -->
    <a-alert class="mb16">修改后会立刻保存, 立即生效 (无需刷新页面)</a-alert>
    <!-- 非安全环境提示 -->
    <a-alert v-if="!isSecureEnvironment"
             type="warning"
             class="mb16">
      当前环境非 HTTPS 环境, 因浏览器安全策略限制, 自定义 '粘贴' 功能无法使用
    </a-alert>
    <!-- 内容区域 -->
    <div class="terminal-setting-body block-body setting-body">
      <a-form class="terminal-setting-form"
              :model="formModel"
              layout="vertical">
        <a-space>
          <!-- 顶部操作按钮 -->
          <a-form-item field="actions" label="顶部操作按钮">
            <icon-actions class="form-item-actions"
                          :actions="actions"
                          position="bottom" />
          </a-form-item>
          <!-- 命令输入框 -->
          <a-form-item field="commandInput" label="命令输入框">
            <a-switch v-model="formModel.commandInput"
                      class="form-item-command-input"
                      :default-checked="true"
                      type="round" />
          </a-form-item>
          <!-- 终端连接状态 -->
          <a-form-item field="showStatus" label="终端连接状态">
            <a-switch v-model="formModel.connectStatus"
                      :default-checked="true"
                      type="round" />
          </a-form-item>
        </a-space>
      </a-form>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalActionBarBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalActionBarSetting } from '@/store/modules/terminal/types';
  import type { SidebarAction } from '../../../types/terminal.type';
  import { computed, ref, watch } from 'vue';
  import { useTerminalStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { ActionBarItems } from '../../../types/terminal.const';
  import { isSecureEnvironment } from '@/utils/env';
  import IconActions from '../../layout/icon-actions.vue';

  const { preference, updateTerminalPreference } = useTerminalStore();

  const formModel = ref<TerminalActionBarSetting>({ ...preference.actionBarSetting });

  // 监听同步
  watch(formModel, (v) => {
    if (!v) {
      return;
    }
    // 同步
    updateTerminalPreference(TerminalPreferenceItem.ACTION_BAR_SETTING, formModel.value, true);
  }, { deep: true });

  // 右侧操作
  const actions = computed<Array<SidebarAction>>(() => {
    return ActionBarItems.map(s => {
      return {
        icon: s.icon,
        content: (formModel.value[s.item] === false ? '显示 ' : '隐藏 ') + s.content,
        checked: formModel.value[s.item] !== false,
        click: () => {
          formModel.value[s.item] = formModel.value[s.item] === false;
        }
      };
    });
  });

</script>

<style lang="less" scoped>
  .form-item-actions {
    display: flex;
    background-color: var(--color-fill-2);
    padding: 4px;
    border-radius: 4px;

    :deep(.terminal-sidebar-icon-wrapper) {
      width: 36px;
      height: 36px;
    }

    :deep(.terminal-sidebar-icon) {
      width: 28px;
      height: 28px;
      font-size: 20px;
    }
  }

  .form-item-actions, .form-item-command-input {
    margin-right: 48px;
  }

  :deep(.arco-form) {
    .arco-form-item-label {
      user-select: none;
    }

    .arco-form-item {
      margin-bottom: 0;
    }
  }

</style>
