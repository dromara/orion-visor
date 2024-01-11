<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        交互设置
      </h3>
    </div>
    <!-- 提示 -->
    <a-alert class="mb16">修改后会立刻保存, 刷新页面后生效</a-alert>
    <!-- 内容区域 -->
    <div class="terminal-setting-body setting-body">
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 快速滚动 -->
        <block-setting-item label="快速滚动" desc="alt + 鼠标滚轮快速滚动">
          <a-switch type="round"
                    v-model="formModel.fastScrollModifier" />
        </block-setting-item>
        <!-- 点击移动光标 -->
        <block-setting-item label="点击移动光标" desc="alt + 鼠标左键可以切换光标位置">
          <a-switch type="round"
                    v-model="formModel.altClickMovesCursor" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 右键选中词条 -->
        <block-setting-item label="右键选中词条" desc="右键文本后会根据单词分隔符自动选中词条">
          <a-switch type="round"
                    v-model="formModel.rightClickSelectsWord" />
        </block-setting-item>
        <!-- 选中自动复制 -->
        <block-setting-item label="选中自动复制" desc="自动将选中的文本复制到剪切板">
          <a-switch type="round"
                    v-model="formModel.selectionChangeCopy" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 复制去除空格 -->
        <block-setting-item label="复制去除空格" desc="复制文本后自动删除尾部空格">
          <a-switch type="round"
                    v-model="formModel.copyAutoTrim" />
        </block-setting-item>
        <!-- 粘贴去除空格 -->
        <block-setting-item label="粘贴去除空格" desc="粘贴文本前自动删除尾部空格 如: 命令输入框, 命令编辑器, 右键粘贴, 粘贴按钮, 右键菜单粘贴, 自定义粘贴快捷键. (系统快捷键无法干预 如: ctrl + shift + v, shift + insert)">
          <a-switch type="round"
                    v-model="formModel.pasteAutoTrim" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 右键粘贴 -->
        <block-setting-item label="右键粘贴" desc="右键自动粘贴, 启用后需要关闭右键菜单 (若开启了右键选中词条, 有选中的文本时, 右键粘贴无效)">
          <a-switch type="round"
                    v-model="formModel.rightClickPaste" />
        </block-setting-item>
        <!-- 启用右键菜单 -->
        <block-setting-item label="启用右键菜单" desc="右键终端将打开自定义菜单, 启用后需要关闭右键粘贴">
          <a-switch type="round"
                    v-model="formModel.enableRightClickMenu" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 启用响铃 -->
        <block-setting-item label="启用响铃" desc="系统接受到 \a 时候会发出响铃 (一般不用开启)">
          <a-switch type="round"
                    v-model="formModel.enableBell" />
        </block-setting-item>
        <!-- 单词分隔符 -->
        <block-setting-item label="单词分隔符" desc="在终端中双击文本将使用该分隔符进行分割 (一般不用修改)">
          <a-input size="small"
                   v-model="formModel.wordSeparator"
                   placeholder="单词分隔符"
                   allow-clear />
        </block-setting-item>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalInteractBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalInteractSetting } from '@/store/modules/terminal/types';
  import { ref, watch } from 'vue';
  import { useTerminalStore } from '@/store';
  import { PreferenceItem } from '@/store/modules/terminal';
  import BlockSettingItem from './block-setting-item.vue';

  const { preference, updateTerminalPreference } = useTerminalStore();

  const formModel = ref<TerminalInteractSetting>({ ...preference.interactSetting });

  // 监听内容变化
  watch(formModel, (v) => {
    if (!v) {
      return;
    }
    // 同步
    updateTerminalPreference(PreferenceItem.INTERACT_SETTING, formModel.value, true);
  }, { deep: true });

</script>

<style lang="less" scoped>
  .setting-body {
    flex-direction: column;
  }

</style>
