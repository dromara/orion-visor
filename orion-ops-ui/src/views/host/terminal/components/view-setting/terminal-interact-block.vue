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
    <div class="terminal-setting-body">
      <a-row class="" :gutter="[16, 16]">
        <a-col :span="12">
          <div class="block-form-item-wrapper">
            <div class="block-form-item-label">
              label
            </div>
            <div class="block-form-item-desc">
              描述一下
            </div>
            <div class="block-form-item-value">
              <a-switch />
            </div>
          </div>
        </a-col>
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

  // TODO
  // 交互设置
  // alt + 滚轮快速滚动 fastScrollModifier  'none' | 'alt'
  // alt 点击可以切换光标位置  altClickMovesCursor

  // 右键选中词条  rightClickSelectsWord
  // 自动将选中内容复制到剪切板 onSelectionChange

  // 粘贴时删除空格
  // 复制时删除空格

  // 右键粘贴
  // 启用右键菜单

  // 自动检测 url 并可以点击
  // 支持显示图片 使用 sixel 打开图片

  // bell sound
  // 分隔符  /\()"'-.,:;<>~!@#$%^&*|+=[]{}~?│   在终端中双击文本将使用到这些符号 wordSeparator

</script>

<style lang="less" scoped>
  .block-form-item-wrapper {
    height: 84px;
    border-radius: 4px;
    background: var(--color-fill-2);
    display: flex;
    padding: 16px;

    .block-form-item-label {
      color: var(--color-content-text-3);
      font-size: 15px;
      font-weight: bold;
    }

    .block-form-item-desc {
      color: var(--color-content-text-2);
      font-size: 12px;
    }
  }

</style>
