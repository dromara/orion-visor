<template>
  <div class="layout-container">
    <!-- 列表-表格 -->
    <host-key-table v-if="renderTable"
                    ref="table"
                    @open-view="(e) => drawer.openView(e)"
                    @open-add="() => drawer.openAdd()"
                    @open-update="(e) => drawer.openUpdate(e)" />
    <!-- 列表-卡片 -->
    <host-key-card-list v-else
                        ref="card"
                        @open-view="(e) => drawer.openView(e)"
                        @open-add="() => drawer.openAdd()"
                        @open-update="(e) => drawer.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <host-key-form-drawer ref="drawer"
                          @added="reload"
                          @updated="reload" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'hostKey'
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

  // 重新加载
  const reload = () => {
    if (renderTable.value) {
      table.value.reload();
    } else {
      card.value.reload();
    }
  };

</script>

<style lang="less" scoped>

</style>
