<template>
  <div class="layout-container">
    <!-- 表格 -->
    <host-table ref="table"
                @openAdd="() => modal.openAdd()"
                @openUpdate="(e) => modal.openUpdate(e)"
                @openUpdateConfig="(e) => config.open(e)" />
    <!-- 添加修改模态框 -->
    <host-form-modal ref="modal"
                     @added="() => table.addedCallback()"
                     @updated="() => table.updatedCallback()" />
    <!-- 配置面板 -->
    <host-config-drawer ref="config" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'assetHost'
  };
</script>

<script lang="ts" setup>
  import HostTable from './components/host-table.vue';
  import HostFormModal from './components/host-form-modal.vue';
  import { onUnmounted, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import HostConfigDrawer from '@/views/asset/host/components/host-config-drawer.vue';

  const table = ref();
  const modal = ref();
  const config = ref();

  // 卸载时清除 tags cache
  onUnmounted(() => {
    const cacheStore = useCacheStore();
    cacheStore.set('tags', []);
  });

</script>

<style lang="less" scoped>

</style>
