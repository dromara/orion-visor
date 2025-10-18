<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  :itemOptions="{ 5: { span: 2 } }"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- 执行描述 -->
      <a-form-item field="description" label="执行描述">
        <a-input v-model="formModel.description"
                 placeholder="请输入执行描述"
                 allow-clear />
      </a-form-item>
      <!-- 执行状态 -->
      <a-form-item field="status" label="执行状态">
        <a-select v-model="formModel.status"
                  :options="toOptions(execStatusKey)"
                  placeholder="请选择执行状态"
                  allow-clear />
      </a-form-item>
      <!-- 执行用户 -->
      <a-form-item field="userId" label="执行用户">
        <user-selector v-model="formModel.userId"
                       placeholder="请选择执行用户"
                       allow-clear />
      </a-form-item>
      <!-- 执行命令 -->
      <a-form-item field="command" label="执行命令">
        <a-input v-model="formModel.command"
                 placeholder="请输入执行命令"
                 allow-clear />
      </a-form-item>
      <!-- id -->
      <a-form-item field="id" label="id">
        <a-input-number v-model="formModel.id"
                        placeholder="请输入id"
                        allow-clear
                        hide-button />
      </a-form-item>
      <!-- 执行时间 -->
      <a-form-item field="startTimeRange" label="执行时间">
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
          批量执行日志列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 执行命令 -->
          <a-button v-permission="['exec:exec-command:exec']"
                    type="primary"
                    @click="router.push({ name: 'execCommand' })">
            执行命令
            <template #icon>
              <icon-thunderbolt />
            </template>
          </a-button>
          <!-- 清理 -->
          <a-button v-permission="['exec:exec-command-log:management:clear']"
                    status="danger"
                    @click="openClear">
            清理
            <template #icon>
              <icon-close />
            </template>
          </a-button>
          <!-- 删除 -->
          <a-popconfirm :content="`确认删除选中的 ${selectedKeys.length} 条记录吗? 删除后会中断执行!`"
                        position="br"
                        type="warning"
                        @ok="deleteSelectedRows">
            <a-button v-permission="['exec:exec-command-log:delete']"
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
             :columns="tableColumns"
             :row-selection="rowSelection"
             :expandable="expandable"
             :data="tableRenderData"
             :pagination="pagination"
             :bordered="false"
             :column-resizable="true"
             @page-change="(page: number) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size: number) => fetchTableData(1, size)"
             @expand="loadExecHost">
      <!-- 展开表格 -->
      <template #expand-row="{ record }">
        <exec-command-host-log-table :row="record"
                                     @view-command="(s: any) => emits('viewCommand', s)"
                                     @view-params="(s: any) => emits('viewParams', s)"
                                     @refresh-host="refreshExecHost" />
      </template>
      <!-- 执行命令 -->
      <template #command="{ record }">
        <span :title="record.command">
          {{ record.command }}
        </span>
      </template>
      <!-- 执行状态 -->
      <template #status="{ record }">
        <a-tag :color="getDictValue(execStatusKey, record.status, 'color')">
          {{ getDictValue(execStatusKey, record.status) }}
        </a-tag>
      </template>
      <!-- 执行时间 -->
      <template #startTime="{ record }">
        <span class="table-cell-value">
          {{ (record.startTime && dateFormat(new Date(record.startTime))) || '-' }}
        </span>
        <br>
        <span class="table-cell-sub-value usn" style="font-size: 12px;">
          持续时间: {{ formatDuration(record.startTime, record.finishTime) || '-' }}
        </span>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 重新执行 -->
          <a-popconfirm content="确定要重新执行吗?"
                        position="left"
                        type="warning"
                        @ok="doReExecCommand(record)">
            <a-button v-permission="['exec:exec-command:exec']"
                      type="text"
                      size="mini">
              重新执行
            </a-button>
          </a-popconfirm>
          <!-- 命令 -->
          <a-button type="text"
                    size="mini"
                    @click="emits('viewCommand', record.command)">
            命令
          </a-button>
          <!-- 日志 -->
          <a-button v-permission="['exec:exec-command-log:query', 'exec:exec-command:exec']"
                    type="text"
                    size="mini"
                    title="ctrl + 左键新页面打开"
                    @click="(e: any) => emits('viewLog', record.id, e.ctrlKey)">
            日志
          </a-button>
          <!-- 中断 -->
          <a-popconfirm content="确定要中断执行吗?"
                        position="left"
                        type="warning"
                        @ok="doInterruptExecCommand(record)">
            <a-button v-permission="['exec:exec-command-log:interrupt']"
                      type="text"
                      size="mini"
                      status="danger"
                      :disabled="record.status !== ExecStatus.WAITING && record.status !== ExecStatus.RUNNING">
              中断
            </a-button>
          </a-popconfirm>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗, 删除后会中断执行?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['exec:exec-command-log:delete']"
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
    name: 'execCommandLogTable'
  };
</script>

