<template>
  <a-modal v-model:visible="visible"
           title-align="start"
           title="执行模板"
           width="86%"
           :top="80"
           :body-style="{ padding: '0 8px' }"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :footer="false"
           @close="handleClose">
    <!-- 搜索 -->
    <a-card class="general-card table-search-card"
            style="margin-bottom: 0;">
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
      <!-- table -->
      <a-table row-key="id"
               ref="tableRef"
               class="table-resize"
               :loading="loading"
               :columns="columns"
               :data="tableRenderData"
               :pagination="pagination"
               :bordered="false"
               :column-resizable="true"
               :scroll="{ x: '100%', y: '60vh' }"
               @page-change="(page: number) => fetchTableData(page, pagination.pageSize)"
               @page-size-change="(size: number) => fetchTableData(1, size)">
        <!-- 模板名称 -->
        <template #name="{ record }">
          <span class="span-blue">{{ record.name }}</span>
        </template>
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
            <!-- 选择 -->
            <a-button type="text"
                      size="mini"
                      @click="selectedTemplate(record)">
              选择
            </a-button>
          </div>
        </template>
      </a-table>
    </a-card>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'execTemplateModal'
  };
</script>

<script lang="ts" setup>
  import type { ExecTemplateQueryRequest, ExecTemplateQueryResponse } from '@/api/exec/exec-template';
  import { reactive, ref } from 'vue';
  import { useTablePagination } from '@/hooks/table';
  import useVisible from '@/hooks/visible';
  import useLoading from '@/hooks/loading';
  import { copy } from '@/hooks/copy';
  import { getExecTemplatePage } from '@/api/exec/exec-template';
  import columns from './table.columns';

  const emits = defineEmits(['selected']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const pagination = useTablePagination();

  const tableRenderData = ref<ExecTemplateQueryResponse[]>([]);
  const formModel = reactive<ExecTemplateQueryRequest>({
    id: undefined,
    name: undefined,
    command: undefined,
  });

  // 打开
  const open = () => {
    setVisible(true);
    // 加载数据
    if (!tableRenderData.value.length) {
      fetchTableData();
    }
  };

  defineExpose({ open });

  // 选择模板
  const selectedTemplate = (record: ExecTemplateQueryResponse) => {
    emits('selected', record);
    handleClose();
  };

  // 加载数据
  const doFetchTableData = async (request: ExecTemplateQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getExecTemplatePage(request);
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

  // 关闭回调
  const handleClose = () => {
    handleClear();
  };

  // 清空
  const handleClear = () => {
    setLoading(false);
    setVisible(false);
  };

</script>

<style lang="less" scoped>

</style>
