<template>
  <!-- 查询头 -->
  <a-card class="general-card table-search-card">
    <!-- 查询头组件 -->
    <operator-log-query-header :model="formModel"
                               @submit="fetchTableData" />
  </a-card>
  <!-- 表格 -->
  <a-card class="general-card table-card">
    <template #title>
      <!-- 左侧操作 -->
      <div class="table-left-bar-handle">
        <!-- 标题 -->
        <div class="table-title">
          操作日志
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 清理 -->
          <a-button v-permission="['infra:operator-log:management:clear']"
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
            <a-button v-permission="['infra:operator-log:delete']"
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
    <!-- 表格 -->
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
      <!-- 操作模块 -->
      <template #module="{ record }">
        <span>{{ getDictValue(operatorLogModuleKey, record.module) }}</span>
        <icon-oblique-line />
        <span>{{ getDictValue(operatorLogTypeKey, record.type) }}</span>
      </template>
      <!-- 风险等级 -->
      <template #riskLevel="{ record }">
        <a-tag :color="getDictValue(operatorRiskLevelKey, record.riskLevel, 'color')">
          {{ getDictValue(operatorRiskLevelKey, record.riskLevel) }}
        </a-tag>
      </template>
      <!-- 执行结果 -->
      <template #result="{ record }">
        <a-tag :color="getDictValue(operatorLogResultKey, record.result, 'color')">
          {{ getDictValue(operatorLogResultKey, record.result) }}
        </a-tag>
      </template>
      <!-- 操作日志 -->
      <template #originLogInfo="{ record }">
        <!-- 操作日志 -->
        <a-tooltip position="tl"
                   :content="record.originLogInfo">
          <span v-html="replaceHtmlTag(record.logInfo)"
                class="text-copy"
                @click="copy(record.originLogInfo, true)" />
        </a-tooltip>
        <!-- 错误消息 -->
        <br v-if="record.errorMessage">
        <span v-if="record.errorMessage"
              class="table-cell-sub-value text-copy error-message"
              @click="copy(record.errorMessage, true)">
          {{ record.errorMessage }}
        </span>
      </template>
      <!-- 留痕地址 -->
      <template #address="{ record }">
        <span class="table-cell-value" :title="record.location">
          {{ record.location }}
        </span>
        <br>
        <span class="table-cell-sub-value text-copy"
              :title="record.address"
              @click="copy(record.address)">
          {{ record.address }}
        </span>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
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
            <a-button v-permission="['infra:operator-log:delete']"
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
    name: 'operatorLogTable'
  };
</script>

<script lang="ts" setup>
  import type { OperatorLogQueryRequest, OperatorLogQueryResponse } from '@/api/user/operator-log';
  import { ref, reactive, onMounted } from 'vue';
  import { TableName, operatorLogModuleKey, operatorLogTypeKey, operatorRiskLevelKey, operatorLogResultKey } from '../types/const';
  import columns from '../types/table.columns';
  import { copy } from '@/hooks/copy';
  import useLoading from '@/hooks/loading';
  import { useRoute } from 'vue-router';
  import { useDictStore } from '@/store';
  import { useTablePagination, useRowSelection, useTableColumns } from '@/hooks/table';
  import { getOperatorLogPage, deleteOperatorLog } from '@/api/user/operator-log';
  import { replaceHtmlTag, clearHtmlTag } from '@/utils';
  import { Message } from '@arco-design/web-vue';
  import { useQueryOrder, DESC } from '@/hooks/query-order';
  import OperatorLogQueryHeader from './operator-log-query-header.vue';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['openClear', 'openDetail']);

  const rowSelection = useRowSelection();
  const pagination = useTablePagination();
  const queryOrder = useQueryOrder(TableName, DESC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);
  const { loading, setLoading } = useLoading();
  const { getDictValue } = useDictStore();

  const selectedKeys = ref<Array<number>>([]);
  const tableRenderData = ref<Array<OperatorLogQueryResponse>>([]);
  const formModel = reactive<OperatorLogQueryRequest>({
    module: undefined,
    type: undefined,
    riskLevel: undefined,
    result: undefined,
    startTimeRange: undefined,
  });

  // 打开清空
  const openClear = () => {
    emits('openClear', { ...formModel });
  };

  // 删除选中行
  const deleteSelectedRows = async () => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteOperatorLog(selectedKeys.value);
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
  const deleteRow = async (record: OperatorLogQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteOperatorLog([record.id]);
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
  const doFetchTableData = async (request: OperatorLogQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getOperatorLogPage(queryOrder.markOrderly(request));
      tableRenderData.value = data.rows.map(s => {
        return { ...s, originLogInfo: clearHtmlTag(s.logInfo) };
      });
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

  // 初始化
  onMounted(() => {
    const operatorType = useRoute().query.operatorType as string;
    if (operatorType) {
      formModel.type = operatorType;
    }
    // 查询数据
    fetchTableData();
  });

</script>

<style lang="less" scoped>
  .error-message {
    color: rgb(var(--red-6));
    font-size: 12px;
  }
</style>
