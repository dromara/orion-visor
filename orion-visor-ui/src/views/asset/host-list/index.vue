<template>
  <div class="layout-container">
    <!-- 列表-表格 -->
    <host-table v-if="renderTable"
                ref="table"
                @open-host-group="() => hostGroup.open()"
                @open-copy="(e) => modal.openCopy(e.id)"
                @open-add="() => modal.openAdd()"
                @open-update="(e) => modal.openUpdate(e.id)"
                @open-update-config="(e) => hostConfig.open(e)" />
    <!-- 列表-卡片 -->
    <host-card-list v-else
                    ref="card"
                    @open-host-group="() => hostGroup.open()"
                    @open-copy="(e) => modal.openCopy(e.id)"
                    @open-add="() => modal.openAdd()"
                    @open-update="(e) => modal.openUpdate(e.id)"
                    @open-update-config="(e) => hostConfig.open(e)" />
    <!-- 添加修改模态框 -->
    <host-form-modal ref="modal"
                     @added="reload"
                     @updated="reload" />
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
  import { computed, ref, onUnmounted, onBeforeMount } from 'vue';
  import { useAppStore, useCacheStore, useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import HostTable from './components/host-table.vue';
  import HostCardList from './components/host-card-list.vue';
  import HostFormModal from './components/host-form-modal.vue';
  import HostConfigDrawer from '../host-config/drawer/index.vue';
  import HostGroupDrawer from '../host-group/drawer/index.vue';

  const table = ref();
  const card = ref();
  const modal = ref();
  const hostConfig = ref();
  const hostGroup = ref();

  const appStore = useAppStore();
  const cacheStore = useCacheStore();

  const renderTable = computed(() => appStore.hostView === 'table');

  // 重新加载
  const reload = () => {
    if (renderTable.value) {
      table.value.reload();
    } else {
      card.value.reload();
    }
  };

  // 加载字典配置
  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
  });

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.reset('hostKeys', 'hostIdentities', 'hostGroups', 'HOST_Tags');
  });

</script>

<style lang="less" scoped>

</style>
