<template>
  <div class="terminal-panel-container">
    <a-tabs v-model:active-key="panel.active"
            :editable="true"
            :auto-switch="true"
            @tab-click="k => panel.clickTab(k as string)"
            @delete="k => panel.deleteTab(k as string)">
      <a-tab-pane v-for="tab in panel.items"
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
        <!-- 终端 -->
        <terminal-view :tab="tab" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalPanel'
  };
</script>

<script lang="ts" setup>
  import type { ITerminalTabManager } from '../../types/terminal.type';
  import TerminalView from '@/views/host/terminal/components/xterm/terminal-view.vue';

  const props = defineProps<{
    panel: ITerminalTabManager
  }>();

  // FIXME 全部关闭展示新增

</script>

<style lang="less" scoped>
  .terminal-panel-container {
    width: 100%;
    height: 100%;
  }

  .tab-title-wrapper {
    display: flex;
    align-items: stretch;

    .tab-title-icon {
      font-size: 16px;
      margin-right: 6px;
    }
  }

  :deep(.arco-tabs) {
    width: 100%;
    height: 100%;

    .arco-tabs-content {
      padding: 0;
      width: 100%;
      height: calc(100% - var(--panel-nav-height));
      overflow: auto;

      &::-webkit-scrollbar {
        display: none;
      }
    }
  }

  :deep(.arco-tabs-nav) {
    height: var(--panel-nav-height);
    background: var(--color-bg-panel);

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
      color: var(--color-panel-text-2);

      &::before {
        background: var(--color-bg-panel-icon-1);
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
    color: var(--color-panel-text-1);
    background: var(--color-bg-panel-tabs);
    position: relative;

    &:hover {
      color: var(--color-panel-text-2);
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
      background: linear-gradient(270deg, var(--color-panel-gradient-start) 45%, var(--color-panel-gradient-end) 120%);
    }

    .arco-tabs-tab-title {
      padding: 11px 18px;
      background: var(--color-bg-panel-tabs);
      font-size: 14px;
      height: var(--panel-nav-height);
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
      color: var(--color-panel-text-2);

      &:hover {
        transition: .2s;
        background: var(--color-bg-panel-icon-1);
      }

      &::before {
        display: none;
      }
    }
  }

  :deep(.arco-tabs-tab-active) {
    background: var(--color-bg-panel-tabs-active);
    color: var(--color-panel-text-2) !important;

    .arco-tabs-tab-title {
      background: var(--color-bg-panel-tabs-active);
    }

    &:hover::after {
      background: linear-gradient(270deg, var(--color-panel-gradient-start) 45%, var(--color-panel-gradient-end) 120%);
    }

  }

</style>
