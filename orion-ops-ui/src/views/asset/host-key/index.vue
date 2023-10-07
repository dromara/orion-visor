<template>
  <div class="layout-container">
    <!-- 列表-表格 -->
    <host-key-table v-if="renderTable"
                    ref="table"
                    @openAdd="() => drawer.openAdd()"
                    @openUpdate="(e) => drawer.openUpdate(e)" />
    <!-- 列表-卡片 -->
    <host-key-card-list v-else
                        ref="card"
                        @openAdd="() => drawer.openAdd()"
                        @openUpdate="(e) => drawer.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <host-key-form-drawer ref="drawer"
                          @added="modalAddCallback"
                          @updated="modalUpdateCallback" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'assetHostKey'
  };
</script>

<script lang="ts" setup>
  import { computed, ref } from 'vue';
  import { useAppStore } from '@/store';
  import HostKeyCardList from './components/host-key-card-list.vue';
  import HostKeyTable from './components/host-key-table.vue';
  import HostKeyFormDrawer from './components/host-key-form-drawer.vue';

  const table = ref();
  const card = ref();
  const drawer = ref();
  const appStore = useAppStore();

  const renderTable = computed(() => appStore.hostKeyView === 'table');

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

</script>

<style lang="less" scoped>

</style>
