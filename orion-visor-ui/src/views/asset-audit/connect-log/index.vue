<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <connect-log-table ref="table"
                       @open-clear="(s) => clearModal.open(s)"
                       @open-detail="(s) => detailModal.open(s)" />
    <!-- 清空模态框 -->
    <connect-log-clear-modal ref="clearModal"
                             @clear="() => table.reload()" />
    <!-- 详情模态框 -->
    <connect-log-detail-drawer ref="detailModal" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'connectLog'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import ConnectLogTable from './components/connect-log-table.vue';
  import ConnectLogClearModal from './components/connect-log-clear-modal.vue';
  import ConnectLogDetailDrawer from './components/connect-log-detail-drawer.vue';

  const render = ref(false);
  const table = ref();
  const clearModal = ref();
  const detailModal = ref();

  // 加载字典配置
  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
