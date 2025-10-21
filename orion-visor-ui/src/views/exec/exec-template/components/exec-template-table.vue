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
      <!-- 模板名称 -->
      <a-form-item field="name" label="模板名称">
        <a-input v-model="formModel.name"
                 placeholder="请输入模板名称"
                 allow-clear />
      </a-form-item>
      <!-- 模板命令 -->
      <a-form-item field="command" label="模板命令">
        <a-input v-model="formModel.command"
                 placeholder="请输入模板命令"
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
          执行模板列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 新增 -->
          <a-button v-permission="['exec:exec-template:create']"
                    type="primary"
                    @click="emits('openAdd')">
            新增
            <template #icon>
              <icon-plus />
            </template>
          </a-button>
          <!-- 删除 -->
          <a-popconfirm :content="`确认删除选中的 ${selectedKeys.length} 条记录吗?`"
                        position="br"
                        type="warning"
                        @ok="deleteSelectedRows">
            <a-button v-permission="['exec:exec-template:delete']"
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
      <!-- 模板命令 -->
      <template #command="{ record }">
        <span class="copy-left"
              title="复制"
              @click="copy(record.command, true)">
          <icon-copy />
        </span>
        <span :title="record.command">{{ record.command }}</span>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <a-button v-permission="['exec:exec-command:exec']"
                    type="text"
                    size="mini"
                    @click="emits('openExec', record.id)">
            执行
          </a-button>
          <!-- 修改 -->
          <a-button v-permission="['exec:exec-template:update']"
                    type="text"
                    size="mini"
                    @click="emits('openUpdate', record.id)">
            修改
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['exec:exec-template:delete']"
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
    name: 'execTemplateTable'
  };
</script>

<script lang="ts" setup>
  import type { ExecTemplateQueryRequest, ExecTemplateQueryResponse } from '@/api/exec/exec-template';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteExecTemplate, batchDeleteExecTemplate, getExecTemplatePage } from '@/api/exec/exec-template';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { useTablePagination, useRowSelection, useTableColumns } from '@/hooks/table';
  import { TableName } from '../types/const';
  import { useQueryOrder, DESC } from '@/hooks/query-order';
  import { copy } from '@/hooks/copy';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openExec']);

  const rowSelection = useRowSelection();
  const pagination = useTablePagination();
  const queryOrder = useQueryOrder(TableName, DESC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);
  const { loading, setLoading } = useLoading();

  const selectedKeys = ref<Array<number>>([]);
  const tableRenderData = ref<Array<ExecTemplateQueryResponse>>([]);
  const formModel = reactive<ExecTemplateQueryRequest>({
    id: undefined,
    name: undefined,
    command: undefined,
  });

  // 删除当前行
  const deleteRow = async (record: ExecTemplateQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteExecTemplate(record.id);
      Message.success('删除成功');
      // 重新加载
      reload();
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
      await batchDeleteExecTemplate(selectedKeys.value);
      Message.success(`成功删除 ${selectedKeys.value.length} 条数据`);
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
  const doFetchTableData = async (request: ExecTemplateQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getExecTemplatePage(queryOrder.markOrderly(request));
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
    fetchTableData();
  });

</script>

<style lang="less" scoped>

</style>
