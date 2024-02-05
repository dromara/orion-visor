<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  :itemOptions="{ 6: { span: 2 } }"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- 连接用户 -->
      <a-form-item field="userId" label="连接用户" label-col-flex="50px">
        <user-selector v-model="formModel.userId"
                       placeholder="请选择用户"
                       allow-clear />
      </a-form-item>
      <!-- 连接主机 -->
      <a-form-item field="hostId" label="连接主机" label-col-flex="50px">
        <host-selector v-model="formModel.hostId"
                       placeholder="请选择主机"
                       allow-clear />
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
      <!-- 类型 -->
      <a-form-item field="type" label="类型" label-col-flex="50px">
        <a-select v-model="formModel.type"
                  placeholder="请选择类型"
                  :options="toOptions(connectTypeKey)"
                  allow-clear />
      </a-form-item>
      <!-- token -->
      <a-form-item field="token" label="token" label-col-flex="50px">
        <a-input v-model="formModel.token" placeholder="请输入token" allow-clear />
      </a-form-item>
      <!-- 开始时间 -->
      <a-form-item field="startTimeRange" label="开始时间" label-col-flex="50px">
        <a-range-picker v-model="formModel.startTimeRange"
                        style="width: 100%"
                        :time-picker-props="{ defaultValue: ['00:00:00', '23:59:59'] }"
                        show-time
                        format="YYYY-MM-DD HH:mm:ss" />
      </a-form-item>
    </query-header>
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
      <!-- 连接用户 -->
      <template #username="{ record }">
        {{ record.userId }} - {{ record.username }}
      </template>
      <!-- 连接主机 -->
      <template #hostName="{ record }">
        {{ record.hostId }} - {{ record.hostName }}
      </template>
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
    name: 'hostAuditConnectLogTable'
  };
</script>

<script lang="ts" setup>
  import type { HostConnectLogQueryRequest, HostConnectLogQueryResponse } from '@/api/asset/host-connect-log';
  import { reactive, ref, onMounted } from 'vue';
  import { getHostConnectLogPage } from '@/api/asset/host-connect-log';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { connectStatusKey, connectTypeKey } from '../types/const';
  import { usePagination } from '@/types/table';
  import { useDictStore } from '@/store';
  import useCopy from '@/hooks/copy';
  import UserSelector from '@/components/user/user/user-selector.vue';
  import HostSelector from '@/components/asset/host/host-selector.vue';

  const emits = defineEmits(['openAdd', 'openUpdate']);

  const tableRenderData = ref<HostConnectLogQueryResponse[]>([]);

  const pagination = usePagination();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();
  const { copy } = useCopy();

  const formModel = reactive<HostConnectLogQueryRequest>({
    userId: undefined,
    hostId: undefined,
    hostAddress: undefined,
    type: undefined,
    token: undefined,
    status: undefined,
    startTimeRange: undefined,
  });

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

  onMounted(() => {
    fetchTableData();
  });

</script>

<style lang="less" scoped>

</style>
