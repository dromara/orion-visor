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
                    v-model="formModel.fastScrollModifier"
                    checked-value="alt"
                    unchecked-value="none" />
        </block-setting-item>
        <!-- 点击移动光标 -->
        <block-setting-item label="点击移动光标" desc="alt + 鼠标左键可以切换光标位置">
          <a-switch type="round"
                    v-model="formModel.altClickMovesCursor" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 右键选中词条 -->
        <block-setting-item label="右键选中词条" desc="右键文本">
          <a-switch type="round"
                    v-model="formModel.rightClickSelectsWord" />
        </block-setting-item>
        <!-- 选中词条自动复制 -->
        <block-setting-item label="选中词条自动复制" desc="自动将选中的词条复制到剪切板">
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
        <block-setting-item label="粘贴去除空格" desc="粘贴文本前自动删除尾部空格">
          <a-switch type="round"
                    v-model="formModel.pasteAutoTrim" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 右键粘贴 -->
        <block-setting-item label="右键粘贴" desc="右键自动粘贴, 启用后需要关闭右键菜单">
          <a-switch type="round"
                    v-model="formModel.rightClickPaste" />
        </block-setting-item>
        <!-- 启用右键菜单 -->
        <block-setting-item label="启用右键菜单" desc="右键终端将打开自定义菜单, 启用后需要关闭右键粘贴">
          <a-switch type="round"
                    v-model="formModel.pasteAutoTrim" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 启用响铃 -->
        <block-setting-item label="启用响铃" desc="系统接受到 \a 时候会发出响铃 (一般不用开启)">
          <a-switch type="round"
                    v-model="formModel.enableBell" />
        </block-setting-item>
        <!-- 单词分隔符 -->
        <block-setting-item label="单词分隔符" desc="在终端中双击文本将使用该分隔符进行分割">
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

  // 快速滚动 fastScrollModifier
  // 点击移动光标 altClickMovesCursor

  // 右键选中词条  rightClickSelectsWord
  // 自动将选中内容复制到剪切板 selectionChangeCopy onSelectionChange

  // 复制时删除空格 pasteAutoTrim
  // 粘贴时删除空格 copyAutoTrim

  // 右键粘贴 rightClickPaste
  // 启用右键菜单 enableRightClickMenu

  // 启用响铃  enableBell
  // 单词分隔符  /\()"'` -.,:;<>~!@#$%^&*|+=[]{}~?│    wordSeparator

  import { ref } from 'vue';
  import BlockSettingItem from './block-setting-item.vue';

  const formModel = ref<Record<string, any>>({});

</script>

<style lang="less" scoped>
  .setting-body {
    flex-direction: column;
  }

</style>
