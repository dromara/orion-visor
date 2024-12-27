<template>
  <a-col :span="12">
    <div class="card full">
      <div class="card-title">
        <p class="card-title-left">最近终端连接记录</p>
        <!-- 跳转 -->
        <span class="pointer span-blue"
              title="详情"
              @click="$router.push({ name: 'connectLog', query: { action: 'self' } })">
          详情
        </span>
      </div>
      <div class="card-body">
        <a-table row-key="id"
                 :loading="loading"
                 :columns="terminalLogColumns"
                 :data="data.asset?.terminalConnectList || []"
                 :pagination="false"
                 :bordered="false"
                 :scroll="{ y: 258 }">
          <!-- 连接主机 -->
          <template #hostName="{ record }">
            <span class="table-cell-value" :title="record.hostName">
              {{ record.hostName }}
            </span>
            <br>
            <span class="table-cell-sub-value text-copy"
                  :title="record.hostAddress"
                  @click="copy(record.hostAddress)">
              {{ record.hostAddress }}
            </span>
          </template>
          <!-- 类型 -->
          <template #type="{ record }">
            <a-tag :color="getDictValue(terminalConnectTypeKey, record.type, 'color')">
              {{ getDictValue(terminalConnectTypeKey, record.type) }}
            </a-tag>
          </template>
          <!-- 操作 -->
          <template #handle="{ record }">
            <div class="table-handle-wrapper">
              <!-- 连接 SSH -->
              <a-button v-permission="['asset:terminal:access']"
                        type="text"
                        size="mini"
                        @click="openNewRoute({ name: 'terminal', query: { connect: record.hostId, type: record.type } })">
                连接
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
    name: 'terminalConnectTable'
  };
</script>

<script lang="ts" setup>
  import type { WorkplaceStatisticsData } from '../types/const';
  import { copy } from '@/hooks/copy';
  import { terminalLogColumns } from '../types/table.columns';
  import { terminalConnectTypeKey } from '../types/const';
  import { useDictStore } from '@/store';
  import { openNewRoute } from '@/router';

  const props = defineProps<{
    loading: boolean;
    data: WorkplaceStatisticsData;
  }>();

  const { getDictValue } = useDictStore();

</script>

<style lang="less" scoped>

</style>
