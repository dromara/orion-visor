<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <sftp-log-table />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'assetAuditSftpLog'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount, onUnmounted } from 'vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import SftpLogTable from './components/sftp-log-table.vue';

  const render = ref(false);

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
