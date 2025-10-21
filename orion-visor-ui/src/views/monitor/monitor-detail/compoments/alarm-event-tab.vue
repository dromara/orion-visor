<template>
  <!-- 搜索 -->
  <a-card class="general-card table-search-card">
    <query-header :model="formModel"
                  label-align="left"
                  @submit="fetchTableData"
                  @reset="fetchTableData"
                  @keyup.enter="() => fetchTableData()">
      <!-- 处理状态 -->
      <a-form-item field="handleStatus" label="处理状态">
        <a-select v-model="formModel.handleStatus"
                  :options="toOptions(HandleStatusKey)"
                  placeholder="请选择处理状态"
                  allow-clear />
      </a-form-item>
      <!-- 告警级别 -->
      <a-form-item field="alarmLevel" label="告警级别">
        <a-select v-model="formModel.alarmLevel"
                  :options="toOptions(AlarmLevelKey)"
                  placeholder="请选择告警级别"
                  allow-clear />
      </a-form-item>
      <!-- 处理人 -->
      <a-form-item field="handleUserId" label="处理人">
        <user-selector v-model="formModel.handleUserId"
                       placeholder="请选择处理人"
                       hide-button
                       allow-clear />
      </a-form-item>
      <!-- 处理备注 -->
      <a-form-item field="handleRemark" label="处理备注">
        <a-input v-model="formModel.handleRemark"
                 placeholder="请输入处理备注"
                 allow-clear />
      </a-form-item>
      <!-- 告警策略 -->
      <a-form-item field="policyId" label="告警策略">
        <alarm-policy-selector v-model="formModel.policyId"
                               placeholder="请输入告警策略"
                               hide-button
                               allow-clear />
      </a-form-item>
      <!-- 数据集 -->
      <a-form-item field="metricsId" label="数据集">
        <a-select v-model="formModel.metricsMeasurement"
                  :options="toOptions(MetricsMeasurementKey)"
                  placeholder="数据集"
                  allow-clear />
      </a-form-item>
      <!-- 告警指标 -->
      <a-form-item field="metricsId" label="告警指标">
        <monitor-metrics-selector v-model="formModel.metricsId"
                                  placeholder="请选择告警指标"
                                  hide-button
                                  allow-clear />
      </a-form-item>
      <!-- 是否误报 -->
      <a-form-item field="falseAlarm" label="是否误报">
        <a-select v-model="formModel.falseAlarm"
                  :options="toOptions(FalseAlarmKey)"
                  placeholder="请选择是否误报"
                  allow-clear />
      </a-form-item>
      <!-- id -->
      <a-form-item field="id" label="id">
        <a-input-number v-model="formModel.id"
                        placeholder="请输入id"
                        hide-button
                        allow-clear />
      </a-form-item>
      <!-- 告警时间 -->
      <a-form-item field="createTimeRange" label="告警时间">
        <a-range-picker v-model="formModel.createTimeRange"
                        style="width: 100%;"
                        :time-picker-props="{ defaultValue: ['00:00:00', '23:59:59'] }"
                        show-time
                        format="YYYY-MM-DD HH:mm:ss" />
      </a-form-item>
    </query-header>
  </a-card>
  <!-- 表格 -->
  <alarm-event-table-base :source-type="AlarmSourceType.HOST"
                          :table-name="TableName"
                          :columns="originColumns"
                          :table-data="tableRenderData"
                          :loading="loading"
                          :form-model="formModel"
                          :pagination="pagination"
                          :show-clear-button="false"
                          @set-loading="setLoading"
                          @reload="reload"
                          @query="fetchTableData" />
</template>

<script lang="ts">
  export default {
    name: 'alarmEventTab'
  };
</script>

<script lang="ts" setup>
  import type { AlarmEventQueryRequest, AlarmEventQueryResponse } from '@/api/monitor/alarm-event';
  import { reactive, ref, onMounted } from 'vue';
  import { getAlarmEventPage } from '@/api/monitor/alarm-event';
  import useLoading from '@/hooks/loading';
  import columns from '../../alarm-event/types/table.columns';
  import { FalseAlarm, HandleStatusKey, FalseAlarmKey, MetricsMeasurementKey, AlarmLevelKey, AlarmSourceType } from '../../alarm-event/types/const';
  import { TableName } from '../types/const';
  import { useTablePagination } from '@/hooks/table';
  import { useDictStore, useCacheStore } from '@/store';
  import { useQueryOrder, DESC } from '@/hooks/query-order';
  import UserSelector from '@/components/user/user/selector/index.vue';
  import MonitorMetricsSelector from '@/components/monitor/metrics/selector/index.vue';
  import AlarmPolicySelector from '@/components/monitor/alarm-policy/selector/index.vue';
  import AlarmEventTableBase from '@/views/monitor/alarm-event/components/alarm-event-table-base.vue';

  const props = defineProps<{
    agentKey: string;
  }>();

  const pagination = useTablePagination();
  const queryOrder = useQueryOrder(TableName, DESC);
  const { loading, setLoading } = useLoading();
  const { toOptions } = useDictStore();
  const originColumns = columns.filter(s => s.dataIndex !== 'sourceInfo');

  const tableRenderData = ref<Array<AlarmEventQueryResponse>>([]);
  const formModel = reactive<AlarmEventQueryRequest>({
    id: undefined,
    agentKey: undefined,
    policyId: undefined,
    metricsId: undefined,
    metricsMeasurement: undefined,
    alarmLevel: undefined,
    falseAlarm: FalseAlarm.FALSE,
    handleStatus: undefined,
    handleRemark: undefined,
    handleUserId: undefined,
    createTimeRange: [],
  });

  // 重新加载
  const reload = () => {
    // 重新加载数据
    fetchTableData();
  };

  defineExpose({ reload });

  // 加载数据
  const doFetchTableData = async (request: AlarmEventQueryRequest) => {
    try {
      setLoading(true);
      const { data } = await getAlarmEventPage(queryOrder.markOrderly({ ...request, agentKey: props.agentKey }));
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

  onMounted(async () => {
    // 加载规则
    await useCacheStore().loadMonitorMetricsList();
    // 重新加载列表
    reload();
  });

</script>

<style lang="less" scoped>
</style>
