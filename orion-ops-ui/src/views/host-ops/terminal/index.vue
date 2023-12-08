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
                          :tabs="tabs"
                          :preference="preference as TerminalPreference"
                          @change-dark-theme="changeLayoutTheme" />
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
  import type { TabItem, TerminalPreference } from './types/terminal.type';
  import { ref, onBeforeMount } from 'vue';
  import { useDark } from '@vueuse/core';
  import { TabType, InnerTabs, DarkTheme, dictKeys } from './types/terminal.type';
  import { DEFAULT_SCHEMA } from './types/terminal.theme';
  import { useDictStore } from '@/store';
  import { getPreference } from '@/api/user/preference';
  import { Message } from '@arco-design/web-vue';
  import TerminalHeader from './components/layout/terminal-header.vue';
  import TerminalLeftSidebar from './components/layout/terminal-left-sidebar.vue';
  import TerminalRightSidebar from './components/layout/terminal-right-sidebar.vue';
  import TerminalContent from './components/layout/terminal-content.vue';
  import './assets/styles/layout.less';
  import '@xterm/xterm/css/xterm.css';

  // 系统主题
  const darkTheme = useDark({
    selector: 'body',
    attribute: 'terminal-theme',
    valueDark: DarkTheme.DARK,
    valueLight: DarkTheme.LIGHT,
    initialValue: DarkTheme.DARK as any,
    storageKey: null
  });
  const dictStore = useDictStore();

  const render = ref(false);
  const activeKey = ref(InnerTabs.THEME_SETTING.key);
  const tabs = ref<Array<TabItem>>([InnerTabs.THEME_SETTING]);
  const preference = ref<TerminalPreference>();
  for (let i = 0; i < 3; i++) {
    tabs.value.push({
      key: `host${i}`,
      title: `主机name ${i}`,
      type: TabType.TERMINAL
    });
  }

  // 切换系统主题
  const changeLayoutTheme = (dark: boolean) => {
    darkTheme.value = dark;
  };

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
    try {
      const { data } = await getPreference<TerminalPreference>('TERMINAL');
      // 设置默认终端主题
      if (!data.config.terminalTheme?.name) {
        data.config.terminalTheme = DEFAULT_SCHEMA;
      }
      preference.value = data.config;
      // 设置暗色主题
      const userDarkTheme = data.config.darkTheme;
      if (userDarkTheme === DarkTheme.AUTO) {
        changeLayoutTheme(data.config.terminalTheme?.dark === true);
      } else {
        changeLayoutTheme(userDarkTheme === DarkTheme.DARK);
      }
      render.value = true;
    } catch (e) {
      Message.error('配置加载失败');
    }
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
      width: 100%;
      height: 100%;
      background: var(--color-bg-content);
      overflow: auto;
    }
  }

</style>
