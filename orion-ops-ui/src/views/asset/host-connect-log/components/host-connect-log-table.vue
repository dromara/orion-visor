<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <a-query-header :model="formModel"
                    label-align="left"
                    @submit="fetchTableData"
                    @reset="reset"
                    @keyup.enter="() => fetchTableData()">
      <!-- 用户 -->
      <a-form-item field="userId" label="用户" label-col-flex="50px">
        <user-selector v-model="formModel.userId"
                       placeholder="请选择用户"
                       allow-clear />
      </a-form-item>
      <!-- 主机 -->
      <a-form-item field="hostId" label="主机" label-col-flex="50px">
        <a-input-number v-model="formModel.hostId"
                        placeholder="FIXME 请输入主机"
                        allow-clear
                        hide-button />
      </a-form-item>
      <!-- 主机地址 -->
      <a-form-item field="hostAddress" label="主机地址" label-col-flex="50px">
        <a-input v-model="formModel.hostAddress" placeholder="请输入主机地址" allow-clear />
      </a-form-item>
      <!-- 状态 -->
      <a-form-item field="status" label="状态" label-col-flex="50px">
        <a-select v-model="formModel.status"
                  placeholder="请选择状态"
                  :options="toOptions(connectStatusKey)"
                  allow-clear />
      </a-form-item>
      <!-- token -->
      <a-form-item field="token" label="token" label-col-flex="50px">
        <a-input v-model="formModel.token" placeholder="请输入token" allow-clear />
      </a-form-item>
      <!-- 开始时间 -->
      <a-form-item field="startTime" label="开始时间" label-col-flex="50px">
        <a-range-picker v-model="timeRange"
                        :time-picker-props="{ defaultValue: ['00:00:00', '23:59:59'] }"
                        show-time
                        format="YYYY-MM-DD HH:mm:ss"
                        @ok="timeRangePicked" />
      </a-form-item>
    </a-query-header>
  </a-card>
  <!-- 表格 -->
  <a-card class="general-card table-card">
    <template #title>
      <!-- 左侧操作 -->
      <div class="table-left-bar-handle">
        <!-- 标题 -->
        <div class="table-title">
          主机连接日志 - 用户
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle" />
    </template>
    <!-- table -->
    <a-table row-key="id"
             class="table-wrapper-8"
             ref="tableRef"
             label-align="left"
             :loading="loading"
             :columns="columns"
             :data="tableRenderData"
             :pagination="pagination"
             @page-change="(page) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size) => fetchTableData(1, size)"
             :bordered="false">
      <!-- 主机地址 -->
      <template #hostAddress="{ record }">
        <span class="copy-left" title="复制" @click="copy(record.hostAddress)">
          <icon-copy />
        </span>
        <span>{{ record.hostAddress }}</span>
      </template>
      <!-- 状态 -->
      <template #status="{ record }">
        <span class="circle" :style="{
           background: getDictValue(connectStatusKey, record.status, 'color')
         }" />
        {{ getDictValue(connectStatusKey, record.status) }}
      </template>
    </a-table>
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'asset-host-connect-log-table'
  };
</script>

<script lang="ts" setup>
  import type { HostConnectLogQueryRequest, HostConnectLogQueryResponse } from '@/api/asset/host-connect-log';
  import { reactive, ref, onMounted } from 'vue';
  import { getHostConnectLogPage } from '@/api/asset/host-connect-log';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { connectStatusKey } from '../types/const';
  import { usePagination } from '@/types/table';
  import { useDictStore } from '@/store';
  import useCopy from '@/hooks/copy';
  import UserSelector from '@/components/user/user/user-selector.vue';

  const emits = defineEmits(['openAdd', 'openUpdate']);

  const tableRenderData = ref<HostConnectLogQueryResponse[]>([]);

  const pagination = usePagination();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();
  const { copy } = useCopy();

  const timeRange = ref<string[]>([]);
  const formModel = reactive<HostConnectLogQueryRequest>({
    userId: undefined,
    hostId: undefined,
    hostAddress: undefined,
    type: undefined,
    token: undefined,
    status: undefined,
    startTimeStart: undefined,
    startTimeEnd: undefined,
  });

  // 选择时间
  const timeRangePicked = (e: string[]) => {
    formModel.startTimeStart = e[0];
    formModel.startTimeEnd = e[1];
  };

  // 加载数据
  const doFetchTableData = async (request: HostConnectLogQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getHostConnectLogPage(request);
      tableRenderData.value = data.rows;
      pagination.total = data.total;
      pagination.current = request.page;
      pagination.pageSize = request.limit;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 切换页码
  const fetchTableData = (page = 1, limit = pagination.pageSize, form = formModel) => {
    doFetchTableData({ page, limit, ...form });
  };

  // 重置
  const reset = () => {
    timeRange.value = [];
    formModel.startTimeStart = undefined;
    formModel.startTimeEnd = undefined;
    fetchTableData();
  };

  onMounted(() => {
    fetchTableData();
  });

</script>

<style lang="less" scoped>

</style>
