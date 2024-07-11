<template>
  <div class="main-container" v-if="render">
    <!-- 查询头 -->
    <a-card class="general-card table-search-card">
      <!-- 查询头组件 -->
      <operator-log-query-header :model="formModel"
                                 :visible-user="false"
                                 @submit="() => table.fetchTableData()" />
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
      <operator-log-simple-table ref="table"
                                 :current="!user"
                                 :base-params="{ userId: user?.id }"
                                 :model="formModel"/>
    </a-card>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'userOperatorLog'
  };
</script>

<script lang="ts" setup>
  import type { UserQueryResponse } from '@/api/user/user';
  import { ref, reactive, onBeforeMount } from 'vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { dictKeys } from '@/views/user/operator-log/types/const';
  import OperatorLogQueryHeader from '@/views/user/operator-log/components/operator-log-query-header.vue';
  import OperatorLogSimpleTable from '@/views/user/operator-log/components/operator-log-simple-table.vue';
  import { OperatorLogQueryRequest } from '@/api/user/operator-log';

  const props = defineProps<{
    user?: UserQueryResponse;
  }>();

  const cacheStore = useCacheStore();

  const render = ref();
  const table = ref();
  const formModel = reactive<OperatorLogQueryRequest>({
    module: undefined,
    type: undefined,
    riskLevel: undefined,
    result: undefined,
    startTimeRange: undefined,
  });

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
