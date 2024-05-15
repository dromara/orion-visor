<template>
  <div class="layout-container">
    <!-- 列表-表格 -->
    <host-table v-if="renderTable"
                ref="table"
                @open-host-group="() => hostGroup.open()"
                @open-add="() => modal.openAdd()"
                @open-update="(e) => modal.openUpdate(e.id)"
                @open-update-config="(e) => hostConfig.open(e)" />
    <!-- 列表-卡片 -->
    <host-card-list v-else
                    ref="card"
                    @open-host-group="() => hostGroup.open()"
                    @open-add="() => modal.openAdd()"
                    @open-update="(e) => modal.openUpdate(e.id)"
                    @open-update-config="(e) => hostConfig.open(e)" />
    <!-- 添加修改模态框 -->
    <host-form-modal ref="modal"
                     @added="modalAddCallback"
                     @updated="modalUpdateCallback" />
    <!-- 配置面板 -->
    <host-config-drawer ref="hostConfig" />
    <!-- 分组配置 -->
    <host-group-drawer ref="hostGroup" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'hostList'
  };
</script>

<script lang="ts" setup>
  import { computed, ref, onUnmounted } from 'vue';
  import { useAppStore, useCacheStore } from '@/store';
  import HostTable from './components/host-table.vue';
  import HostCardList from './components/host-card-list.vue';
  import HostFormModal from './components/host-form-modal.vue';
  import HostConfigDrawer from './components/config/host-config-drawer.vue';
  import HostGroupDrawer from './components/group/host-group-drawer.vue';

  const table = ref();
  const card = ref();
  const modal = ref();
  const hostConfig = ref();
  const hostGroup = ref();

  const appStore = useAppStore();
  const cacheStore = useCacheStore();

  const renderTable = computed(() => appStore.hostView === 'table');

  // 添加回调
  const modalAddCallback = () => {
    if (renderTable.value) {
      table.value.addedCallback();
    } else {
      card.value.addedCallback();
    }
  };

  // 修改回调
  const modalUpdateCallback = () => {
    if (renderTable.value) {
      table.value.updatedCallback();
    } else {
      card.value.updatedCallback();
    }
  };

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.reset('hosts', 'hostKeys', 'hostIdentities', 'hostGroups', 'HOST_Tags');
  });

</script>

<style lang="less" scoped>

</style>
