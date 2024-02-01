<template>
  <div class="terminal-header">
    <!-- 左侧 logo -->
    <!-- FIXME -->
    <div class="terminal-header-left">
      <img alt="logo"
           class="terminal-header-logo-img"
           draggable="false"
           src="//p3-armor.byteimg.com/tos-cn-i-49unhts6dw/dfdba5317c0c20ce20e64fac803d52bc.svg~tplv-49unhts6dw-image.image" />
      <h5 class="terminal-header-logo-text">Orion Ops Pro</h5>
    </div>
    <!-- 左侧 tabs -->
    <div class="terminal-header-tabs">
      <a-tabs v-model:active-key="tabManager.active"
              :editable="true"
              :hide-content="true"
              :auto-switch="true"
              @tab-click="k => tabManager.clickTab(k as string)"
              @delete="k => tabManager.deleteTab(k as string)">
        <a-tab-pane v-for="tab in tabManager.items"
                    :key="tab.key">
          <!-- 标题 -->
          <template #title>
            <span class="tab-title-wrapper">
              <span class="tab-title-icon">
                <component :is="tab.icon" />
              </span>
              {{ tab.title }}
            </span>
          </template>
        </a-tab-pane>
      </a-tabs>
    </div>
    <!-- 右侧操作 -->
    <div class="terminal-header-right">
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
    name: 'layoutHeader'
  };
</script>

<script lang="ts" setup>
  import type { SidebarAction } from '../../types/terminal.type';
  import { useFullscreen } from '@vueuse/core';
  import { computed } from 'vue';
  import { useTerminalStore } from '@/store';
  import IconActions from '../layout/icon-actions.vue';
  import DictKeySelector from '@/components/system/dict-key/dict-key-selector.vue';

  const { isFullscreen, toggle: toggleFullScreen } = useFullscreen();
  const { tabManager } = useTerminalStore();

  // 顶部操作
  const actions = computed<Array<SidebarAction>>(() => [
    {
      icon: isFullscreen.value ? 'icon-fullscreen-exit' : 'icon-fullscreen',
      content: isFullscreen.value ? '点击退出全屏模式' : '点击切换全屏模式',
      click: toggleFullScreen
    },
  ]);

</script>

<style lang="less" scoped>
  @logo-width: 168px;

  .terminal-header {
    height: 100%;
    color: var(--color-header-text-2);
    display: flex;
    user-select: none;

    &-left {
      width: @logo-width;
      display: flex;
      align-items: center;
      justify-content: flex-start;
    }

    &-logo-img {
      padding-left: 6px;
      width: 36px;
      height: 36px;
    }

    &-logo-text {
      margin: 0;
      display: flex;
      align-items: center;
      padding: 0 8px;
      font-size: 16px;
      overflow: hidden;
      white-space: nowrap;
    }

    &-tabs {
      width: calc(100% - @logo-width - var(--sidebar-icon-wrapper-size));
      display: flex;
    }

    &-right {
      width: var(--sidebar-icon-wrapper-size);
      display: flex;
      justify-content: flex-end;

      &-actions {
        width: var(--sidebar-icon-wrapper-size);
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

  .tab-title-wrapper {
    display: flex;
    align-items: stretch;

    .tab-title-icon {
      font-size: 16px;
      margin-right: 6px;
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
    background: var(--color-bg-header-tabs);
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
      padding: 11px 18px;
      background: var(--color-bg-header-tabs);
      font-size: 14px;
      height: var(--header-height);
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
    background: var(--color-bg-header-tabs-active);
    color: var(--color-header-text-2) !important;

    .arco-tabs-tab-title {
      background: var(--color-bg-header-tabs-active);
    }

    &:hover::after {
      background: linear-gradient(270deg, var(--color-gradient-start) 45%, var(--color-gradient-end) 120%);
    }

  }

</style>
