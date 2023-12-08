<template>
  <div class="terminal-setting-container theme-setting-container">
    <div class="theme-setting-wrapper">
      <!-- 大标题 -->
      <h2 class="terminal-setting-title">
        主题设置
      </h2>
      <!-- 切换主题 -->
      <div class="terminal-setting-block">
        <!-- 顶部 -->
        <div class="theme-subtitle-wrapper">
          <h3 class="terminal-setting-subtitle">
            主题选择
          </h3>
          <a-radio-group v-model="userDarkTheme"
                         size="mini"
                         type="button"
                         @change="changeDarkTheme">
            <a-radio v-for="theme in DarkTheme" :key="theme.value" :value="theme.value">
              {{ theme.label }}
            </a-radio>
          </a-radio-group>
        </div>
        <!-- 内容区域 -->
        <div class="theme-list">
          <div class="theme-row"
               v-for="index in ThemeSchema.length / 2"
               :key="index">
            <a-card v-for="(theme, index) in [ThemeSchema[(index - 1) * 2], ThemeSchema[(index - 1) * 2 + 1]]"
                    :key="theme.name"
                    class="terminal-theme-card simple-card"
                    :class="{
                      'terminal-theme-card-check': theme.name === userTerminalTheme.name
                    }"
                    :title="theme.name"
                    :style="{
                      background: theme.background,
                      marginRight: index === 0 ? '16px' : 0
                    }"
                    :header-style="{
                      color: theme.dark ? 'rgba(255, 255, 255, .8)' : 'rgba(0, 0, 0, .8)'
                    }"
                    @click="checkTheme(theme)">
              <!-- 样例 -->
              <terminal-example :theme="theme" />
              <icon-check class="theme-check-icon" :style="{
                display: theme.name === userTerminalTheme.name ? 'flex': 'none'
              }" />
            </a-card>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalThemeSetting'
  };
</script>

<script lang="ts" setup>
  import type { TerminalTheme } from '../types/terminal.theme';
  import { DarkTheme } from '../types/terminal.type';
  import ThemeSchema, { FRAPPE } from '../types/terminal.theme';
  import useEmitter from '@/hooks/emitter';
  import { onBeforeMount, ref } from 'vue';
  import TerminalExample from './terminal-example.vue';
  import { useDebounceFn } from '@vueuse/core';
  import { Message } from '@arco-design/web-vue';

  defineProps();

  const emits = defineEmits(['emitter']);

  const { bubblesEmitter } = useEmitter(emits);

  interface TerminalPreference {
    darkTheme: string,
    terminalTheme: TerminalTheme
  }

  const userDarkTheme = ref(DarkTheme.DARK.value);
  const userTerminalTheme = ref<TerminalTheme>(FRAPPE);

  // 修改暗色主题
  const changeDarkTheme = (value: string) => {
    if (value === DarkTheme.DARK.value) {
      // 暗色
      bubblesEmitter('changeDarkTheme', true);
    } else if (value === DarkTheme.LIGHT.value) {
      // 亮色
      bubblesEmitter('changeDarkTheme', false);
    } else if (value === DarkTheme.AUTO.value) {
      // 自动配色
      bubblesEmitter('changeDarkTheme', userTerminalTheme.value.dark ? DarkTheme.DARK.value : DarkTheme.LIGHT.value);
    }
    sync();
  };

  // 选择终端主题
  const checkTheme = (theme: TerminalTheme) => {
    userTerminalTheme.value = theme;
    // 切换主题配色
    if (userDarkTheme.value === DarkTheme.AUTO.value) {
      changeDarkTheme(theme.dark ? DarkTheme.DARK.value : DarkTheme.LIGHT.value);
    } else {
      sync();
    }
  };

  // 同步用户偏好
  const syncUserPreference = async () => {
    try {
      // FIXME 同步用户配置

      Message.success('同步成功');
    } catch (e) {
      Message.error('同步失败');
    }
  };
  // 同步用户偏好防抖
  const sync = useDebounceFn(syncUserPreference, 1500);

  onBeforeMount(() => {
    // FIXME 加载用户配置
  });

</script>

<style lang="less" scoped>
  @terminal-width: 458px;
  @terminal-height: 182px;
  @wrapper-width: @terminal-width * 2 + 16;

  .theme-setting-wrapper {
    width: @wrapper-width;
    user-select: none;
  }

  .theme-subtitle-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;

    .terminal-setting-subtitle {
      margin: 0;
    }
  }

  .theme-list {
    margin-top: 16px;

    .theme-row {
      display: flex;
      margin-bottom: 16px;
    }
  }

  .terminal-theme-card {
    width: @terminal-width;
    height: @terminal-height;
    border: 2px solid var(--color-border);
    cursor: pointer;

    :deep(.arco-card-header) {
      padding: 4px 16px;
      height: 40px;
      border-bottom: .5px solid #DEDEDE;

      &-title {
        color: unset;
        font-size: 16px;
        font-weight: 600;
      }
    }

    :deep(.arco-card-body) {
      height: calc(@terminal-height - 44px);
      padding: 0;
      display: flex;
      position: relative;

      .theme-check-icon {
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
