<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        主题设置
      </h3>
      <!-- 暗色选择 -->
      <a-radio-group v-model="preference.darkTheme"
                     class="usn"
                     size="mini"
                     type="button"
                     :options="toOptions(darkThemeKey)"
                     @change="changeDarkTheme">
      </a-radio-group>
    </div>
    <!-- 内容区域 -->
    <div class="terminal-setting-body terminal-theme-container">
      <div class="theme-row"
           v-for="index in ThemeSchema.length / 2"
           :key="index">
        <a-card v-for="(theme, index) in [ThemeSchema[(index - 1) * 2], ThemeSchema[(index - 1) * 2 + 1]]"
                :key="theme.name"
                class="terminal-theme-card simple-card"
                :class="{
                  'terminal-theme-card-check': theme.name === preference.themeSchema.name
                }"
                :title="theme.name"
                :style="{
                  background: theme.background,
                  marginRight: index === 0 ? '16px' : 0
                }"
                :header-style="{
                  color: theme.dark ? 'rgba(255, 255, 255, .8)' : 'rgba(0, 0, 0, .8)',
                  userSelect: 'none'
                }"
                @click="changeThemeSchema(theme)">
          <!-- 样例 -->
          <terminal-example :theme="{ ...theme, cursor: theme.background }" />
          <!-- 选中按钮 -->
          <icon-check class="theme-check-icon"
                      v-show="theme.name === preference.themeSchema.name" />
        </a-card>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalThemeBlock'
  };
</script>

<script lang="ts" setup>
  import { darkThemeKey } from '../../types/terminal.const';
  import ThemeSchema from '../../types/terminal.theme';
  import { useDictStore, useTerminalStore } from '@/store';
  import TerminalExample from './terminal-example.vue';

  const { changeThemeSchema, changeDarkTheme, preference } = useTerminalStore();
  const { toOptions } = useDictStore();

</script>

<style lang="less" scoped>
  @terminal-width: 458px;
  @terminal-height: 138px;

  .terminal-theme-container {
    flex-direction: column;

    .theme-row {
      display: flex;
      margin-bottom: 16px;
    }
  }

  .terminal-theme-card {
    width: @terminal-width;
    height: calc(@terminal-height + 44px);
    border: 2px solid var(--color-border);
    cursor: pointer;

    :deep(.arco-card-header) {
      padding: 4px 16px;
      height: 40px;
      border-bottom: .5px solid var(--color-border-2);

      &-title {
        color: unset;
        font-size: 16px;
        font-weight: 600;
      }
    }

    :deep(.arco-card-body) {
      height: @terminal-height;
      padding: 0;
      display: flex;
      position: relative;

      .theme-check-icon {
        display: flex;
        position: absolute;
        color: #FFF;
        right: 0;
        bottom: 0;
        z-index: 10;
      }
    }

    &-check, &:hover {
      border: 2px solid rgb(var(--blue-6));
    }

    &-check::after {
      content: '';
      position: absolute;
      right: 0;
      bottom: 0;
      width: 0;
      height: 0;
      border-bottom: 28px solid rgb(var(--blue-6));
      border-left: 28px solid transparent;
    }
  }

</style>
