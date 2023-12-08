<template>
  <div class="terminal-header">
    <!-- 左侧 logo -->
    <div class="terminal-header-left">
      <h5 class="terminal-header-logo-text">Orion Ops Pro</h5>
    </div>
    <!-- 左侧 tabs -->
    <div class="terminal-header-tabs">
      <a-tabs v-model:active-key="activeKey"
              :editable="true"
              :hide-content="true"
              :auto-switch="true"
              @tab-click="e => emits('clickTab', e)"
              @delete="e => emits('deleteTab', e)">
        <a-tab-pane v-for="tab in tabs"
                    :key="tab.key"
                    :title="tab.title" />
      </a-tabs>
    </div>
    <!-- 右侧操作 -->
    <div class="terminal-header-right">
      <!-- 分享用户 -->
      <a-avatar-group v-if="false"
                      class="terminal-header-right-avatar-group"
                      :size="28"
                      :max-count="4"
                      :max-style="{background: '#168CFF'}">
        <a-avatar v-for="i in 8" :key="i"
                  :style="{background: '#168CFF'}">
          {{ i }}
        </a-avatar>
      </a-avatar-group>
      <!-- 操作按钮 -->
      <icon-actions class="terminal-header-right-actions"
                    :actions="actions"
                    position="br"
                    icon-class="terminal-header-icon" />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalHeader'
  };
</script>

<script lang="ts" setup>
  import type { SidebarAction, TabItem } from '../../types/terminal.const';
  import type { PropType } from 'vue';
  import { useFullscreen } from '@vueuse/core';
  import { computed } from 'vue';
  import IconActions from '../layout/icon-actions.vue';

  const props = defineProps({
    modelValue: {
      type: String,
      required: true
    },
    tabs: {
      type: Array as PropType<Array<TabItem>>,
      required: true
    }
  });

  const emits = defineEmits(['update:modelValue', 'clickTab', 'deleteTab', 'split', 'share']);

  const { isFullscreen, toggle: toggleFullScreen } = useFullscreen();

  // 顶部操作
  const actions = computed<Array<SidebarAction>>(() => [
    {
      icon: 'icon-interaction',
      content: '分屏',
      visible: false,
      click: () => emits('split')
    },
    {
      icon: 'icon-share-alt',
      content: '分享链接',
      visible: false,
      click: () => emits('share')
    },
    {
      icon: isFullscreen.value ? 'icon-fullscreen-exit' : 'icon-fullscreen',
      content: isFullscreen.value ? '点击退出全屏模式' : '点击切换全屏模式',
      click: () => toggleFullScreen()
    },
  ]);

  const activeKey = computed<String>({
    get() {
      return props.modelValue;
    },
    set(e) {
      if (e) {
        emits('update:modelValue', e);
      } else {
        emits('update:modelValue', null);
      }
    }
  });

</script>

<style lang="less" scoped>
  .terminal-header {
    --logo-width: 150px;
    --right-avatar-width: calc(28px * 5 - 7px * 4);
    --right-action-width: calc(var(--header-height) * 3);
  }

  .terminal-header {
    height: 100%;
    color: var(--color-header-text-2);
    display: flex;
    user-select: none;

    &-left {
      width: var(--logo-width);
    }

    &-logo-text {
      height: var(--header-height);
      margin: 0;
      display: flex;
      align-items: center;
      padding-left: 16px;
      font-size: 16px;
    }

    &-tabs {
      width: calc(100% - var(--logo-width) - var(--right-avatar-width) - var(--right-action-width));
      display: flex;
    }

    &-right {
      width: calc(var(--right-avatar-width) + var(--right-action-width));
      display: flex;
      justify-content: flex-end;

      &-avatar-group {
        width: var(--right-avatar-width);
        display: flex;
        align-items: center;
        justify-content: flex-end;
      }

      &-actions {
        width: var(--right-action-width);
        display: flex;
        justify-content: flex-end;
      }
    }

    :deep(&-icon) {
      color: var(--color-header-text-2) !important;

      &:hover {
        background: var(--color-bg-header-icon-1) !important;
      }
    }
  }

  :deep(.arco-tabs-nav) {
    height: 100%;

    &::before {
      display: none;
    }

    &-tab {
      height: 100%;
    }

    &-ink {
      display: none;
    }

    &-button .arco-icon-hover:hover {
      color: var(--color-header-text-2);

      &::before {
        background: var(--color-bg-header-icon-1);
      }
    }
  }

  :deep(.arco-tabs-nav-type-line .arco-tabs-tab:hover .arco-tabs-tab-title::before) {
    background: transparent;
  }

  :deep(.arco-tabs-tab) {
    height: 100%;
    margin: 0;
    padding: 0;
    color: var(--color-header-text-1);
    background: var(--color-header-tabs-bg);
    position: relative;

    &:hover {
      color: var(--color-header-text-2);
      transition: .2s;
    }

    &::after {
      content: '';
      width: 54px;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: flex-end;
      position: absolute;
      right: 0;
      top: 0;
    }

    &:hover::after {
      background: linear-gradient(270deg, var(--color-gradient-start) 45%, var(--color-gradient-end) 120%);
    }

    .arco-tabs-tab-title {
      padding: 11px 16px;
      background: var(--color-header-tabs-bg);
      font-size: 13px;
      display: flex;
      align-items: center;

      &::before {
        display: none;
      }
    }

    &:hover .arco-tabs-tab-close-btn {
      display: unset;
    }

    &-close-btn {
      margin: 0 8px 0 0;
      padding: 4px;
      border-radius: 4px;
      position: absolute;
      right: 0;
      z-index: 4;
      display: none;
      color: var(--color-header-text-2);

      &:hover {
        transition: .2s;
        background: var(--color-bg-header-icon-1);
      }

      &::before {
        display: none;
      }
    }
  }

  :deep(.arco-tabs-tab-active) {
    background: var(--color-header-tabs-bg-hover);
    color: var(--color-header-text-2) !important;

    .arco-tabs-tab-title {
      background: var(--color-header-tabs-bg-hover);
    }

    &:hover::after {
      background: linear-gradient(270deg, var(--color-gradient-start) 45%, var(--color-gradient-end) 120%);
    }

  }

</style>
