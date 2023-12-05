<template>
  <div class="host-layout">
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
        <terminal-content>
          <div class="my16 mx16">
            <a-button @click="changeTheme">
              {{ darkTheme }}
            </a-button>
          </div>
        </terminal-content>
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
  import TerminalHeader from './components/layout/terminal-header.vue';
  import TerminalLeftSidebar from './components/layout/terminal-left-sidebar.vue';
  import TerminalRightSidebar from './components/layout/terminal-right-sidebar.vue';
  import TerminalContent from './components/terminal-content.vue';
  import { useDark } from '@vueuse/core';
  import './assets/styles/layout.less';

  // 主题
  const darkTheme = useDark({
    selector: 'body',
    attribute: 'terminal-theme',
    valueDark: 'dark',
    valueLight: 'light',
    initialValue: 'dark',
    storageKey: null
  });

  const changeTheme = () => {
    console.log('current', darkTheme.value);
    darkTheme.value = !darkTheme.value;
  };

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
      overflow: hidden;
      position: relative;
      display: flex;
      justify-content: space-between;
    }

    &-left, &-right {
      width: var(--sidebar-width);
      height: 100%;
      background: var(--color-bg-sidebar);
      border-top: 1px solid var(--color-bg-content);
    }

    &-content {
      width: 100%;
      height: 100%;
      background: var(--color-bg-content);
    }
  }
</style>
