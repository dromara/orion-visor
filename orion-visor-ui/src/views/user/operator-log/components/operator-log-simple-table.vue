<template>
  <!-- 表格 -->
  <a-table row-key="id"
           ref="tableRef"
           class="table-resize"
           :loading="loading"
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
                  @click="openLogDetail(record)">
          详情
        </a-button>
      </div>
    </template>
  </a-table>
  <!-- json 查看器模态框 -->
  <json-editor-modal ref="jsonView" />
</template>

<script lang="ts">
  export default {
    name: 'operatorLogSimpleTable'
  };
</script>

<script lang="ts" setup>
  import type { OperatorLogQueryRequest, OperatorLogQueryResponse } from '@/api/user/operator-log';
  import { ref, onMounted } from 'vue';
  import { operatorLogModuleKey, operatorLogTypeKey, operatorRiskLevelKey, operatorLogResultKey, dictKeys } from '../types/const';
  import columns from '../types/table.columns';
  import { TableName, getLogDetail } from '../types/const';
  import { copy } from '@/hooks/copy';
  import useLoading from '@/hooks/loading';
  import { useTablePagination } from '@/hooks/table';
  import { useDictStore } from '@/store';
  import { getOperatorLogPage } from '@/api/user/operator-log';
  import { getCurrentUserOperatorLog } from '@/api/user/mine';
  import { replaceHtmlTag, clearHtmlTag } from '@/utils';
  import { useQueryOrder, DESC } from '@/hooks/query-order';
  import JsonEditorModal from '@/components/view/json-editor/modal/index.vue';

  const props = withDefaults(defineProps<Partial<{
    handleColumn: boolean;
    current: boolean;
    baseParams: OperatorLogQueryRequest;
    model: OperatorLogQueryRequest;
  }>>(), {
    baseParams: () => {
      return {};
    },
    model: () => {
      return {};
    },
  });

  const pagination = useTablePagination();
  const { markOrderly } = useQueryOrder(TableName, DESC);
  const { loading, setLoading } = useLoading();
  const { getDictValue, loadKeys } = useDictStore();

  const jsonView = ref();
  const tableColumns = ref();
  const tableRenderData = ref<OperatorLogQueryResponse[]>([]);

  // 查看详情
  const openLogDetail = (record: OperatorLogQueryResponse) => {
    jsonView.value.open(getLogDetail(record));
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
      let rows;
      if (props.current) {
        // 查询当前用户
        const { data } = await getCurrentUserOperatorLog(markOrderly(request));
        rows = data;
      } else {
        // 查询所有
        const { data } = await getOperatorLogPage(markOrderly({ ...request, ...props.baseParams }));
        rows = data;
      }
      tableRenderData.value = rows.rows.map(s => {
        return { ...s, originLogInfo: clearHtmlTag(s.logInfo) };
      });
      pagination.total = rows.total;
      pagination.current = request.page;
      pagination.pageSize = request.limit;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 切换页码
  const fetchTableData = (page = 1, limit = pagination.pageSize, form = props.model) => {
    doFetchTableData({ page, limit, ...form });
  };

  // 初始化
  onMounted(async () => {
    // 加载字典值
    await loadKeys(dictKeys);
    // 设置表格列
    let cols = columns.map(s => {
      return { ...s };
    }).filter(s => s.dataIndex !== 'username');
    if (props.handleColumn) {
      const handleCol = cols.find(s => s.dataIndex === 'handle');
      // 设置操作项宽度
      if (handleCol) {
        handleCol.width = 80;
      }
    } else {
      // 不显示操作
      cols = cols.filter(s => s.dataIndex !== 'handle');
    }
    tableColumns.value = cols;
    // 加载数据
    fetchTableData();
  });

</script>

<style lang="less" scoped>
  .error-message {
    color: rgb(var(--red-6));
    font-size: 12px;
  }
</style>
