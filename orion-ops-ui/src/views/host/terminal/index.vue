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
        <main-content v-else />
      </main>
      <!-- 右侧操作栏 -->
      <div class="host-terminal-layout-right">
        <right-sidebar />
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
  import { ref, onBeforeMount, onUnmounted, onMounted } from 'vue';
  import { dictKeys, InnerTabs } from './types/terminal.const';
  import { useCacheStore, useDictStore, useTerminalStore } from '@/store';
  import useLoading from '@/hooks/loading';
  import LayoutHeader from './components/layout/layout-header.vue';
  import LeftSidebar from './components/layout/left-sidebar.vue';
  import RightSidebar from './components/layout/right-sidebar.vue';
  import MainContent from './components/layout/main-content.vue';
  import LoadingSkeleton from './components/layout/loading-skeleton.vue';
  import '@/assets/style/host-terminal-layout.less';
  import 'xterm/css/xterm.css';

  const terminalStore = useTerminalStore();
  const dictStore = useDictStore();
  const cacheStore = useCacheStore();
  const { loading: contentLoading, setLoading: setContentLoading } = useLoading(true);

  const originTitle = document.title;
  const render = ref(false);

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
    await dictStore.loadKeys([...dictKeys]);
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

  // 事件处理
  onMounted(() => {
    // 默认标题
    document.title = InnerTabs.NEW_CONNECTION.title;
    // 注册关闭视口事件
    // FIXME 开发阶段
    // window.addEventListener('beforeunload', handleBeforeUnload);
  });

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
      border-top: 1px solid var(--color-bg-content);
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
