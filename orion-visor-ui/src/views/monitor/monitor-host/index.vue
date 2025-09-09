<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <monitor-host-table v-if="renderTable"
                        ref="table"
                        @open-upload="() => uploadModal.open()"
                        @open-update="(e: any) => drawer.openUpdate(e)" />
    <!-- 列表-卡片 -->
    <monitor-host-card-list v-else
                            ref="card"
                            @open-upload="() => uploadModal.open()"
                            @open-update="(e: any) => drawer.openUpdate(e)" />
    <!-- 添加修改抽屉 -->
    <monitor-host-form-drawer ref="drawer"
                              @added="reload"
                              @updated="reload" />
    <!-- 发布包上传模态框 -->
    <release-upload-modal ref="uploadModal" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'monitorHost'
  };
</script>

<script lang="ts" setup>
  import { computed, ref, onBeforeMount } from 'vue';
  import { useAppStore, useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import MonitorHostTable from './components/monitor-host-table.vue';
  import MonitorHostCardList from './components/monitor-host-card-list.vue';
  import MonitorHostFormDrawer from './components/monitor-host-form-drawer.vue';
  import ReleaseUploadModal from './components/release-upload-modal.vue';

  const appStore = useAppStore();

  const renderTable = computed(() => appStore.monitorHostView === 'table');

  const render = ref(false);
  const table = ref();
  const card = ref();
  const drawer = ref();
  const uploadModal = ref();

  // 重新加载
  const reload = () => {
    if (renderTable.value) {
      table.value.reload();
    } else {
      card.value.reload();
    }
  };

  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>
</style>
