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
                    :create-card-position="'head'"
                    :card-height="180"
                    :list="list"
                    :pagination="pagination"
                    ref="card"
                    @openAdd="() => modal.openAdd()"
                    @openUpdate="(e) => modal.openUpdate(e)"
                    @openUpdateConfig="(e) => config.open(e)">

      <template #title="{ record }">
        {{ record.name }}
      </template>
      <template #extra="{ index }">
        {{ index }}
      </template>
      <template #card="{ record }">
        {{ record }}
      </template>
    </host-card-list>
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
  import { useCardPagination } from '@/types/table';

  const pagination = useCardPagination();
  const list = ref<Array<any>>([]);

  for (let i = 0; i < 270; i++) {
    list.value.push({
      id: i + 1,
      name: `名称 ${i + 1}`,
      host: `192.168.1.${i}`,
      disabled: i === 0
    });
  }
  pagination.total = 270;

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
