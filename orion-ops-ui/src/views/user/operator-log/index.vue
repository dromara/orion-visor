<template>
  <div class="layout-container">
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
    <json-editor-modal ref="view" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'userOperatorLog'
  };
</script>

<script lang="ts" setup>
  import { ref, onUnmounted } from 'vue';
  import { useCacheStore } from '@/store';
  import OperatorLogQueryHeader from './components/operator-log-query-header.vue';
  import OperatorLogTable from './components/operator-log-table.vue';
  import JsonEditorModal from '@/components/view/json-editor/json-editor-modal.vue';

  const cacheStore = useCacheStore();

  const table = ref();
  const view = ref();

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.reset('users');
  });

</script>

<style lang="less" scoped>
</style>
