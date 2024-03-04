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
          主机连接日志
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 清空 -->
          <a-button v-permission="['asset:host-connect-log:management:clear']"
                    status="danger"
                    @click="openClear">
            清空
            <template #icon>
              <icon-close />
            </template>
          </a-button>
          <!-- 删除 -->
          <a-popconfirm :content="`确认删除选中的 ${selectedKeys.length} 条记录吗?`"
                        position="br"
                        type="warning"
                        @ok="deleteSelectRows">
            <a-button v-permission="['asset:host-connect-log:management:delete']"
                      type="secondary"
                      status="danger"
                      :disabled="selectedKeys.length === 0">
              删除
              <template #icon>
                <icon-delete />
              </template>
            </a-button>
          </a-popconfirm>
        </a-space>
      </div>
    </template>
    <!-- table -->
    <a-table row-key="id"
             class="table-wrapper-8"
             ref="tableRef"
             :loading="loading"
             v-model:selected-keys="selectedKeys"
             :row-selection="rowSelection"
             :columns="columns"
             :data="tableRenderData"
             :pagination="pagination"
             @page-change="(page) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size) => fetchTableData(1, size)"
             :bordered="false">
      <!-- 连接地址 -->
      <template #address="{ record }">
        <span class="connect-location" :title="record.extra?.location">
          {{ record.extra?.location }}
        </span>
        <br>
        <span class="copy-left" title="复制" @click="copy(record.extra?.address)">
          <icon-copy />
        </span>
        <span class="connect-address" :title="record.extra?.address">
          {{ record.extra?.address }}
        </span>
      </template>
      <!-- 连接用户 -->
      <template #username="{ record }">
        {{ record.username }}
      </template>
      <!-- 连接主机 -->
      <template #hostName="{ record }">
        <span class="host-name" :title="record.hostName">
          {{ record.hostName }}
        </span>
        <br>
        <span class="copy-left" title="复制" @click="copy(record.hostAddress)">
          <icon-copy />
        </span>
        <span class="host-address" :title="record.hostAddress">
          {{ record.hostAddress }}
        </span>
      </template>
      <!-- 状态 -->
      <template #status="{ record }">
        <span class="circle" :style="{
           background: getDictValue(connectStatusKey, record.status, 'color')
        }" />
        {{ getDictValue(connectStatusKey, record.status) }}
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 详情 -->
          <a-button type="text"
                    size="mini"
                    @click="openDetail(record)">
            详情
          </a-button>
          <!-- 下线 -->
          <a-popconfirm v-if="record.status === HostConnectStatus.CONNECTING"
                        content="确认要强制下线吗?"
                        position="left"
                        type="warning"
                        @ok="forceOffline(record)">
            <a-button v-permission="['asset:host-connect-log:management:force-offline']"
                      type="text"
                      size="mini"
                      status="danger">
              下线
            </a-button>
          </a-popconfirm>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['asset:host-connect-log:management:delete']"
                      type="text"
                      size="mini"
                      status="danger">
              删除
            </a-button>
          </a-popconfirm>
        </div>
      </template>
    </a-table>
  </a-card>
  <!-- 清空模态框 -->
  <connect-log-clear-modal ref="clearModal"
                           @clear="fetchTableData" />
  <!-- 详情模态框 -->
  <connect-log-detail-drawer ref="detailModal" />
</template>

<script lang="ts">
  export default {
    name: 'hostAuditConnectLogTable'
  };
</script>

<script lang="ts" setup>
  import type { HostConnectLogQueryRequest, HostConnectLogQueryResponse } from '@/api/asset/host-connect-log';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteHostConnectLog, getHostConnectLogPage, hostForceOffline } from '@/api/asset/host-connect-log';
  import { connectStatusKey, connectTypeKey, HostConnectStatus } from '../types/const';
  import { usePagination, useRowSelection } from '@/types/table';
  import { useDictStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import columns from '../types/table.columns';
  import useLoading from '@/hooks/loading';
  import useCopy from '@/hooks/copy';
  import UserSelector from '@/components/user/user/user-selector.vue';
  import HostSelector from '@/components/asset/host/host-selector.vue';
  import ConnectLogClearModal from './connect-log-clear-modal.vue';
  import ConnectLogDetailDrawer from './connect-log-detail-drawer.vue';

  const emits = defineEmits(['openAdd', 'openUpdate']);

  const tableRenderData = ref<HostConnectLogQueryResponse[]>([]);
  const selectedKeys = ref<number[]>([]);
  const clearModal = ref();
  const detailModal = ref();

  const pagination = usePagination();
  const rowSelection = useRowSelection();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();
  const { copy } = useCopy();

  const formModel = reactive<HostConnectLogQueryRequest>({
    userId: undefined,
    hostId: undefined,
    hostAddress: undefined,
    type: undefined,
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
      selectedKeys.value = [];
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 切换页码
  const fetchTableData = (page = 1, limit = pagination.pageSize, form = formModel) => {
    doFetchTableData({ page, limit, ...form });
  };

  // 打开清空
  const openClear = () => {
    clearModal.value?.open({ ...formModel });
  };

  // 打开详情
  const openDetail = (record: HostConnectLogQueryResponse) => {
    detailModal.value?.open(record);
  };

  // 强制下线
  const forceOffline = async (record: HostConnectLogQueryResponse) => {
    try {
      setLoading(true);
      await hostForceOffline({ id: record.id });
      record.status = HostConnectStatus.FORCE_OFFLINE;
      record.endTime = Date.now();
      Message.success('已下线');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 删除选中行
  const deleteSelectRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteHostConnectLog(selectedKeys.value);
      Message.success(`成功删除 ${selectedKeys.value.length} 条数据`);
      selectedKeys.value = [];
      // 重新加载数据
      fetchTableData();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 删除当前行
  const deleteRow = async ({ id }: {
    id: number
  }) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteHostConnectLog([id]);
      Message.success('删除成功');
      selectedKeys.value = [];
      // 重新加载数据
      fetchTableData();
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
  .host-name, .connect-location {
    color: var(--color-text-2);
  }

  .host-address, .connect-address {
    margin-top: 4px;
    display: inline-block;
    color: var(--color-text-3);
  }
</style>
