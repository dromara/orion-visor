<template>
  <div class="terminal-header">
    <!-- 左侧 logo -->
    <div class="terminal-header-left">
      <h5 class="terminal-header-logo-text">Orion Ops Pro</h5>
    </div>
    <!-- 左侧 tabs -->
    <div class="terminal-header-tabs">
      <slot />
    </div>
    <!-- 右侧操作 -->
    <div class="terminal-header-right-actions">
      <!-- 操作按钮 -->
      <a-tooltip position="left"
                 :mini="true"
                 content-class="terminal-sidebar-tooltip-content"
                 arrow-class="terminal-sidebar-tooltip-arrow"
                 :content="isFullscreen ? '点击退出全屏模式' : '点击切换全屏模式'">
        <div class="terminal-sidebar-icon-wrapper">
          <div class="terminal-sidebar-icon" @click="toggleFullScreen">
            <icon-fullscreen-exit v-if="isFullscreen" />
            <icon-fullscreen v-else />
          </div>
        </div>
      </a-tooltip>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalHeader'
  };
</script>

<script lang="ts" setup>
  import { useFullscreen } from '@vueuse/core';

  const { isFullscreen, toggle: toggleFullScreen } = useFullscreen();

</script>

<style lang="less" scoped>
  .terminal-header {
    --logo-width: 150px;
    --right-action-width: calc(var(--header-height) * 2);
  }

  .terminal-header {
    height: 100%;
    color: var(--color-text-white);
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
      width: calc(100% - var(--logo-width) - var(--right-action-width));
      display: flex;
    }

    &-right-actions {
      width: var(--right-action-width);
      display: flex;
      justify-content: flex-end;
    }
  }

  :deep(.arco-tabs-nav) {
    height: 100%;

    &::before {
      display: none;
    }
  }

  :deep(.arco-tabs-nav-tab) {
    height: 100%;
  }

  :deep(.arco-tabs-nav-ink) {
    display: none;
  }

  :deep(.arco-tabs-tab-close-btn) {
    margin-left: -12px;

    &:hover {
      color: var(--color-text-black);
    }
  }

  :deep(.arco-tabs-nav-button .arco-icon-hover:hover) {
    color: var(--color-text-black);
  }

  :deep(.arco-tabs-nav-type-line .arco-tabs-tab:hover .arco-tabs-tab-title::before) {
    background: transparent;
  }

  :deep(.arco-tabs-tab) {
    height: 100%;
    margin: 0;
    padding: 8px 12px;
    color: var(--color-host-tabs-text);
    background: var(--color-host-tabs-bg);

    &:hover {
      color: var(--color-host-tabs-hover-text);
    }

    .arco-tabs-tab-title {
      padding: 0 16px 0 0;
      background: var(--color-host-tabs-bg);
      z-index: 100;

      &:hover {
        z-index: 0;
      }
    }
  }

  :deep(.arco-tabs-tab-active) {
    background: var(--color-host-tabs-active-bg);
    color: var(--color-host-tabs-active-text) !important;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);

    .arco-tabs-tab-title {
      background: var(--color-host-tabs-active-bg);
    }
  }

</style>
