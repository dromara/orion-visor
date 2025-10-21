<template>
  <a-col :span="8">
    <div class="card full">
      <div class="card-title">
        <p class="card-title-left">用户登录日志</p>
        <!-- 跳转 -->
        <span class="pointer span-blue"
              title="详情"
              @click="router.push({ name: 'userInfo', query: { tab: 'loginHistory' } })">
          详情
        </span>
      </div>
      <div class="card-body">
        <a-table row-key="id"
                 class="table-resize"
                 :loading="loading"
                 :columns="userLoginColumns"
                 :data="data.infra?.loginHistoryList || []"
                 :pagination="false"
                 :bordered="false"
                 :column-resizable="true"
                 :scroll="{ y: 388 }">
          <!-- 登录设备 -->
          <template #content="{ record }">
            <span>{{ record.address }} - {{ record.location }} - {{ record.userAgent }}</span>
          </template>
          <!-- 登录结果 -->
          <template #result="{ record }">
            <a-tag :color="getDictValue(operatorLogResultKey, record.result, 'color')">
              {{ getDictValue(operatorLogResultKey, record.result) }}
            </a-tag>
          </template>
        </a-table>
      </div>
    </div>
  </a-col>
</template>

<script lang="ts">
  export default {
    name: 'userLoginTable'
  };
</script>

<script lang="ts" setup>
  import type { WorkplaceStatisticsData } from '@/views/dashboard/workplace/types/const';
  import { userLoginColumns } from '../types/table.columns';
  import { operatorLogResultKey } from '../types/const';
  import { useRouter } from 'vue-router';
  import { useDictStore } from '@/store';

  const props = defineProps<{
    loading: boolean;
    data: WorkplaceStatisticsData;
  }>();

  const router = useRouter();
  const { getDictValue } = useDictStore();

</script>

<style lang="less" scoped>

</style>
