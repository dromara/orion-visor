<template>
  <!-- 表格 -->
  <a-card class="general-card table-card">
    <template #title>
      <!-- 左侧操作 -->
      <div class="table-left-bar-handle">
        <!-- 标题 -->
        <div class="table-title">
          告警事件列表
        </div>
      </div>
      <!-- 右侧操作 -->
      <div class="table-right-bar-handle">
        <a-space>
          <!-- 清理 -->
          <a-button v-if="showClearButton"
                    v-permission="['monitor:alarm-event:management:clear']"
                    status="danger"
                    @click="openClear">
            清理
            <template #icon>
              <icon-close />
            </template>
          </a-button>
          <!-- 处理告警 -->
          <a-button v-permission="['monitor:alarm-event:handle']"
                    type="primary"
                    :disabled="selectedKeys.length === 0"
                    @click="openHandle(selectedKeys)">
            处理告警
            <template #icon>
              <icon-play-arrow-fill />
            </template>
          </a-button>
          <!-- 标记误报 -->
          <a-button v-permission="['monitor:alarm-event:handle']"
                    type="primary"
                    :disabled="selectedKeys.length === 0"
                    @click="setFalseAlarm(selectedKeys, true)">
            标记误报
            <template #icon>
              <icon-bug />
            </template>
          </a-button>
          <!-- 删除 -->
          <a-button v-permission="['monitor:alarm-event:delete']"
                    type="secondary"
                    status="danger"
                    :disabled="selectedKeys.length === 0"
                    @click="deleteRows(selectedKeys)">
            删除
            <template #icon>
              <icon-delete />
            </template>
          </a-button>
          <!-- 调整 -->
          <table-adjust :columns="columns"
                        :columns-hook="columnsHook"
                        :query-order="queryOrder"
                        @query="$emit('query')" />
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
             :data="tableData"
             :pagination="pagination"
             :bordered="false"
             :column-resizable="true"
             :scroll="{ x: 'auto' }"
             @page-change="(page: number) => $emit('query', page, pagination.pageSize)"
             @page-size-change="(size: number) => $emit('query', 1, size)">
      <!-- 来源信息 -->
      <template #sourceInfo="{ record }">
        <div class="info-wrapper">
          <!-- 主机名称 -->
          <div v-if="record.sourceType === AlarmSourceType.HOST && record.sourceInfo?.name" class="info-item">
            <span class="info-label">主机名称</span>
            <span class="info-value text-copy text-ellipsis"
                  :title="record.sourceInfo.name"
                  @click="copy(record.sourceInfo.name, true)">
              {{ record.sourceInfo.name }}
            </span>
          </div>
          <!-- 主机地址 -->
          <div v-if="record.sourceType === AlarmSourceType.HOST && record.sourceInfo?.address" class="info-item">
            <span class="info-label">主机地址</span>
            <span class="info-value span-blue text-copy text-ellipsis"
                  :title="record.sourceInfo.address"
                  @click="copy(record.sourceInfo.address, true)">
              {{ record.sourceInfo.address }}
            </span>
          </div>
        </div>
      </template>
      <!-- 处理状态 -->
      <template #handleStatus="{ record }">
        <!-- 是否误报 -->
        <a-tag v-if="record.falseAlarm === FalseAlarm.TRUE" color="arcoblue">
          {{ getDictValue(FalseAlarmKey, record.falseAlarm) }}
        </a-tag>
        <!-- 处理状态 -->
        <a-tag v-else :color="getDictValue(HandleStatusKey, record.handleStatus, 'color')">
          {{ getDictValue(HandleStatusKey, record.handleStatus) }}
        </a-tag>
      </template>
      <!-- 告警级别 -->
      <template #alarmLevel="{ record }">
        <a-tag :color="getDictValue(AlarmLevelKey, record.alarmLevel, 'color')">
          {{ getDictValue(AlarmLevelKey, record.alarmLevel) }}
        </a-tag>
      </template>
      <!-- 指标数据集 -->
      <template #metricsMeasurement="{ record }">
        {{ getDictValue(MetricsMeasurementKey, record.metricsMeasurement) }}
      </template>
      <!-- 告警指标 -->
      <template #metricsId="{ record }">
        <div>
          <b class="span-blue">{{ getMetricsField(record.metricsId, 'value') }}</b>
          <br />
          <b>{{ getMetricsField(record.metricsId, 'name') }}</b>
        </div>
      </template>
      <!-- 告警标签 -->
      <template #alarmTags="{ record }">
        <component :is="extraTags(record)" />
      </template>
      <!-- 告警值 -->
      <template #alarmValue="{ record }">
        <b class="span-red">{{ formatMetricsValueUnit(record.alarmValue, record) }}</b>
      </template>
      <!-- 告警阈值 -->
      <template #alarmThreshold="{ record }">
        <b class="span-red">{{ getDictValue(TriggerConditionKey, record.triggerCondition) }} {{ formatMetricsValueUnit(record.alarmThreshold, record) }}</b>
      </template>
      <!-- 持续数据点 -->
      <template #consecutiveCount="{ record }">
        {{ record.consecutiveCount }} 次
      </template>
      <!-- 操作 -->
      <template #handle="{ record }">
        <div class="table-handle-wrapper">
          <!-- 处理 -->
          <a-button v-permission="['monitor:alarm-event:handle']"
                    type="text"
                    size="mini"
                    @click="openHandle([record.id])">
            处理
          </a-button>
          <!-- 更多 -->
          <a-dropdown trigger="hover" :popup-max-height="false">
            <a-button type="text" size="mini">
              更多
            </a-button>
            <template #content>
              <!-- 标记误报 -->
              <a-doption v-permission="['monitor:alarm-event:handle']"
                         @click="setFalseAlarm([record.id], false)">
                <span class="more-doption normal">标记误报</span>
              </a-doption>
              <!-- 删除 -->
              <a-doption v-permission="['monitor:alarm-event:delete']"
                         @click="deleteRows([record.id])">
                <span class="more-doption error">删除</span>
              </a-doption>
            </template>
          </a-dropdown>
        </div>
      </template>
    </a-table>
    <!-- 处理模态框-->
    <alarm-event-handle-modal ref="handleModal"
                              @handled="alarmHandled" />
    <!-- 清理模态框-->
    <alarm-event-clear-modal ref="clearModal"
                             :source-type="sourceType"
                             @clear="emits('reload')" />
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'alarmEventTableBase'
  };
