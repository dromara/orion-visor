<template>
  <div class="host-layout" v-if="render">
    <!-- 头部区域 -->
    <header class="host-layout-header">
      <terminal-header v-model="activeKey"
                       :tabs="tabs"
                       @click-tab="clickTab"
                       @deleteTab="deleteTab" />
    </header>
    <!-- 主体区域 -->
    <main class="host-layout-main">
      <!-- 左侧操作栏 -->
      <div class="host-layout-left">
        <terminal-left-sidebar @switch-tab="switchTab" />
      </div>
      <!-- 内容区域 -->
      <div class="host-layout-content">
        <terminal-content v-model="activeKey"
                          :tabs="tabs" />
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
  import type { TabItem } from './types/terminal.const';
  import { ref, onBeforeMount } from 'vue';
  import { TabType, InnerTabs, dictKeys } from './types/terminal.const';
  import { useDictStore, useTerminalStore } from '@/store';
  import TerminalHeader from './components/layout/terminal-header.vue';
  import TerminalLeftSidebar from './components/layout/terminal-left-sidebar.vue';
  import TerminalRightSidebar from './components/layout/terminal-right-sidebar.vue';
  import TerminalContent from './components/layout/terminal-content.vue';
  import './assets/styles/layout.less';
  import 'xterm/css/xterm.css';

  const terminalStore = useTerminalStore();
  const dictStore = useDictStore();

  const render = ref(false);
  const activeKey = ref(InnerTabs.NEW_CONNECTION.key);
  const tabs = ref<Array<TabItem>>([InnerTabs.NEW_CONNECTION]);
  for (let i = 0; i < 3; i++) {
    tabs.value.push({
      key: `host${i}`,
      title: `主机name ${i}`,
      type: TabType.TERMINAL
    });
  }

  // 点击 tab
  const clickTab = (key: string) => {
    activeKey.value = key;
  };

  // 删除 tab
  const deleteTab = (key: string) => {
    const tabIndex = tabs.value.findIndex(s => s.key === key);
    tabs.value.splice(tabIndex, 1);
    if (key === activeKey.value && tabs.value.length !== 0) {
      // 切换为前一个 tab
      activeKey.value = tabs.value[Math.max(tabIndex - 1, 0)].key;
    }
  };

  // 切换 tab
  const switchTab = (tab: TabItem) => {
    // 不存在则创建tab
    if (!tabs.value.find(s => s.key === tab.key)) {
      tabs.value.push(tab);
    }
    activeKey.value = tab.key;
  };

  // 加载用户终端偏好
  onBeforeMount(async () => {
    await terminalStore.fetchPreference();
    render.value = true;
  });

  // 加载字典值
  onBeforeMount(async () => {
    await dictStore.loadKeys([...dictKeys]);
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
