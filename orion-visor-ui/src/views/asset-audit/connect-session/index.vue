<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <connect-session-table @open-watcher="openWatcher"
                           @open-event="(s) => eventDrawer.open(s)" />
    <!-- 监视模态框 -->
    <xterm-watcher-modal ref="watcherModal" />
    <!-- 事件抽屉 -->
    <connect-event-drawer ref="eventDrawer" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'connectSession'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import { openNewRoute } from '@/router';
  import ConnectSessionTable from './components/connect-session-table.vue';
  import ConnectEventDrawer from '../connect-log/components/connect-event-drawer.vue';
  import XtermWatcherModal from '@/components/xterm/watcher/modal/index.vue';

  const render = ref(false);
  const watcherModal = ref();
  const eventDrawer = ref();

  // 打开监视
  const openWatcher = (sessionId: string, newWindow: boolean) => {
    if (newWindow) {
      // 跳转新页面
      openNewRoute({
        name: 'terminalWatcher',
        query: { sessionId }
      });
    } else {
      watcherModal.value.open(sessionId);
    }
  };

  // 加载字典配置
  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
