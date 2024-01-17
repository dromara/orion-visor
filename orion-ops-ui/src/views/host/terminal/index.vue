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
      <div class="host-layout-content" ref="content">
        <!-- 主机加载中骨架 -->
        <loading-skeleton v-if="contentLoading" />
        <!-- 终端内容区域 -->
        <terminal-content v-else />
      </div>
      <!-- 右侧操作栏 -->
      <div class="host-layout-right">
        <terminal-right-sidebar @screenshot="screenshotTerminal" />
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
  import TerminalHeader from './components/layout/terminal-header.vue';
  import TerminalLeftSidebar from './components/layout/terminal-left-sidebar.vue';
  import TerminalRightSidebar from './components/layout/terminal-right-sidebar.vue';
  import TerminalContent from './components/layout/terminal-content.vue';
  import LoadingSkeleton from './components/layout/loading-skeleton.vue';
  import html2canvas from 'html2canvas';
  import { saveAs } from 'file-saver';
  import './assets/styles/layout.less';
  import 'xterm/css/xterm.css';
  import { Message } from '@arco-design/web-vue';

  const terminalStore = useTerminalStore();
  const dictStore = useDictStore();
  const cacheStore = useCacheStore();
  const { loading: contentLoading, setLoading: setContentLoading } = useLoading(true);

  const originTitle = document.title;
  const render = ref(false);
  const content = ref();

  // 关闭视口处理
  const handleBeforeUnload = (event: any) => {
    event.preventDefault();
    event.returnValue = confirm('系统可能不会保存您所做的更改');
  };

  // 终端截图
  // FIXME test 水印
  const screenshotTerminal = async () => {
    try {
      console.log(content.value);
      const canvas = await html2canvas(content.value, {
        useCORS: true,
        backgroundColor: 'transparent',
      });
      console.log(canvas);
      const blob = await new Promise((resolve, reject) => {
        canvas.toBlob((blob) => {
          if (!blob) {
            reject(new Error('截屏失败'));
          }
          resolve(blob);
        }, 'image/png');
      });
      saveAs(blob, `screenshot-${Date.now()}.png`);
    } catch (e) {
      Message.error('保存失败');
    }
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
    cacheStore.reset('authorizedHostKeys', 'authorizedHostIdentities');
    // 移除关闭视口事件
    window.removeEventListener('beforeunload', handleBeforeUnload);
    // 去除 body style
    document.body.removeAttribute('terminal-theme');
    // 重置 title
    document.title = originTitle;
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
      height: var(--header-height);
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
