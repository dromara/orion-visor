<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        RDP 工具栏设置
      </h3>
    </div>
    <!-- 内容区域 -->
    <div class="terminal-setting-body block-body setting-body">
      <a-form class="terminal-setting-form"
              :model="formModel"
              layout="vertical">
        <a-space size="large">
          <!-- 工具栏按钮 -->
          <a-form-item field="actions" label="工具栏按钮">
            <icon-actions class="form-item-actions"
                          :actions="actions"
                          position="bottom" />
          </a-form-item>
          <!-- 工具栏方向 -->
          <a-form-item field="position" label="工具栏方向">
            <a-select v-model="formModel.position"
                      style="width: 148px;"
                      placeholder="请选择工具栏方向"
                      :options="toOptions(graphActionBarPositionKey)" />
          </a-form-item>
        </a-space>
      </a-form>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalRdpActionBarBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalRdpActionBarSetting } from '@/store/modules/terminal/types';
  import type { SidebarAction } from '@/views/terminal/types/define';
  import { computed, ref, watch } from 'vue';
  import { useTerminalStore, useDictStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { RdpActionBarItems, graphActionBarPositionKey } from '@/views/terminal/types/const';
  import IconActions from '../../layout/icon-actions.vue';

  const { toOptions } = useDictStore();
  const { preference, updateTerminalPreference } = useTerminalStore();

  const formModel = ref<TerminalRdpActionBarSetting>({ ...preference.rdpActionBarSetting });

  // 监听同步
  watch(formModel, (v) => {
    if (!v) {
      return;
    }
    // 同步
    updateTerminalPreference(TerminalPreferenceItem.RDP_ACTION_BAR_SETTING, formModel.value, true);
  }, { deep: true });

  // 操作项
  const actions = computed<Array<SidebarAction>>(() => {
    return RdpActionBarItems.map(s => {
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
