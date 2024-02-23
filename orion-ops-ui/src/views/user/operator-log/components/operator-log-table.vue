<template>
  <a-table row-key="id"
           class="table-wrapper-8"
           ref="tableRef"
           label-align="left"
           :loading="loading"
           :columns="tableColumns"
           :data="tableRenderData"
           :pagination="pagination"
           @page-change="(page) => fetchTableData(page, pagination.pageSize)"
           @page-size-change="(size) => fetchTableData(1, size)"
           :bordered="false">
    <!-- 操作模块 -->
    <template #module="{ record }">
      {{ getDictValue(operatorLogModuleKey, record.module) }}
    </template>
    <!-- 操作类型 -->
    <template #type="{ record }">
      {{ getDictValue(operatorLogTypeKey, record.type) }}
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
      <icon-copy class="copy-left" @click="copy(record.originLogInfo, '已复制')" />
      <span v-html="replaceHtmlTag(record.logInfo)" />
    </template>
    <!-- 操作 -->
    <template #handle="{ record }">
      <div class="table-handle-wrapper">
        <!-- 详情 -->
        <a-button type="text" size="mini" @click="viewDetail(record)">
          详情
        </a-button>
      </div>
    </template>
  </a-table>
</template>

<script lang="ts">
  export default {
    name: 'operatorLogTable'
  };
</script>

<script lang="ts" setup>
  import type { OperatorLogQueryRequest, OperatorLogQueryResponse } from '@/api/user/operator-log';
  import { ref, onMounted } from 'vue';
  import { operatorLogModuleKey, operatorLogTypeKey, operatorRiskLevelKey, operatorLogResultKey } from '../types/const';
  import columns from '../types/table.columns';
  import useLoading from '@/hooks/loading';
  import { usePagination } from '@/types/table';
  import { useDictStore } from '@/store';
  import { getOperatorLogPage } from '@/api/user/operator-log';
  import { replaceHtmlTag, clearHtmlTag, dateFormat } from '@/utils';
  import { pick } from 'lodash';
  import { getCurrentUserOperatorLog } from '@/api/user/mine';
  import useCopy from '@/hooks/copy';

  const emits = defineEmits(['viewDetail']);
  const props = defineProps({
    visibleUser: {
      type: Boolean,
      default: true
    },
    current: {
      type: Boolean,
      default: false
    },
    baseParams: {
      type: Object,
      default: () => {
        return {};
      }
    }
  });

  const pagination = usePagination();
  const { loading, setLoading } = useLoading();
  const { getDictValue } = useDictStore();
  const { copy } = useCopy();

  const tableColumns = ref();
  const tableRenderData = ref<OperatorLogQueryResponse[]>([]);

  // 查看详情
  const viewDetail = (record: OperatorLogQueryResponse) => {
    try {
      const detail = Object.assign({} as Record<string, any>,
        pick(record, 'traceId', 'address', 'location',
          'userAgent', 'errorMessage'));
      detail.duration = `${record.duration} ms`;
      detail.startTime = dateFormat(new Date(record.startTime));
      detail.endTime = dateFormat(new Date(record.endTime));
      detail.extra = JSON.parse(record?.extra);
      detail.returnValue = JSON.parse(record?.returnValue);
      emits('viewDetail', detail);
    } catch (e) {
      emits('viewDetail', record);
    }
  };

  // 加载数据
  const doFetchTableData = async (request: OperatorLogQueryRequest) => {
    try {
      setLoading(true);
      let rows;
      if (props.current) {
        // 查询当前用户
        const { data } = await getCurrentUserOperatorLog(request);
        rows = data;
      } else {
        // 查询所有
        const { data } = await getOperatorLogPage({ ...request, ...props.baseParams });
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
  const fetchTableData = (page = 1, limit = pagination.pageSize, form = {}) => {
    doFetchTableData({ page, limit, ...form });
  };

  onMounted(() => {
    if (props.visibleUser) {
      tableColumns.value = columns;
    } else {
      tableColumns.value = columns.filter(s => s.dataIndex !== 'username');
    }
    fetchTableData();
  });

  defineExpose({
    fetchTableData
  });

</script>

<style lang="less" scoped>

</style>
