<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        显示偏好
      </h3>
    </div>
    <!-- 提示 -->
    <a-alert class="mb16">修改后会立刻保存, 重新打开终端后生效 (无需刷新页面)</a-alert>
    <!-- 内容区域 -->
    <div class="terminal-setting-body block-body setting-body">
      <a-form class="terminal-setting-form"
              :model="formModel"
              layout="vertical">
        <a-space>
          <!-- 字体样式 -->
          <a-form-item field="fontFamily" label="字体样式">
            <a-select v-model="formModel.fontFamily"
                      class="form-item-font-family"
                      placeholder="请选择字体样式"
                      :options="toOptions(fontFamilyKey)"
                      :allow-create="true"
                      :filter-option="labelFilter">
              <template #option="{ data }">
                <span :style="{ fontFamily: data.value }">{{ data.label }}</span>
              </template>
            </a-select>
          </a-form-item>
          <!-- 字体大小 -->
          <a-form-item field="fontSize" label="字体大小">
            <a-select v-model="formModel.fontSize"
                      class="form-item-font-size"
                      placeholder="请选择字体大小"
                      :options="toOptions(fontSizeKey)" />
          </a-form-item>
          <!-- 行高 -->
          <a-form-item field="lineHeight" label="行高">
            <a-input-number v-model="formModel.lineHeight"
                            class="form-item-line-height"
                            placeholder="请输入行高"
                            :precision="2"
                            :min="1"
                            :max="2"
                            hide-button />
          </a-form-item>
        </a-space>
        <a-space>
          <!-- 普通文本字重 -->
          <a-form-item field="fontWeight" label="普通文本字重">
            <a-select v-model="formModel.fontWeight"
                      class="form-item-font-weight"
                      placeholder="请选择字重"
                      :options="toOptions(fontWeightKey)" />
          </a-form-item>
          <!-- 加粗文本字重 -->
          <a-form-item field="fontWeightBold" label="加粗文本字重">
            <a-select v-model="formModel.fontWeightBold"
                      class="form-item-font-bold-weight"
                      placeholder="请选择字重"
                      :options="toOptions(fontWeightKey)" />
          </a-form-item>
        </a-space>
        <a-space>
          <!-- 光标样式 -->
          <a-form-item field="cursorStyle" label="光标样式">
            <a-radio-group type="button"
                           v-model="formModel.cursorStyle"
                           class="form-item-cursor-style usn"
                           :options="toRadioOptions(cursorStyleKey)" />
          </a-form-item>
          <!-- 光标闪烁 -->
          <a-form-item field="cursorBlink" label="光标是否闪烁">
            <a-switch v-model="formModel.cursorBlink"
                      type="round"
                      class="form-item-cursor-blink" />
          </a-form-item>
        </a-space>
      </a-form>
      <!-- 预览区域 -->
      <div class="terminal-example">
        <span class="terminal-example-label">预览效果</span>
        <div class="terminal-example-wrapper"
             :style="{ background: preference.theme.schema.background }">
          <terminal-example :schema="preference.theme.schema"
                            ref="previewTerminal" />
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalDisplayBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalDisplaySetting } from '@/store/modules/terminal/types';
  import { ref, watch } from 'vue';
  import { useDictStore, useTerminalStore } from '@/store';
  import { fontFamilyKey, fontSizeKey, fontWeightKey, fontFamilySuffix, cursorStyleKey } from '../../types/terminal.const';
  import { labelFilter } from '@/types/form';
  import { PreferenceItem } from '@/store/modules/terminal';
  import TerminalExample from '../view-setting/terminal-example.vue';

  const { toOptions, toRadioOptions } = useDictStore();
  const { preference, updateTerminalPreference } = useTerminalStore();

  const previewTerminal = ref();
  const formModel = ref<TerminalDisplaySetting>({ ...preference.displaySetting });

  // 监听内容变化
  watch(formModel, (v) => {
    if (!v) {
      return;
    }
    const options = previewTerminal.value?.term?.options;
    if (!options) {
      return;
    }
    // 修改预览终端配置
    Object.keys(v).forEach(key => {
      if (key === 'fontFamily') {
        options[key] = (formModel.value as any)[key] + fontFamilySuffix;
      } else {
        options[key] = (formModel.value as any)[key];
      }
    });
    // 同步
    updateTerminalPreference(PreferenceItem.DISPLAY_SETTING, formModel.value, true);
    // 聚焦
    previewTerminal.value.term.focus();
  }, { deep: true });

</script>

<style lang="less" scoped>
  @terminal-width: 458px;

  .setting-body {
    height: 248px;
    justify-content: space-between;
  }

  :deep(.arco-form) {
    .arco-form-item-label {
      user-select: none;
    }

    .arco-form-item {
      margin-bottom: 14px;
    }

    .form-item-font-family {
      width: 158px;
    }

    .form-item-font-size {
      width: 148px;
    }

    .form-item-line-height {
      width: 114px;
    }

    .form-item-font-weight, .form-item-font-bold-weight {
      width: 178px;
    }

    .form-item-font-weight {
      margin-right: 70px;
    }

    .form-item-cursor-style {
      margin-right: 90px;

      .arco-radio-button-content {
        padding: 0 20px;
      }
    }
  }

  .terminal-example {
    height: 100%;

    &-label {
      color: var(--color-text-2);
      display: block;
      height: 16px;
      margin-bottom: 12px;
      user-select: none;
    }

    &-wrapper {
      border-radius: 4px;
      width: calc(@terminal-width - 16px);
      height: calc(100% - 16px - 12px);
    }
  }

</style>
