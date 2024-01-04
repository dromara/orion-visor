<template>
  <div class="host-layout" v-if="render">
    <!-- 头部区域 -->
    <header class="host-layout-header">
      <terminal-header />
    </header>
    <!-- 主体区域 -->
    <main class="host-layout-main">
      <!-- 左侧操作栏 -->
      <div class="host-layout-left">
        <terminal-left-sidebar />
      </div>
      <!-- 内容区域 -->
      <div class="host-layout-content">
        <terminal-content />
      </div>
      <!-- 右侧操作栏 -->
      <div class="host-layout-right">
        <terminal-right-sidebar />
      </div>
    </main>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'hostTerminal'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount, onUnmounted } from 'vue';
  import { dictKeys } from './types/terminal.const';
  import { useCacheStore, useDictStore, useTerminalStore } from '@/store';
  import TerminalHeader from './components/layout/terminal-header.vue';
  import TerminalLeftSidebar from './components/layout/terminal-left-sidebar.vue';
  import TerminalRightSidebar from './components/layout/terminal-right-sidebar.vue';
  import TerminalContent from './components/layout/terminal-content.vue';
  import './assets/styles/layout.less';
  import 'xterm/css/xterm.css';

  const terminalStore = useTerminalStore();
  const dictStore = useDictStore();
  const cacheStore = useCacheStore();

  const render = ref(false);

  // 加载用户终端偏好
  onBeforeMount(async () => {
    await terminalStore.fetchPreference();
    render.value = true;
  });

  // 加载字典值
  onBeforeMount(async () => {
    await dictStore.loadKeys([...dictKeys]);
  });

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.reset('authorizedHostKeys', 'authorizedHostIdentities');
  });

</script>

<style lang="less" scoped>
  .host-layout {
    width: 100%;
    height: 100vh;
    position: relative;
    color: var(--color-content-text-2);

    &-header {
      width: 100%;
      height: 44px;
      background: var(--color-bg-header);
      position: relative;
      z-index: 9999;
    }

    &-main {
      width: 100%;
      height: calc(100% - var(--sidebar-width));
      position: relative;
      display: flex;
      justify-content: space-between;
    }

    &-left, &-right {
      width: var(--sidebar-width);
      height: 100%;
      background: var(--color-bg-sidebar);
      border-top: 1px solid var(--color-bg-content);
      overflow: hidden;
    }

    &-content {
      width: calc(100% - var(--sidebar-width) * 2);
      height: 100%;
      background: var(--color-bg-content);
      overflow: auto;
    }
  }

</style>
