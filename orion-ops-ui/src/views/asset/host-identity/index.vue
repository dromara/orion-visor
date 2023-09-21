<template>
  <div class="layout-container">
    <!-- 表格 -->
    <host-identity-table ref="table"
                         @openAdd="() => modal.openAdd()"
                         @openUpdate="(e) => modal.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <host-identity-form-modal ref="modal"
                              @added="() => table.addedCallback()"
                              @updated="() => table.updatedCallback()" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'assetHostIdentity'
  };
</script>

<script lang="ts" setup>
  import HostIdentityTable from './components/host-identity-table.vue';
  import HostIdentityFormModal from './components/host-identity-form-modal.vue';

  import { onUnmounted, ref } from 'vue';
  import { useCacheStore } from '@/store';

  const table = ref();
  const modal = ref();

  // 卸载时清除 tags cache
  onUnmounted(() => {
    const cacheStore = useCacheStore();
    cacheStore.set('hostKeys', []);
  });

</script>

<style lang="less" scoped>

</style>
