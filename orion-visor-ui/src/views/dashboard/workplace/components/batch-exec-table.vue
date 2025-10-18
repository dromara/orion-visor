<template>
  <a-col :span="8">
    <div class="card full">
      <div class="card-title">
        <p class="card-title-left">最近批量执行记录</p>
        <!-- 跳转 -->
        <span class="pointer span-blue"
              title="详情"
              @click="router.push({ name: 'execCommandLog', query: { action: 'self' } })">
          详情
        </span>
      </div>
      <div class="card-body">
        <!-- 表格 -->
        <a-table row-key="id"
                 class="table-resize"
                 :loading="loading"
                 :columns="batchExecColumns"
                 :data="data.exec?.execLogList || []"
                 :pagination="false"
                 :bordered="false"
                 :column-resizable="true"
                 :scroll="{ y: 258 }">
          <!-- 空状态 -->
          <template #empty>
            <a-empty style="margin-top: 42px;" description="暂无批量执行记录" />
          </template>
          <!-- 执行状态 -->
          <template #status="{ record }">
            <a-tag :color="getDictValue(execHostStatusKey, record.status, 'color')">
              {{ getDictValue(execHostStatusKey, record.status) }}
            </a-tag>
          </template>
          <!-- 操作 -->
          <template #handle="{ record }">
            <div class="table-handle-wrapper">
              <!-- 日志 -->
              <a-button v-permission="['exec:exec-command:exec']"
                        type="text"
                        size="mini"
                        @click="router.push({ name: 'execCommand', query: { id: record.id } })">
                日志
              </a-button>
            </div>
          </template>
        </a-table>
      </div>
    </div>
  </a-col>
</template>

<script lang="ts">
  export default {
    name: 'batchExecTable'
  };
</script>

<script lang="ts" setup>
  import type { WorkplaceStatisticsData } from '../types/const';
  import { batchExecColumns } from '../types/table.columns';
  import { useDictStore } from '@/store';
  import { useRouter } from 'vue-router';
  import { execHostStatusKey } from '@/components/exec/log/const';

  const props = defineProps<{
    loading: boolean;
    data: WorkplaceStatisticsData;
  }>();

  const router = useRouter();
  const { getDictValue } = useDictStore();

</script>

<style lang="less" scoped>

</style>
