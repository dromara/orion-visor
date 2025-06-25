<template>
  <div class="terminal-panel-container">
    <!-- 终端 tab -->
    <a-tabs v-model:active-key="panel.active"
            :editable="true"
            :auto-switch="false"
            :show-add-button="true"
            @add="openNewConnect"
            @tab-click="(k) => panel.clickTab(k as string)"
            @delete="(k) => panel.deleteTab(k as string)">
      <!-- 右侧按钮 -->
      <template #extra>
        <a-space class="panel-extra">
          <!-- 关闭 -->
          <span class="extra-icon"
                title="关闭面板"
                @click.stop.prevent="close">
            <icon-close />
          </span>
        </a-space>
      </template>
      <!-- 终端面板 -->
      <a-tab-pane v-for="item in panel.items" :key="item.key">
        <!-- 标题 -->
        <template #title>
          <span class="tab-title-wrapper usn"
                :style="{
                  '--color': getDictValue(tabColorKey, item.color, 'color', 'transparent'),
                  '--bg': panel.active === item.key ? getDictValue(tabColorKey, item.color, 'bg', 'transparent') : 'transparent',
                }"
                @dblclick="copySession(item, index)">
            <span class="tab-title-icon">
              <component :is="item.icon" />
            </span>
            {{ item.title }}
          </span>
        </template>
        <!-- ssh -->
        <ssh-view v-if="item.type === TerminalSessionTypes.SSH.type" :item="item" />
        <!-- sftp -->
        <sftp-view v-else-if="item.type === TerminalSessionTypes.SFTP.type" :item="item" />
        <!-- rdp -->
        <rdp-view v-else-if="item.type === TerminalSessionTypes.RDP.type" :item="item" />
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
  import type { ITerminalTabManager, TerminalSessionTabItem, ITerminalSession, IDomViewportHandler } from '@/views/terminal/interfaces';
  import { ref, watch } from 'vue';
  import { useDictStore, useTerminalStore } from '@/store';
  import { tabColorKey, TerminalSessionTypes } from '../../types/const';
  import SshView from '../view/ssh/ssh-view.vue';
  import SftpView from '../view/sftp/sftp-view.vue';
  import RdpView from '../view/rdp/rdp-view.vue';

  const props = defineProps<{
    index: number;
    panel: ITerminalTabManager<TerminalSessionTabItem>;
  }>();

  const emits = defineEmits(['close', 'openNewConnect', 'split']);

  const { sessionManager, copySession } = useTerminalStore();
  const { getDictValue } = useDictStore();

  const closed = ref(false);

  // 监听 tab 切换
  watch(() => props.panel.active, (active, before) => {
    // 失焦自动终端
    if (before) {
      const beforeTab = props.panel.items.find(s => s.key === before);
      if (beforeTab) {
        (sessionManager.getSession<ITerminalSession>(beforeTab.key) as unknown as IDomViewportHandler)?.blur?.();
      }
    }
    // 终端自动聚焦
    if (active) {
      const activeTab = props.panel.items.find(s => s.key === active);
      if (activeTab) {
        (sessionManager.getSession<ITerminalSession>(activeTab.key) as unknown as IDomViewportHandler)?.focus?.();
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
    if (!closed.value) {
      closed.value = true;
      emits('close', props.index);
    }
    // 重置 closed
    setTimeout(() => {
      closed.value = false;
    }, 1000);
  };

</script>

<style lang="less" scoped>
  .terminal-panel-container {
    width: 100%;
    height: 100%;
  }

  .tab-title-wrapper {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    padding: 11px 18px 9px 14px;
    background: var(--bg);
    position: relative;
    transition: all .3s;

    .tab-title-icon {
      font-size: 16px;
      margin-right: 6px;
    }

    &:hover {
      filter: brightness(1.04);
    }

    &::after {
      content: '';
      width: calc(100% - 3px);
      height: 2px;
      background: var(--color);
      position: absolute;
      left: 1px;
      bottom: -1px;
    }
  }

  .panel-extra {
    margin-right: 8px;

    .extra-icon {
      color: var(--color-panel-text-1);
      transition: 0.2s;
      font-size: 16px;
      cursor: pointer;
      width: 24px;
      height: 24px;
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

      .arco-tabs-content-list {
        height: 100%;
      }

      .arco-tabs-content-item {
        height: 100%;
      }

      .arco-tabs-pane {
        height: 100%;
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
    margin: 0 !important;
    padding: 0 !important;
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
    background: var(--color-bg-panel-tabs-active) !important;
    color: var(--color-panel-text-2) !important;

    .arco-tabs-tab-title {
      background: var(--color-bg-panel-tabs-active);
    }

    &:hover::after {
      background: linear-gradient(270deg, var(--color-panel-gradient-start) 45%, var(--color-panel-gradient-end) 120%);
    }
  }

</style>
