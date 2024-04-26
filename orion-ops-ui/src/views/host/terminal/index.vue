<template>
  <div class="host-terminal-layout" v-if="render">
    <!-- 头部区域 -->
    <header class="host-terminal-layout-header">
      <layout-header />
    </header>
    <!-- 主体区域 -->
    <main class="host-terminal-layout-main">
      <!-- 左侧操作栏 -->
      <div class="host-terminal-layout-left">
        <left-sidebar />
      </div>
      <!-- 内容区域 -->
      <main class="host-terminal-layout-content">
        <!-- 主机加载中骨架 -->
        <loading-skeleton v-if="contentLoading" />
        <!-- 终端内容区域 -->
        <main-content v-else
                      @open-command-snippet="() => snippetRef.open()"
                      @open-path-bookmark="() => pathRef.open()"
                      @open-transfer-list="() => transferRef.open()"
                      @screenshot="screenshot" />
      </main>
      <!-- 右侧操作栏 -->
      <div class="host-terminal-layout-right">
        <right-sidebar @open-command-snippet="() => snippetRef.open()"
                       @open-path-bookmark="() => pathRef.open()"
                       @open-transfer-list="() => transferRef.open()"
                       @screenshot="screenshot" />
      </div>
    </main>
    <!-- 命令片段列表抽屉 -->
    <command-snippet-list-drawer ref="snippetRef" />
    <!-- 路径书签列表抽屉 -->
    <path-bookmark-list-drawer ref="pathRef" />
    <!-- 传输列表 -->
    <transfer-drawer ref="transferRef" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminal'
  };
</script>

<script lang="ts" setup>
  import type { ISshSession } from './types/terminal.type';
  import { ref, onBeforeMount, onUnmounted, onMounted } from 'vue';
  import { dictKeys, PanelSessionType, TerminalTabs } from './types/terminal.const';
  import { useCacheStore, useDictStore, useTerminalStore } from '@/store';
  import useLoading from '@/hooks/loading';
  import debug from '@/utils/env';
  import LayoutHeader from './components/layout/layout-header.vue';
  import LeftSidebar from './components/layout/left-sidebar.vue';
  import RightSidebar from './components/layout/right-sidebar.vue';
  import MainContent from './components/layout/main-content.vue';
  import LoadingSkeleton from './components/layout/loading-skeleton.vue';
  import TransferDrawer from '@/views/host/terminal/components/transfer/transfer-drawer.vue';
  import CommandSnippetListDrawer from '@/views/host/command-snippet/components/command-snippet-list-drawer.vue';
  import PathBookmarkListDrawer from '@/views/host/path-bookmark/components/path-bookmark-list-drawer.vue';
  import '@/assets/style/host-terminal-layout.less';
  import 'xterm/css/xterm.css';

  const terminalStore = useTerminalStore();
  const dictStore = useDictStore();
  const cacheStore = useCacheStore();
  const { loading: contentLoading, setLoading: setContentLoading } = useLoading(true);

  const originTitle = document.title;
  const render = ref(false);
  const snippetRef = ref();
  const pathRef = ref();
  const transferRef = ref();

  // 终端截屏
  const screenshot = () => {
    const handler = terminalStore.getCurrentSession<ISshSession>(PanelSessionType.SSH.type, true)?.handler;
    if (handler && handler.enabledStatus('screenshot')) {
      handler.screenshot();
    }
  };

  // 关闭视口处理
  const handleBeforeUnload = (event: any) => {
    event.preventDefault();
    event.returnValue = confirm('系统可能不会保存您所做的更改');
  };

  // 加载用户终端偏好
  onBeforeMount(async () => {
    await terminalStore.fetchPreference();
    // 设置系统主题配色
    const dark = terminalStore.preference.theme.dark;
    document.body.setAttribute('terminal-theme', dark ? 'dark' : 'light');
    render.value = true;
  });

  // 加载字典值
  onBeforeMount(async () => {
    await dictStore.loadKeys(dictKeys);
  });

  // 加载主机信息
  onMounted(async () => {
    try {
      await terminalStore.loadHosts();
    } catch (e) {
    } finally {
      setContentLoading(false);
    }
  });

  // 加载处理
  onMounted(() => {
    // 默认标题
    document.title = TerminalTabs.NEW_CONNECTION.title;
    // 注册关闭视口事件
    if (!debug) {
      window.addEventListener('beforeunload', handleBeforeUnload);
    }
  });

  // 卸载处理
  onUnmounted(() => {
    // 卸载时清除 cache
    cacheStore.reset('authorizedHostKeys', 'authorizedHostIdentities', 'commandSnippetGroups');
    // 移除关闭视口事件
    window.removeEventListener('beforeunload', handleBeforeUnload);
    // 去除 body style
    document.body.removeAttribute('terminal-theme');
    // 重置 title
    document.title = originTitle;
  });

</script>

<style lang="less" scoped>
  .host-terminal-layout {
    width: 100%;
    height: 100vh;
    position: relative;
    color: var(--color-content-text-2);

    &-header {
      width: 100%;
      height: var(--header-height);
      background: var(--color-bg-header);
      position: relative;
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
      overflow: hidden;
    }

    &-left {
      border-right: 1px solid var(--color-bg-content);
    }

    &-right {
      border-left: 1px solid var(--color-bg-content);
    }

    &-content {
      width: calc(100% - var(--sidebar-width) * 2);
      height: 100%;
      background: var(--color-bg-content);
      overflow: auto;
    }
  }

</style>
