<template>
  <div class="layout-container">
    <!-- 列表-表格 -->
    <host-table v-if="renderTable"
                ref="table"
                @openAdd="() => modal.openAdd()"
                @openUpdate="(e) => modal.openUpdate(e)"
                @openUpdateConfig="(e) => config.open(e)" />
    <!-- 列表-卡片 -->
    <host-card-list v-else
                    ref="card"
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
  import { computed, onUnmounted, ref } from 'vue';
  import { useAppStore, useCacheStore } from '@/store';
  import HostTable from './components/host-table.vue';
  import HostCardList from '@/views/asset/host/components/host-card-list.vue';
  import HostFormModal from './components/host-form-modal.vue';
  import HostConfigDrawer from '@/views/asset/host/components/host-config-drawer.vue';

  const table = ref();
  const card = ref();
  const modal = ref();
  const config = ref();
  const appStore = useAppStore();

  // FIXME 临时
  const renderTable = computed(() => appStore.hostView === 'card');

  // 卸载时清除 tags cache
  onUnmounted(() => {
    const cacheStore = useCacheStore();
    cacheStore.set('tags', []);
    cacheStore.set('hostKeys', []);
    cacheStore.set('hostIdentities', []);
  });

</script>

<style lang="less" scoped>

</style>
