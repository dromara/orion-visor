<template>
  <div class="layout-container" v-if="render">
    <!-- 查询头 -->
    <a-card class="general-card table-search-card">
      <!-- 查询头组件 -->
      <operator-log-query-header @submit="(e) => table.fetchTableData(undefined, undefined, e)" />
    </a-card>
    <!-- 表格 -->
    <a-card class="general-card table-card">
      <template #title>
        <!-- 左侧操作 -->
        <div class="table-left-bar-handle">
          <!-- 标题 -->
          <div class="table-title">
            操作日志
          </div>
        </div>
      </template>
      <!-- 表格组件 -->
      <operator-log-table ref="table" @viewDetail="(e) => view.open(e)" />
    </a-card>
    <!-- json 查看器模态框 -->
    <json-view-modal ref="view" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'userOperatorLog'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount, onUnmounted } from 'vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import OperatorLogQueryHeader from './components/operator-log-query-header.vue';
  import OperatorLogTable from './components/operator-log-table.vue';
  import JsonViewModal from '@/components/view/json/json-view-modal.vue';

  const cacheStore = useCacheStore();

  const render = ref();
  const table = ref();
  const view = ref();

  onBeforeMount(async () => {
    // 加载字典值
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
