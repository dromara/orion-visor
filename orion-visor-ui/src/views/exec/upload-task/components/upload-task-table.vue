<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  :itemOptions="{ 5: { span: 2 } }"
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
      <!-- 上传用户 -->
      <a-form-item field="userId" label="上传用户">
        <user-selector v-model="formModel.userId"
                       placeholder="请选择上传用户"
                       allow-clear />
      </a-form-item>
      <!-- 远程路径 -->
      <a-form-item field="remotePath" label="远程路径">
        <a-input v-model="formModel.remotePath"
                 placeholder="请输入远程路径"
                 allow-clear />
      </a-form-item>
      <!-- 上传描述 -->
      <a-form-item field="description" label="上传描述">
        <a-input v-model="formModel.description"
                 placeholder="请输入上传描述"
                 allow-clear />
      </a-form-item>
      <!-- 上传状态 -->
      <a-form-item field="status" label="上传状态">
        <a-select v-model="formModel.status"
                  :options="toOptions(uploadTaskStatusKey)"
                  placeholder="请选择状态"
                  allow-clear />
      </a-form-item>
      <!-- 上传时间 -->
      <a-form-item field="createTimeRange" label="上传时间">
        <a-range-picker v-model="formModel.createTimeRange"
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
          上传任务列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 上传 -->
          <a-button v-permission="['exec:upload-task:upload']"
                    type="primary"
                    @click="router.push({ name: 'batchUpload' })">
            上传
            <template #icon>
              <icon-upload />
            </template>
          </a-button>
          <!-- 清理 -->
          <a-button v-permission="['exec:upload-task:management:clear']"
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
            <a-button v-permission="['exec:upload-task:delete']"
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
          <table-adjust :columns="columns"
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
             :data="tableRenderData"
             :pagination="pagination"
             :bordered="false"
             :column-resizable="true"
             @page-change="(page: number) => fetchTableData(page, pagination.pageSize)"
             @page-size-change="(size: number) => fetchTableData(1, size)">
      <!-- 上传路径 -->
      <template #remotePath="{ record }">
        <span class="text-copy span-blue" @click="copy(record.remotePath)">
          {{ record.remotePath }}
        </span>
      </template>
      <!-- 文件数量 -->
      <template #fileCount="{ record }">
        <span class="span-blue">
          {{ record.fileCount }}
        </span>
      </template>
      <!-- 主机数量 -->
      <template #hostCount="{ record }">
        <span class="span-blue">
          {{ record.hostCount }}
        </span>
      </template>
      <!-- 上传状态 -->
      <template #status="{ record }">
        <a-tag :color="getDictValue(uploadTaskStatusKey, record.status, 'color')">
          {{ getDictValue(uploadTaskStatusKey, record.status) }}
        </a-tag>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 详情 -->
          <a-button v-permission="['exec:upload-task:query']"
                    type="text"
                    size="mini"
                    @click="router.push({ name: 'batchUpload', query: { id: record.id } })">
            详情
          </a-button>
          <!-- 取消 -->
          <a-popconfirm v-if="record.status === UploadTaskStatus.WAITING || record.status === UploadTaskStatus.UPLOADING"
                        content="确定要取消上传吗?"
                        position="left"
                        type="warning"
                        @ok="doCancel(record)">
            <a-button v-permission="['exec:upload-task:upload']"
                      type="text"
                      size="mini">
              取消
            </a-button>
          </a-popconfirm>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['exec:upload-task:delete']"
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
    name: 'uploadTaskTable'
  };
</script>

<script lang="ts" setup>
  import type { UploadTaskQueryRequest, UploadTaskQueryResponse } from '@/api/exec/upload-task';
  import { reactive, ref, onMounted, onUnmounted } from 'vue';
  import { batchDeleteUploadTask, deleteUploadTask, getUploadTaskPage, cancelUploadTask, getUploadTaskStatus } from '@/api/exec/upload-task';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { TableName, UploadTaskStatus, uploadTaskStatusKey } from '../types/const';
  import { useTablePagination, useRowSelection, useTableColumns } from '@/hooks/table';
  import { useQueryOrder, DESC } from '@/hooks/query-order';
  import { useRouter } from 'vue-router';
  import { useDictStore } from '@/store';
  import { copy } from '@/hooks/copy';
  import UserSelector from '@/components/user/user/selector/index.vue';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['openClear']);

  const router = useRouter();
  const rowSelection = useRowSelection();
  const pagination = useTablePagination();
  const queryOrder = useQueryOrder(TableName, DESC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();

  const pullIntervalId = ref();
  const selectedKeys = ref<Array<number>>([]);
  const tableRenderData = ref<Array<UploadTaskQueryResponse>>([]);
  const formModel = reactive<UploadTaskQueryRequest>({
    id: undefined,
    userId: undefined,
    remotePath: undefined,
    description: undefined,
    status: undefined,
    createTimeRange: undefined,
  });

  // 打开清理
  const openClear = () => {
    emits('openClear', { ...formModel, id: undefined });
  };

  // 执行取消
  const doCancel = async (record: UploadTaskQueryResponse) => {
    try {
      setLoading(true);
      // 取消
      await cancelUploadTask(record.id, false);
      // 设置状态
      record.status = UploadTaskStatus.CANCELED;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 删除选中行
  const deleteSelectedRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await batchDeleteUploadTask(selectedKeys.value);
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
  const deleteRow = async (record: UploadTaskQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteUploadTask(record.id);
      Message.success('删除成功');
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

  // 加载状态
  const pullTaskStatus = async () => {
    const unCompleteIdList = tableRenderData.value
      .filter(s => s.status === UploadTaskStatus.WAITING || s.status === UploadTaskStatus.UPLOADING)
      .map(s => s.id);
    if (!unCompleteIdList.length) {
      return;
    }
    // 加载未完成的状态
    const { data } = await getUploadTaskStatus(unCompleteIdList, false);
    data.forEach(s => {
      const tableRow = tableRenderData.value.find(r => r.id === s.id);
      if (!tableRow) {
        return;
      }
      tableRow.status = s.status;
    });
  };

  // 加载数据
  const doFetchTableData = async (request: UploadTaskQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getUploadTaskPage(queryOrder.markOrderly(request));
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

  onMounted(() => {
    // 加载数据
    fetchTableData();
    // 注册状态轮询
    pullIntervalId.value = window.setInterval(pullTaskStatus, 10000);
  });

  onUnmounted(() => {
    // 卸载状态轮询
    clearInterval(pullIntervalId.value);
  });

</script>

<style lang="less" scoped>
</style>