</script>

<script lang="ts" setup>
  import type { PaginationProps } from '@arco-design/web-vue';
  import type { MetricsQueryResponse } from '@/api/monitor/metrics';
  import type { AlarmEventQueryRequest, AlarmEventQueryResponse, AlarmEventHandleRequest } from '@/api/monitor/alarm-event';
  import { h, ref } from 'vue';
  import { batchDeleteAlarmEvent, setAlarmEventFalse } from '@/api/monitor/alarm-event';
  import { Message, Modal, Space, Tag } from '@arco-design/web-vue';
  import {
    FalseAlarm,
    HandleStatusKey,
    FalseAlarmKey,
    MetricsMeasurementKey,
    AlarmLevelKey,
    TriggerConditionKey,
    AlarmSourceType
  } from '../types/const';
  import { useRowSelection, useTableColumns } from '@/hooks/table';
  import { copy } from '@/hooks/copy';
  import { useQueryOrder, DESC } from '@/hooks/query-order';
  import { useDictStore, useCacheStore, useUserStore } from '@/store';
  import { MetricsUnit, MetricUnitFormatter } from '@/utils/metrics';
  import TableAdjust from '@/components/app/table-adjust/index.vue';
  import AlarmEventClearModal from './alarm-event-clear-modal.vue';
  import AlarmEventHandleModal from './alarm-event-handle-modal.vue';

  const props = defineProps<{
    tableName: string;
    sourceType: string;
    columns: any[];
    tableData: AlarmEventQueryResponse[];
    loading: boolean;
    formModel: AlarmEventQueryRequest;
    pagination: PaginationProps;
    showClearButton?: boolean;
  }>();

  const emits = defineEmits<{
    reload: [];
    setLoading: [loading: boolean];
    query: [page?: number, pageSize?: number];
  }>();

  const rowSelection = useRowSelection();
  const userStore = useUserStore();
  const cacheStore = useCacheStore();
  const queryOrder = useQueryOrder(props.tableName, DESC);
  const { tableColumns, columnsHook } = useTableColumns(props.tableName, props.columns);
  const { getDictValue } = useDictStore();

  const handleModal = ref();
  const clearModal = ref();
  const selectedKeys = ref<Array<number>>([]);

  // 告警处理回调
  const alarmHandled = (request: Required<AlarmEventHandleRequest>) => {
    props.tableData.filter(s => (request.idList || []).includes(s.id)).forEach(s => {
      s.handleTime = request.handleTime;
      s.handleStatus = request.handleStatus;
      s.handleRemark = request.handleRemark;
      s.handleUserId = userStore.id as number;
      s.handleUsername = userStore.username as string;
    });
    selectedKeys.value = [];
  };

  // 获取指标名称
  const getMetricsField = (metricsId: number, field: string) => {
    return (cacheStore.monitorMetrics as Array<MetricsQueryResponse> || []).find(m => m.id === metricsId)?.[field];
  };

  // 提取标签
  const extraTags = (record: AlarmEventQueryResponse) => {
    try {
      const parse = JSON.parse(record.alarmTags);
      const children = Object.entries(parse).map(([key, value]) => {
        return h(Tag, { title: `${key}: ${value}` }, { default: () => `${key}: ${value}` });
      });
      return h(Space, {}, { default: () => children });
    } catch (e) {
      return h('span', {}, '');
    }
  };

  // 格式化指标单位
  const formatMetricsValueUnit = (value: number, record: AlarmEventQueryResponse) => {
    try {
      const unit = getMetricsField(record.metricsId, 'unit');
      const suffix = getMetricsField(record.metricsId, 'suffix');
      return MetricUnitFormatter[unit as keyof typeof MetricsUnit].format(value, { suffix });
    } catch (e) {
      return value;
    }
  };

  // 标记误报
  const setFalseAlarm = async (idList: Array<number>, clear: boolean) => {
    Modal.confirm({
      title: '误报确认',
      content: `确定要标记这 ${idList.length} 条数据为误报吗?`,
      onOk: async () => {
        try {
          emits('setLoading', true);
          // 调用设置误报
          await setAlarmEventFalse({ idList });
          Message.success('已标记为误报');
          if (clear) {
            selectedKeys.value = [];
          }
          props.tableData.filter(s => idList.includes(s.id)).forEach(s => {
            s.falseAlarm = FalseAlarm.TRUE;
          });
        } catch (e) {
        } finally {
          emits('setLoading', false);
        }
      }
    });
  };

  // 删除数据
  const deleteRows = async (idList: Array<number>) => {
    Modal.confirm({
      title: '确认删除',
      content: `确定要删除这 ${idList.length} 条数据吗?`,
      onOk: async () => {
        try {
          emits('setLoading', true);
          // 调用删除接口
          await batchDeleteAlarmEvent(idList);
          Message.success(`成功删除 ${idList.length} 条数据`);
          selectedKeys.value = [];
          // 重新加载
          emits('query');
        } catch (e) {
        } finally {
          emits('setLoading', false);
        }
      }
    });
  };

  // 打开处理
  const openHandle = (idList: Array<number>) => {
    handleModal.value.open(idList);
  };

  // 打开清理
  const openClear = () => {
    clearModal.value.open(props.formModel);
  };

</script>

<style lang="less" scoped>
  .info-wrapper {
    padding: 4px 0;

    .info-item {
      display: flex;

      &:not(:last-child) {
        margin-bottom: 4px;
      }

      .info-label {
        width: 60px;
        margin-right: 8px;
        user-select: none;
        font-weight: 600;

        &::after {
          content: ':';
        }
      }

      .info-value {
        width: calc(100% - 68px);
      }
    }
  }
</style>
