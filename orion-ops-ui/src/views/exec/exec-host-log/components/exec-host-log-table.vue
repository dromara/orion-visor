<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- id -->
      <a-form-item field="id" label="id">
        <a-input-number v-model="formModel.id"
                        placeholder="请输入id"
                        allow-clear
                        hide-button />
      </a-form-item>
      <!-- 执行日志id -->
      <a-form-item field="logId" label="执行日志id">
        <a-input-number v-model="formModel.logId"
                        placeholder="请输入执行日志id"
                        allow-clear
                        hide-button />
      </a-form-item>
      <!-- 主机id -->
      <a-form-item field="hostId" label="主机id">
        <a-input-number v-model="formModel.hostId"
                        placeholder="请输入主机id"
                        allow-clear
                        hide-button />
      </a-form-item>
      <!-- 主机名称 -->
      <a-form-item field="hostName" label="主机名称">
        <a-input v-model="formModel.hostName"
                 placeholder="请输入主机名称"
                 allow-clear />
      </a-form-item>
      <!-- 执行状态 -->
      <a-form-item field="status" label="执行状态">
        <a-select v-model="formModel.status"
                  :options="toOptions(execHostStatusKey)"
                  placeholder="请选择执行状态"
                  allow-clear />
      </a-form-item>
      <!-- 执行命令 -->
      <a-form-item field="command" label="执行命令">
        <a-input v-model="formModel.command"
                 placeholder="请输入执行命令"
                 allow-clear />
      </a-form-item>
      <!-- 执行参数 -->
      <a-form-item field="parameter" label="执行参数">
        <a-input v-model="formModel.parameter"
                 placeholder="请输入执行参数"
                 allow-clear />
      </a-form-item>
      <!-- 退出码 -->
      <a-form-item field="exitStatus" label="退出码">
        <a-input-number v-model="formModel.exitStatus"
                        placeholder="请输入退出码"
                        allow-clear
                        hide-button />
      </a-form-item>
      <!-- 日志路径 -->
      <a-form-item field="logPath" label="日志路径">
        <a-input v-model="formModel.logPath"
                 placeholder="请输入日志路径"
                 allow-clear />
      </a-form-item>
      <!-- 错误信息 -->
      <a-form-item field="errorMessage" label="错误信息">
        <a-input v-model="formModel.errorMessage"
                 placeholder="请输入错误信息"
                 allow-clear />
      </a-form-item>
      <!-- 执行开始时间 -->
      <a-form-item field="startTime" label="执行开始时间">
        <a-date-picker v-model="formModel.startTime"
                       style="width: 100%"
                       placeholder="请选择执行开始时间"
                       show-time
                       allow-clear />
      </a-form-item>
      <!-- 执行结束时间 -->
      <a-form-item field="finishTime" label="执行结束时间">
        <a-date-picker v-model="formModel.finishTime"
                       style="width: 100%"
                       placeholder="请选择执行结束时间"
                       show-time
                       allow-clear />
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
          主机执行记录列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 新增 -->
          <a-button v-permission="['asset:exec-host-log:create']"
                    type="primary"
                    @click="emits('openAdd')">
            新增
            <template #icon>
              <icon-plus />
            </template>
          </a-button>
        </a-space>
      </div>
    </template>
    <!-- table -->
    <a-table row-key="id"
             ref="tableRef"
             :loading="loading"
             :columns="columns"
             :data="tableRenderData"
             :pagination="pagination"
             @page-change="(page) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size) => fetchTableData(1, size)"
             :bordered="false">
      <!-- 执行状态 -->
      <template #status="{ record }">
        {{ getDictValue(execHostStatusKey, record.status) }}
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 修改 -->
          <a-button v-permission="['asset:exec-host-log:update']"
                    type="text"
                    size="mini"
                    @click="emits('openUpdate', record)">
            修改
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['asset:exec-host-log:delete']"
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
    name: 'execHostLogTable'
  };
</script>

<script lang="ts" setup>
  import type { ExecHostLogQueryRequest, ExecHostLogQueryResponse } from '@/api/exec/exec-host-log';
  import { reactive, ref, onMounted } from 'vue';
  import { batchDeleteExecHostLog, deleteExecHostLog, getExecHostLogPage } from '@/api/exec/exec-host-log';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { execHostStatusKey } from '../types/const';
  import { usePagination } from '@/types/table';
  import { useDictStore } from '@/store';

  const emits = defineEmits(['openAdd', 'openUpdate']);

  const pagination = usePagination();
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();

  const tableRenderData = ref<ExecHostLogQueryResponse[]>([]);
  const formModel = reactive<ExecHostLogQueryRequest>({
    id: undefined,
    logId: undefined,
    hostId: undefined,
    hostName: undefined,
    status: undefined,
    command: undefined,
    parameter: undefined,
    exitStatus: undefined,
    logPath: undefined,
    errorMessage: undefined,
    startTime: undefined,
    finishTime: undefined,
  });

  // 删除当前行
  const deleteRow = async ({ id }: {
    id: number
  }) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteExecHostLog(id);
      Message.success('删除成功');
      // 重新加载数据
      fetchTableData();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 添加后回调
  const addedCallback = () => {
    fetchTableData();
  };

  // 更新后回调
  const updatedCallback = () => {
    fetchTableData();
  };

  defineExpose({
    addedCallback, updatedCallback
  });

  // 加载数据
  const doFetchTableData = async (request: ExecHostLogQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getExecHostLogPage(request);
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
