<template>
  <div class="main-container" v-if="render">
    <!-- 查询头 -->
    <a-card class="general-card table-search-card">
      <!-- 查询头组件 -->
      <operator-log-query-header :visible-user="false"
                                 @submit="(e) => table.fetchTableData(undefined, undefined, e)" />
    </a-card>
    <!-- 表格 -->
    <a-card class="general-card table-card">
      <template #title>
        <!-- 左侧操作 -->
        <div class="table-left-bar-handle">
          <!-- 标题 -->
          <div class="table-title">
            操作日志 <span class="user-info" v-if="user">{{ user.nickname }}({{ user.username }})</span>
          </div>
        </div>
      </template>
      <!-- 表格组件 -->
      <operator-log-table ref="table"
                          :visible-user="false"
                          :current="!user"
                          :base-params="{userId: user?.id}"
                          @viewDetail="(e) => view.open(e)" />
    </a-card>
    <!-- json 查看器模态框 -->
    <json-editor-modal ref="view" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'operatorLogList'
  };
</script>

<script lang="ts" setup>
  import type { UserQueryResponse } from '@/api/user/user';
  import type { PropType } from 'vue';
  import { ref, onBeforeMount } from 'vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { dictKeys } from '../../operator-log/types/const';
  import OperatorLogQueryHeader from '../../operator-log/components/operator-log-query-header.vue';
  import OperatorLogTable from '../../operator-log/components/operator-log-table.vue';
  import JsonEditorModal from '@/components/view/json-editor/json-editor-modal.vue';

  const props = defineProps({
    user: Object as PropType<UserQueryResponse>,
  });

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

</script>

<style lang="less" scoped>
  .main-container {
    padding-left: 16px;

    .table-search-card {
      padding-top: 8px;
      margin-bottom: 0;
    }

    .table-card {
      :deep(.arco-card-body) {
        padding-bottom: 8px;
      }
    }

    .user-info {
      display: inline-block;
      margin-left: 6px;
      color: rgb(var(--primary-6));
    }
  }
</style>