<script lang="ts" setup>
  import type { TableData } from '@arco-design/web-vue';
  import type { ExecLogQueryResponse, ExecLogQueryRequest } from '@/api/exec/exec-log';
  import { reactive, ref, onMounted, onUnmounted } from 'vue';
  import {
    batchDeleteExecCommandLog,
    deleteExecCommandLog,
    getExecCommandHostLogList,
    getExecCommandLogPage,
    getExecCommandLogStatus
  } from '@/api/exec/exec-command-log';
  import { Message } from '@arco-design/web-vue';
  import { useRoute, useRouter } from 'vue-router';
  import useLoading from '@/hooks/loading';
  import { logColumns } from '../types/table.columns';
  import { ExecStatus, execStatusKey } from '@/components/exec/log/const';
  import { useExpandable, useTablePagination, useRowSelection, useTableColumns } from '@/hooks/table';
  import { TableName } from '../types/const';
  import { useDictStore, useUserStore } from '@/store';
  import { dateFormat, formatDuration } from '@/utils';
  import { reExecCommand } from '@/api/exec/exec-command';
  import { interruptExecCommand } from '@/api/exec/exec-command-log';
  import { useQueryOrder, DESC } from '@/hooks/query-order';
  import UserSelector from '@/components/user/user/selector/index.vue';
  import ExecCommandHostLogTable from './exec-command-host-log-table.vue';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['viewCommand', 'viewParams', 'viewLog', 'openClear']);

  const route = useRoute();
  const router = useRouter();
  const expandable = useExpandable();
  const rowSelection = useRowSelection();
  const pagination = useTablePagination();
  const queryOrder = useQueryOrder(TableName, DESC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, logColumns);
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();

  const pullIntervalId = ref();
  const tableRef = ref();
  const selectedKeys = ref<Array<number>>([]);
  const tableRenderData = ref<Array<ExecLogQueryResponse>>([]);
  const formModel = reactive<ExecLogQueryRequest>({
    id: undefined,
    userId: undefined,
    description: undefined,
    command: undefined,
    status: undefined,
    startTimeRange: undefined,
  });

  // 打开清理
  const openClear = () => {
    emits('openClear', { ...formModel, id: undefined });
  };

  // 删除选中行
  const deleteSelectedRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await batchDeleteExecCommandLog(selectedKeys.value);
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
  const deleteRow = async (record: ExecLogQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteExecCommandLog(record.id);
      Message.success('删除成功');
      // 重新加载
      reload();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 重新执行命令
  const doReExecCommand = async (record: ExecLogQueryResponse) => {
    try {
      setLoading(true);
      // 调用中断接口
      await reExecCommand({
        logId: record.id
      });
      Message.success('已重新执行');
      fetchTableData();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 中断执行
  const doInterruptExecCommand = async (record: ExecLogQueryResponse) => {
    try {
      setLoading(true);
      // 调用中断接口
      await interruptExecCommand({
        logId: record.id
      });
      Message.success('已中断');
      record.status = ExecStatus.COMPLETED;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 刷新执行主机
  const refreshExecHost = (id: number) => {
    // 获取到执行主机
    const exec = tableRenderData.value.find(s => s.id === id);
    if (!exec) {
      return;
    }
    // 加载数据
    getExecCommandHostLogList(id).then(s => {
      exec.hosts = s.data;
    });
  };

  // 加载主机数据
  const loadExecHost = async (key: number | string, record: TableData) => {
    if (record.hosts?.length) {
      return;
    }
    // 加载数据
    const { data } = await getExecCommandHostLogList(record.id);
    record.hosts = data;
  };

  // 加载状态
  const pullExecStatus = async () => {
    const unCompleteIdList = tableRenderData.value
      .filter(s => s.status === ExecStatus.WAITING || s.status === ExecStatus.RUNNING)
      .map(s => s.id);
    if (!unCompleteIdList.length) {
      return;
    }
    // 加载未完成的状态
    const { data: { logList, hostList } } = await getExecCommandLogStatus(unCompleteIdList);
    // 设置任务状态
    logList.forEach(s => {
      const tableRow = tableRenderData.value.find(r => r.id === s.id);
      if (!tableRow) {
        return;
      }
      tableRow.status = s.status;
      tableRow.startTime = s.startTime;
      tableRow.finishTime = s.finishTime;
    });
    // 设置主机状态
    hostList.forEach(s => {
      const host = tableRenderData.value
        .find(r => r.id === s.logId)
        ?.hosts
        ?.find(r => r.id === s.id);
      if (!host) {
        return;
      }
      host.status = s.status;
      host.startTime = s.startTime;
      host.finishTime = s.finishTime;
      host.exitCode = s.exitCode;
      host.errorMessage = s.errorMessage;
    });
  };

  // 重新加载
  const reload = () => {
    // 重新加载数据
    fetchTableData();
  };

  defineExpose({ reload });

  // 加载数据
  const doFetchTableData = async (request: ExecLogQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getExecCommandLogPage(queryOrder.markOrderly(request));
      tableRenderData.value = data.rows;
      pagination.total = data.total;
      pagination.current = request.page;
      pagination.pageSize = request.limit;
      selectedKeys.value = [];
      tableRef.value.expandAll(false);
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
    // 当前用户
    const action = route.query.action as string;
    if (action === 'self') {
      formModel.userId = useUserStore().id;
    }
    // 加载数据
    fetchTableData();
    // 注册状态轮询
    pullIntervalId.value = window.setInterval(pullExecStatus, 10000);
  });

  onUnmounted(() => {
    // 卸载状态轮询
    clearInterval(pullIntervalId.value);
  });

</script>

<style lang="less" scoped>
  :deep(.arco-table-cell-fixed-expand) {
    width: 100% !important;
  }

</style>
