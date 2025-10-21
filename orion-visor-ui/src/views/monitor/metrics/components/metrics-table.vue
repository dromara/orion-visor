<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- 指标名称 -->
      <a-form-item field="name" label="指标名称">
        <a-input v-model="formModel.name"
                 placeholder="请输入指标名称"
                 allow-clear />
      </a-form-item>
      <!-- 数据集 -->
      <a-form-item field="measurement" label="数据集">
        <a-select v-model="formModel.measurement"
                  :options="toOptions(MeasurementKey)"
                  placeholder="请选择数据集"
                  allow-clear />
      </a-form-item>
      <!-- 指标项 -->
      <a-form-item field="value" label="指标项">
        <a-input v-model="formModel.value"
                 placeholder="请输入指标项"
                 allow-clear />
      </a-form-item>
      <!-- 指标描述 -->
      <a-form-item field="description" label="指标描述">
        <a-input v-model="formModel.description"
                 placeholder="请输入指标描述"
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
          监控指标列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 新增 -->
          <a-button v-permission="['monitor:monitor-metrics:create']"
                    type="primary"
                    @click="emits('openAdd')">
            新增
            <template #icon>
              <icon-plus />
            </template>
          </a-button>
          <!-- 调整 -->
          <table-adjust :columns="columns"
                        :columns-hook="columnsHook"
                        :query-order="queryOrder"
                        @query="fetchTableData" />
        </a-space>
      </div>
    </template>
    <!-- table -->
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
      <!-- 数据集 -->
      <template #measurement="{ record }">
        <span class="span-blue text-copy" @click="copy(record.measurement, '数据集名称已复制')">
          {{ getDictValue(MeasurementKey, record.measurement) }}
        </span>
      </template>
      <!-- 指标项 -->
      <template #value="{ record }">
        <span class="span-blue text-copy" @click="copy(record.value, true)">
          {{ record.value }}
        </span>
      </template>
      <!-- 单位 -->
      <template #unit="{ record }">
        <div>
          <span v-if="record.unit === MetricsUnit.TEXT">
            {{ record.suffix }}
          </span>
          <span v-else-if="record.unit === MetricsUnit.NONE">
            -
          </span>
          <span v-else>
            {{ getDictValue(MetricsUnitKey, record.unit) }}
          </span>
        </div>
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 修改 -->
          <a-button v-permission="['monitor:monitor-metrics:update']"
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
            <a-button v-permission="['monitor:monitor-metrics:delete']"
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
    name: 'metricsTable'
  };
</script>

<script lang="ts" setup>
  import type { MetricsQueryRequest, MetricsQueryResponse } from '@/api/monitor/metrics';
  import { reactive, ref, onMounted } from 'vue';
  import { deleteMetrics, getMetricsPage } from '@/api/monitor/metrics';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import columns from '../types/table.columns';
  import { MetricsUnit } from '@/utils/metrics';
  import { TableName, MeasurementKey, MetricsUnitKey } from '../types/const';
  import { useTablePagination, useTableColumns } from '@/hooks/table';
  import { useDictStore } from '@/store';
  import { useQueryOrder, ASC } from '@/hooks/query-order';
  import { copy } from '@/hooks/copy';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['openAdd', 'openUpdate']);

  const pagination = useTablePagination();
  const queryOrder = useQueryOrder(TableName, ASC);
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);
  const { loading, setLoading } = useLoading();
  const { toOptions, getDictValue } = useDictStore();

  const tableRenderData = ref<Array<MetricsQueryResponse>>([]);
  const formModel = reactive<MetricsQueryRequest>({
    id: undefined,
    name: undefined,
    measurement: undefined,
    value: undefined,
    unit: undefined,
    suffix: undefined,
    description: undefined,
  });

  // 删除当前行
  const deleteRow = async (record: MetricsQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteMetrics(record.id);
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

  // 加载数据
  const doFetchTableData = async (request: MetricsQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getMetricsPage(queryOrder.markOrderly(request));
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
