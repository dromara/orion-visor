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
      <!-- 任务名称 -->
      <a-form-item field="name" label="任务名称">
        <a-input v-model="formModel.name"
                 placeholder="请输入任务名称"
                 allow-clear />
      </a-form-item>
      <!-- 执行命令 -->
      <a-form-item field="command" label="执行命令">
        <a-input v-model="formModel.command"
                 placeholder="请输入执行命令"
                 allow-clear />
      </a-form-item>
      <!-- 任务状态 -->
      <a-form-item field="status" label="任务状态">
        <a-select v-model="formModel.status"
                  :options="toOptions(execJobStatusKey)"
                  placeholder="请选择状态"
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
          计划任务列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 新增 -->
          <a-button v-permission="['asset:exec-job:create']"
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
             :bordered="false"
             @page-change="(page) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size) => fetchTableData(1, size)">
      <!-- cron -->
      <template #expression="{ record }">
        <span class="copy-left"
              title="复制"
              @click="copy(record.expression, '已复制')">
          <icon-copy />
        </span>
        <span class="text-copy span-blue"
              title="查看下次执行时间"
              @click="emits('testCron', record.expression)">
          {{ record.expression }}
        </span>
      </template>
      <!-- 命令 -->
      <template #command="{ record }">
        <span class="copy-left"
              title="复制"
              @click="copy(record.command, '已复制')">
          <icon-copy />
        </span>
        <span>{{ record.command }}</span>
      </template>
      <!-- 任务状态 -->
      <template #status="{ record }">
        <!-- 状态开关 可编辑 -->
        <a-switch v-if="hasPermission('asset:exec-job:update-status')"
                  type="round"
                  :default-checked="record.status === ExecJobStatus.ENABLED"
                  :checked-text="getDictValue(execJobStatusKey, ExecJobStatus.ENABLED)"
                  :unchecked-text="getDictValue(execJobStatusKey, ExecJobStatus.DISABLED)"
                  :checked-value="ExecJobStatus.ENABLED"
                  :unchecked-value="ExecJobStatus.DISABLED"
                  :before-change="s => updateStatus(record.id, s as number)" />
        <!-- 状态 不可编辑 -->
        <a-tag v-else :color="getDictValue(execJobStatusKey, record.status, 'color')">
          {{ getDictValue(execJobStatusKey, record.status) }}
        </a-tag>
      </template>
      <!-- 最近执行 -->
      <template #recentLog="{ record }">
        <div class="flex-center" v-if="record.recentLogId && record.recentLogStatus">
          <!-- 执行状态 -->
          <a-tag class="mr8" :color="getDictValue(execStatusKey, record.recentLogStatus, 'color')">
            {{ getDictValue(execStatusKey, record.recentLogStatus) }}
          </a-tag>
          <!-- 执行时间 -->
          {{ dateFormat(new Date(record.recentLogTime), 'MM-dd HH:mm') }}
        </div>
        <!-- 无任务 -->
        <div v-else class="mx8">-</div>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 详情 -->
          <a-button type="text"
                    size="mini"
                    @click="emits('openDetail', record.id)">
            详情
          </a-button>
          <!-- 修改 -->
          <a-button v-permission="['asset:exec-job:update']"
                    type="text"
                    size="mini"
                    @click="emits('openUpdate', record.id)">
            修改
          </a-button>
          <!-- 手动触发 -->
          <a-popconfirm content="确认要手动触发吗?"
                        position="left"
                        type="warning"
                        @ok="triggerJob(record.id)">
            <a-button v-permission="['asset:exec-job:trigger']"
                      type="text"
                      size="mini">
              手动触发
            </a-button>
          </a-popconfirm>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['asset:exec-job:delete']"
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
    name: 'execJobTable'
  };
</script>

<script lang="ts" setup>
  import type { ExecJobQueryRequest, ExecJobQueryResponse } from '@/api/exec/exec-job';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteExecJob, getExecJobPage, triggerExecJob, updateExecJobStatus } from '@/api/exec/exec-job';
  import { Message } from '@arco-design/web-vue';
  import usePermission from '@/hooks/permission';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { ExecJobStatus, execJobStatusKey, execStatusKey } from '../types/const';
  import { usePagination } from '@/types/table';
  import { useDictStore } from '@/store';
  import { copy } from '@/hooks/copy';
  import { dateFormat } from '@/utils';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openDetail', 'testCron']);

  const pagination = usePagination();
  const { loading, setLoading } = useLoading();
  const { hasPermission } = usePermission();
  const { toOptions, getDictValue } = useDictStore();

  const tableRenderData = ref<ExecJobQueryResponse[]>([]);
  const formModel = reactive<ExecJobQueryRequest>({
    id: undefined,
    name: undefined,
    command: undefined,
    status: undefined,
    queryRecentLog: true
  });

  // 删除当前行
  const deleteRow = async ({ id }: {
    id: number
  }) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteExecJob(id);
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

  // 修改状态
  const updateStatus = async (id: number, status: number) => {
    return updateExecJobStatus({
      id,
      status
    }).then(() => {
      return true;
    }).catch(() => {
      return false;
    });
  };

  // 手动触发任务
  const triggerJob = async (id: number) => {
    setLoading(true);
    try {
      await triggerExecJob(id);
      Message.success('已触发');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 加载数据
  const doFetchTableData = async (request: ExecJobQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getExecJobPage(request);
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
