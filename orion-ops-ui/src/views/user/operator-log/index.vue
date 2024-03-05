<template>
  <div class="layout-container" v-if="render">
    <operator-log-table />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'userOperatorLog'
  };
</script>

<script lang="ts" setup>
  import { ref, onUnmounted, onBeforeMount } from 'vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import OperatorLogTable from './components/operator-log-table.vue';

  const cacheStore = useCacheStore();

  const render = ref(false);

  // 加载字典值
  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.reset('users');
  });

</script>

<style lang="less" scoped>
</style>
