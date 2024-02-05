<template>
  <div class="terminal-panel-container">
    <!-- 终端 tab -->
    <a-tabs v-model:active-key="panel.active"
            :editable="true"
            :auto-switch="true"
            :show-add-button="true"
            @add="openNewConnect"
            @tab-click="k => panel.clickTab(k as string)"
            @delete="k => panel.deleteTab(k as string)">
      <!-- 右侧按钮 -->
      <template #extra>
        <a-space class="panel-extra">
          <span class="extra-icon" @click="close">
            <icon-close />
          </span>
        </a-space>
      </template>
      <!-- 终端面板 -->
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
  import type { ITerminalTabManager, TerminalPanelTabItem } from '../../types/terminal.type';
  import { watch } from 'vue';
  import { useTerminalStore } from '@/store';
  import { PanelSessionType } from '../../types/terminal.const';
  import TerminalView from '../xterm/terminal-view.vue';

  const props = defineProps<{
    index: number,
    panel: ITerminalTabManager<TerminalPanelTabItem>,
  }>();

  const emits = defineEmits(['close', 'openNewConnect']);

  const { sessionManager } = useTerminalStore();

  // 监听 tab 切换
  watch(() => props.panel.active, (active, before) => {
    // 失焦自动终端
    if (before) {
      const beforeTab = props.panel.items.find(s => s.key === before);
      if (beforeTab && beforeTab?.type === PanelSessionType.TERMINAL.type) {
        sessionManager.getSession(before)?.blur();
      }
    }
    // 终端自动聚焦
    if (active) {
      const activeTab = props.panel.items.find(s => s.key === active);
      if (activeTab && activeTab?.type === PanelSessionType.TERMINAL.type) {
        sessionManager.getSession(active)?.focus();
      }
    }
    // 无终端自动关闭
    if (!props.panel.items.length) {
      close();
    }
  });

  // 打开主机模态框
  const openNewConnect = () => {
    emits('openNewConnect', props.index);
  };

  // 关闭面板
  const close = () => {
    emits('close', props.index);
  };

</script>

<style lang="less" scoped>
  .terminal-panel-container {
    width: 100%;
    height: 100%;
  }

  .tab-title-wrapper {
    display: flex;
    align-items: center;

    .tab-title-icon {
      font-size: 18px;
      margin-right: 6px;
    }
  }

  .panel-extra {
    margin-right: 8px;

    .extra-icon {
      color: var(--color-panel-text-2);
      transition: 0.2s;
      font-size: 16px;
      cursor: pointer;
      width: 20px;
      height: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;

      &:hover {
        background: var(--color-bg-panel-icon-1);
      }
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

    &-add-btn {
      padding: 0 14px;

      .arco-icon-hover {
        font-size: 14px;
      }
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
      padding: 11px 18px 11px 14px;
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
