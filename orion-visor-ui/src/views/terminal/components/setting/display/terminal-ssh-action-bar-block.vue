<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        SSH 操作栏设置
      </h3>
    </div>
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
        <a-space size="large">
          <!-- 顶部操作按钮 -->
          <a-form-item field="actions" label="顶部操作按钮">
            <icon-actions class="form-item-actions"
                          :actions="actions"
                          position="bottom" />
          </a-form-item>
          <!-- 终端连接状态 -->
          <a-form-item field="showStatus" label="终端连接状态">
            <a-switch v-model="formModel.connectStatus"
                      :default-checked="true"
                      type="round" />
          </a-form-item>
          <!-- 分享按钮 -->
          <a-form-item field="share" label="分享按钮">
            <a-switch v-model="formModel.share"
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
    name: 'terminalSshActionBarBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalSshActionBarSetting } from '@/store/modules/terminal/types';
  import type { SidebarAction } from '@/views/terminal/types/define';
  import { computed, ref, watch } from 'vue';
  import { useTerminalStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { SshActionBarItems } from '@/views/terminal/types/const';
  import { isSecureEnvironment } from '@/utils/env';
  import IconActions from '../../layout/icon-actions.vue';

  const { preference, updateTerminalPreference } = useTerminalStore();

  const formModel = ref<TerminalSshActionBarSetting>({ ...preference.sshActionBarSetting });

  // 监听同步
  watch(formModel, (v) => {
    if (!v) {
      return;
    }
    // 同步
    updateTerminalPreference(TerminalPreferenceItem.SSH_ACTION_BAR_SETTING, formModel.value, true);
  }, { deep: true });

  // 操作项
  const actions = computed<Array<SidebarAction>>(() => {
    return SshActionBarItems.map(s => {
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
      width: 40px;
      height: 40px;
    }

    :deep(.terminal-sidebar-icon) {
      width: 32px;
      height: 32px;
      font-size: 20px;
    }
  }

  .form-item-actions {
    margin-right: 24px;
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
