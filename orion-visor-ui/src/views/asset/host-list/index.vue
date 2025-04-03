<template>
  <div class="layout-container">
    <!-- 列表-表格 -->
    <host-table v-if="renderTable"
                ref="table"
                @open-host-group="() => hostGroup.open()"
                @open-copy="(e) => drawer.openCopy(e.id)"
                @open-add="() => drawer.openAdd()"
                @open-update="(e) => drawer.openUpdate(e.id)" />
    <!-- 列表-卡片 -->
    <host-card-list v-else
                    ref="card"
                    @open-host-group="() => hostGroup.open()"
                    @open-copy="(e) => drawer.openCopy(e.id)"
                    @open-add="() => drawer.openAdd()"
                    @open-update="(e) => drawer.openUpdate(e.id)" />
    <!-- 添加修改抽屉 -->
    <host-form-drawer ref="drawer" @reload="reload" />
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
  import { computed, ref, onBeforeMount } from 'vue';
  import { useAppStore, useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import HostTable from './components/host-table.vue';
  import HostCardList from './components/host-card-list.vue';
  import HostFormDrawer from './components/host-form-drawer.vue';
  import HostGroupDrawer from '../host-group/drawer/index.vue';

  const table = ref();
  const card = ref();
  const drawer = ref();
  const hostGroup = ref();

  const appStore = useAppStore();

  const renderTable = computed(() => appStore.hostView === 'table');

  // 添加回调
  const reload = () => {
    if (renderTable.value) {
      table.value.reload();
    } else {
      card.value.reload();
    }
  };

  // 加载字典配置
  onBeforeMount(() => {
    useDictStore().loadKeys(dictKeys);
  });

</script>

<style lang="less" scoped>

</style>
