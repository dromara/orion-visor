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
          <a-button v-permission="['asset:exec-template:create']"
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
      <!-- 模板名称 -->
      <template #name="{ record }">
        <span class="span-blue">{{ record.name }}</span>
      </template>
      <!-- 模板命令 -->
      <template #command="{ record }">
        <span class="copy-left" @click="copy(record.command, '已复制')">
          <icon-copy />
        </span>
        <span :title="record.command">{{ record.command }}</span>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 修改 -->
          <a-button v-permission="['asset:exec-template:update']"
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
            <a-button v-permission="['asset:exec-template:delete']"
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
  import { deleteExecTemplate, getExecTemplatePage } from '@/api/exec/exec-template';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import {} from '../types/const';
  import { usePagination } from '@/types/table';
  import useCopy from '@/hooks/copy';

  const emits = defineEmits(['openAdd', 'openUpdate']);

  const tableRenderData = ref<ExecTemplateQueryResponse[]>([]);

  const pagination = usePagination();
  const { loading, setLoading } = useLoading();
  const { copy } = useCopy();

  const formModel = reactive<ExecTemplateQueryRequest>({
    id: undefined,
    name: undefined,
    command: undefined,
  });

  // 删除当前行
  const deleteRow = async ({ id }: {
    id: number
  }) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteExecTemplate(id);
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

  onMounted(() => {
    fetchTableData();
  });

</script>

<style lang="less" scoped>

</style>
