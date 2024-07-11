<template>
  <div class="layout-container" v-if="render">
    <!-- 表格 -->
    <operator-log-table ref="table"
                        @open-detail="openLogDetail"
                        @open-clear="(s) => clearModal.open(s)" />
    <!-- 清理模态框 -->
    <operator-log-clear-modal ref="clearModal"
                              @clear="() => table.fetchTableData()" />
    <!-- json 查看器模态框 -->
    <json-editor-modal ref="jsonView" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'operatorLog'
  };
</script>

<script lang="ts" setup>
  import type { OperatorLogQueryResponse } from '@/api/user/operator-log';
  import { ref, onUnmounted, onBeforeMount } from 'vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { dictKeys, getLogDetail } from './types/const';
  import OperatorLogTable from './components/operator-log-table.vue';
  import OperatorLogClearModal from './components/operator-log-clear-modal.vue';
  import JsonEditorModal from '@/components/view/json-editor/modal/index.vue';

  const cacheStore = useCacheStore();

  const render = ref(false);
  const table = ref();
  const clearModal = ref();
  const jsonView = ref();

  // 查看详情
  const openLogDetail = (record: OperatorLogQueryResponse) => {
    jsonView.value.open(getLogDetail(record));
  };

  // 加载字典值
  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.reset('users');
  });

</script>

<style lang="less" scoped>
</style>
