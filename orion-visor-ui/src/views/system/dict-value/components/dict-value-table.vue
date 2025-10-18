<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  @submit="fetchTableData"
                  @reset="resetForm"
                  @keyup.enter="() => fetchTableData()">
      <!-- 配置项 -->
      <a-form-item field="keyId" label="配置项">
        <dict-key-selector v-model="formModel.keyId"
                           @change="changeKey"
                           allow-create
                           allow-clear />
      </a-form-item>
      <!-- 配置描述 -->
      <a-form-item field="label" label="配置描述">
        <a-input v-model="formModel.label" placeholder="请输入配置描述" allow-clear />
      </a-form-item>
      <!-- 配置值 -->
      <a-form-item field="value" label="配置值">
        <a-input v-model="formModel.value" placeholder="请输入配置值" allow-clear />
      </a-form-item>
      <!-- 配置值 -->
      <a-form-item field="extra" label="额外参数">
        <a-input v-model="formModel.extra" placeholder="额外参数" allow-clear />
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
          字典配置值列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 新增 -->
          <a-button type="primary"
                    v-permission="['infra:dict-value:create']"
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
            <a-button v-permission="['infra:dict-value:delete']"
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
      <!-- 配置项 -->
      <template #keyName="{ record }">
        {{ record.keyName }}<span style="margin: 0 4px;">-</span>{{ record.keyDescription }}
      </template>
      <!-- 配置值 -->
      <template #value="{ record }">
        <a-tooltip position="tl"
                   :content="record.value"
                   @click="copy(record.value, true)">
          <span class="text-copy">{{ record.value }}</span>
        </a-tooltip>
      </template>
      <!-- 额外参数 -->
      <template #extra="{ record }">
        <span class="text-copy"
              @click="copy(record.extra, true)">
          {{ record.extra }}
        </span>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 修改 -->
          <a-button type="text"
                    size="mini"
                    v-permission="['infra:dict-value:update']"
                    @click="emits('openUpdate', record)">
            修改
          </a-button>
          <!-- 历史 -->
          <a-button type="text"
                    size="mini"
                    v-permission="['infra:dict-value:update']"
                    @click="emits('openHistory', record)">
            历史
          </a-button>
          <!-- 删除 -->
          <a-popconfirm content="确认删除这条记录吗?"
                        position="left"
                        type="warning"
                        @ok="deleteRow(record)">
            <a-button v-permission="['infra:dict-value:delete']"
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
    name: 'dictValueTable'
  };
</script>

<script lang="ts" setup>
  import type { DictValueQueryRequest, DictValueQueryResponse } from '@/api/system/dict-value';
  import { reactive, ref, onMounted } from 'vue';
  import { batchDeleteDictValue, deleteDictValue, getDictValuePage } from '@/api/system/dict-value';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { useTablePagination, useRowSelection, useTableColumns } from '@/hooks/table';
  import { copy } from '@/hooks/copy';
  import { TableName } from '../types/const';
  import { useQueryOrder, DESC } from '@/hooks/query-order';
  import DictKeySelector from '@/components/system/dict-key/selector/index.vue';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openHistory']);

  const rowSelection = useRowSelection();
  const pagination = useTablePagination();
  const queryOrder = useQueryOrder(TableName, DESC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);
  const { loading, setLoading } = useLoading();

  const selectedKeys = ref<Array<number>>([]);
  const tableRenderData = ref<Array<DictValueQueryResponse>>([]);

  const formModel = reactive<DictValueQueryRequest>({
    keyId: undefined,
    keyName: undefined,
    value: undefined,
    label: undefined,
    extra: undefined,
  });

  // 删除选中行
  const deleteSelectedRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await batchDeleteDictValue(selectedKeys.value);
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
  const deleteRow = async (record: DictValueQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteDictValue(record.id);
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

  // 修改 key
  const changeKey = ({ id, keyName }: { id: number, keyName: string }) => {
    if (id) {
      formModel.keyId = id;
    } else {
      formModel.keyName = keyName;
    }
  };

  // 清空表单
  const resetForm = () => {
    formModel.keyName = undefined;
    fetchTableData();
  };

  // 加载数据
  const doFetchTableData = async (request: DictValueQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getDictValuePage(queryOrder.markOrderly(request));
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
