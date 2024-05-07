<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  :itemOptions="{ 4: { span: 2 } }"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- 连接用户 -->
      <a-form-item field="userId" label="连接用户">
        <user-selector v-model="formModel.userId"
                       placeholder="请选择用户"
                       allow-clear />
      </a-form-item>
      <!-- 连接主机 -->
      <a-form-item field="hostId" label="连接主机">
        <host-selector v-model="formModel.hostId"
                       placeholder="请选择主机"
                       allow-clear />
      </a-form-item>
      <!-- 主机地址 -->
      <a-form-item field="hostAddress" label="主机地址">
        <a-input v-model="formModel.hostAddress" placeholder="请输入主机地址" allow-clear />
      </a-form-item>
      <!-- 类型 -->
      <a-form-item field="type" label="类型">
        <a-select v-model="formModel.type"
                  placeholder="请选择类型"
                  :options="toOptions(connectTypeKey)"
                  allow-clear />
      </a-form-item>
      <!-- 开始时间 -->
      <a-form-item field="startTimeRange" label="开始时间">
        <a-range-picker v-model="formModel.startTimeRange"
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
          主机在线会话
        </div>
      </div>
    </template>
    <!-- table -->
    <a-table row-key="id"
             ref="tableRef"
             :loading="loading"
             :columns="columns"
             :data="tableRenderData"
             :pagination="false"
             :bordered="false">
      <!-- 连接用户 -->
      <template #username="{ record }">
        {{ record.username }}
      </template>
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
        <a-tag :color="getDictValue(connectTypeKey, record.type, 'color')">
          {{ getDictValue(connectTypeKey, record.type) }}
        </a-tag>
      </template>
      <!-- 留痕地址 -->
      <template #address="{ record }">
        <span class="table-cell-value" :title="record.extra?.location">
          {{ record.extra?.location }}
        </span>
        <br>
        <span class="table-cell-sub-value text-copy"
              :title="record.extra?.address"
              @click="copy(record.extra?.address)">
          {{ record.extra?.address }}
        </span>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 下线 -->
          <a-popconfirm content="确认要强制下线吗?"
                        position="left"
                        type="warning"
                        @ok="forceOffline(record)">
            <a-button v-permission="['asset:host-connect-log:management:force-offline', 'asset:host-connect-session:management:force-offline']"
                      type="text"
                      size="mini"
                      status="danger">
              下线
            </a-button>
          </a-popconfirm>
        </div>
      </template>
    </a-table>
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'connectSessionTable'
  };
</script>

<script lang="ts" setup>
  import type { HostConnectLogQueryRequest, HostConnectLogQueryResponse } from '@/api/asset/host-connect-log';
  import { reactive, ref, onMounted } from 'vue';
  import { getHostConnectSessions, hostForceOffline } from '@/api/asset/host-connect-log';
  import { connectTypeKey } from '../types/const';
  import { useDictStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import columns from '../types/table.columns';
  import useLoading from '@/hooks/loading';
  import { copy } from '@/hooks/copy';
  import UserSelector from '@/components/user/user/selector/index.vue';
  import HostSelector from '@/components/asset/host/selector/index.vue';

  const tableRenderData = ref<HostConnectLogQueryResponse[]>([]);

  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();

  const formModel = reactive<HostConnectLogQueryRequest>({
    userId: undefined,
    hostId: undefined,
    hostAddress: undefined,
    type: undefined,
    startTimeRange: undefined,
  });

  // 加载数据
  const fetchTableData = async () => {
    try {
      setLoading(true);
      const { data } = await getHostConnectSessions(formModel);
      tableRenderData.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 强制下线
  const forceOffline = async (record: HostConnectLogQueryResponse) => {
    try {
      setLoading(true);
      await hostForceOffline({ id: record.id });
      record.endTime = Date.now();
      Message.success('已下线');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  onMounted(() => {
    fetchTableData();
  });

</script>

<style lang="less" scoped>
</style>
