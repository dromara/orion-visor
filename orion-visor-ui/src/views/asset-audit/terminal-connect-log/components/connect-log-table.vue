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
      <!-- 状态 -->
      <a-form-item field="status" label="状态">
        <a-select v-model="formModel.status"
                  placeholder="请选择状态"
                  :options="toOptions(connectStatusKey)"
                  allow-clear />
      </a-form-item>
      <!-- 主机地址 -->
      <a-form-item field="hostAddress" label="主机地址">
        <a-input v-model="formModel.hostAddress" placeholder="请输入主机地址" allow-clear />
      </a-form-item>
      <!-- id -->
      <a-form-item field="id" label="id">
        <a-input-number v-model="formModel.id" placeholder="请输入日志id" allow-clear />
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
          终端连接日志
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 清理 -->
          <a-button v-permission="['terminal:terminal-connect-log:management:clear']"
                    status="danger"
                    @click="openClear">
            清理
            <template #icon>
              <icon-close />
            </template>
          </a-button>
          <!-- 删除 -->
          <a-popconfirm :content="`确认删除选中的 ${selectedKeys.length} 条记录吗?`"
                        position="br"
                        type="warning"
                        @ok="deleteSelectedRows">
            <a-button v-permission="['terminal:terminal-connect-log:management:delete']"
                      type="primary"
                      status="danger"
                      :disabled="selectedKeys.length === 0">
              删除
              <template #icon>
                <icon-delete />
              </template>
            </a-button>
          </a-popconfirm>
          <!-- 调整 -->
          <table-adjust :columns="logColumns"
                        :columns-hook="columnsHook"
                        :query-order="queryOrder"
                        @query="fetchTableData" />
        </a-space>
      </div>
    </template>
    <!-- table -->
    <a-table v-model:selected-keys="selectedKeys"
             row-key="id"
             ref="tableRef"
             class="table-resize"
             :loading="loading"
             :row-selection="rowSelection"
             :columns="tableColumns"
             :data="tableRenderData"
             :pagination="pagination"
             :bordered="false"
             :column-resizable="true"
             @page-change="(page: number) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size: number) => fetchTableData(1, size)">
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
      <!-- 状态 -->
      <template #status="{ record }">
        <span class="circle" :style="{
           background: getDictValue(connectStatusKey, record.status, 'color')
        }" />
        {{ getDictValue(connectStatusKey, record.status) }}
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
      <!-- 连接时间 -->
      <template #connectTime="{ record }">
        <div>
          从: {{ record.startTime && dateFormat(new Date(record.startTime)) }}
        </div>
        <div class="mt4">
          至: {{ (record.endTime && dateFormat(new Date(record.endTime)) || '现在') }}
        </div>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 连接 -->
          <a-button v-permission="['terminal:terminal:access']"
                    type="text"
                    size="mini"
                    @click="openNewRoute({ name: 'terminal', query: { connect: record.hostId, type: record.type } })">
            连接
          </a-button>
          <!-- 下线 -->
          <a-popconfirm v-if="record.status === HostConnectStatus.CONNECTING"
                        content="确认要强制下线吗?"
                        position="left"
                        type="warning"
                        @ok="forceOffline(record)">
            <a-button v-permission="['terminal:terminal-connect-log:management:force-offline', 'terminal:terminal-connect-session:management:force-offline']"
                      type="text"
                      size="mini"
                      status="danger">
              下线
            </a-button>
          </a-popconfirm>
          <!-- 详情 -->
          <a-button type="text"
                    size="mini"
                    @click="emits('openDetail', record)">
            详情
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['terminal:terminal-connect-log:management:delete']"
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
</template>

<script lang="ts">
  export default {
    name: 'connectLogTable'
  };
</script>

<script lang="ts" setup>
  import type { TerminalConnectLogQueryRequest, TerminalConnectLogQueryResponse } from '@/api/terminal/terminal-connect-log';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteTerminalConnectLog, getTerminalConnectLogPage, hostForceOffline } from '@/api/terminal/terminal-connect-log';
  import { TableName, connectStatusKey, connectTypeKey, HostConnectStatus } from '../types/const';
  import { useTablePagination, useRowSelection, useTableColumns } from '@/hooks/table';
  import { useDictStore, useUserStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import { logColumns } from '../types/table.columns';
  import useLoading from '@/hooks/loading';
  import { copy } from '@/hooks/copy';
  import { useRoute } from 'vue-router';
  import { dateFormat } from '@/utils';
  import { openNewRoute } from '@/router';
  import { useQueryOrder, DESC } from '@/hooks/query-order';
  import UserSelector from '@/components/user/user/selector/index.vue';
  import HostSelector from '@/components/asset/host/selector/index.vue';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['openClear', 'openDetail']);

  const route = useRoute();
  const rowSelection = useRowSelection();
  const pagination = useTablePagination();
  const queryOrder = useQueryOrder(TableName, DESC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, logColumns);
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();

  const tableRenderData = ref<Array<TerminalConnectLogQueryResponse>>([]);
  const selectedKeys = ref<Array<number>>([]);

  const formModel = reactive<TerminalConnectLogQueryRequest>({
    id: undefined,
    userId: undefined,
    hostId: undefined,
    hostAddress: undefined,
    type: undefined,
    status: undefined,
    startTimeRange: undefined,
  });

  // 删除选中行
  const deleteSelectedRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteTerminalConnectLog(selectedKeys.value);
      Message.success(`成功删除 ${selectedKeys.value.length} 条数据`);
      selectedKeys.value = [];
      // 重新加载
      reload();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 删除当前行
  const deleteRow = async (record: TerminalConnectLogQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteTerminalConnectLog([record.id]);
      Message.success('删除成功');
      selectedKeys.value = [];
      // 重新加载
      reload();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 重新加载
  const reload = () => {
    // 重新加载数据
    fetchTableData();
  };

  defineExpose({ reload });

  // 加载数据
  const doFetchTableData = async (request: TerminalConnectLogQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getTerminalConnectLogPage(queryOrder.markOrderly(request));
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
    emits('openClear', { ...formModel, id: undefined });
  };

  // 强制下线
  const forceOffline = async (record: TerminalConnectLogQueryResponse) => {
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

  onMounted(() => {
    // 当前用户
    const action = route.query.action as string;
    if (action === 'self') {
      formModel.userId = useUserStore().id;
    }
    // id
    const id = route.query.id as string;
    if (id) {
      formModel.id = Number.parseInt(id);
    }
    fetchTableData();
  });

</script>

<style lang="less" scoped>
</style>
