<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <host-connect-log-table />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'assetHostConnectLog'
  };
</script>

<script lang="ts" setup>
  import HostConnectLogTable from './components/host-connect-log-table.vue';
  import { ref, onBeforeMount, onUnmounted } from 'vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { dictKeys } from './types/const';

  const render = ref(false);
  const table = ref();
  const modal = ref();

  // 加载字典配置
  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

  // 重置缓存
  onUnmounted(() => {
    const cacheStore = useCacheStore();
    cacheStore.reset('users', 'hosts');
  });

</script>

<style lang="less" scoped>

</style>
