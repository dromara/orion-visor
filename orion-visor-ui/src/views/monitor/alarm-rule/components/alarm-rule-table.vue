<template>
  <!-- 内容部分 -->
  <div class="container-content">
    <!-- 指标类型 -->
    <a-card class="general-card table-search-card measurement-card">
      <a-tabs v-model:active-key="measurement"
              direction="vertical"
              type="rounded"
              :hide-content="true"
              @change="reload">
        <a-tab-pane key="" title="全部" />
        <a-tab-pane v-for="item in toOptions(MeasurementKey)"
                    :key="item.value as string"
                    :title="item.label" />
      </a-tabs>
    </a-card>
    <!-- 表格 -->
    <a-card class="general-card table-card">
      <template #title>
        <!-- 左侧操作 -->
        <div class="table-left-bar-handle">
          <!-- 标题 -->
          <div class="table-title">
            告警规则 - {{ policyName }}
          </div>
        </div>
        <!-- 右侧操作 -->
        <div class="table-right-bar-handle">
          <a-space>
            <!-- 新增 -->
            <a-button v-permission="['monitor:alarm-policy:update-rule']"
                      type="primary"
                      @click="emits('openAdd', policyId)">
              新增
              <template #icon>
                <icon-plus />
              </template>
            </a-button>
            <!-- 刷新 -->
            <a-button @click="doFetchTableData">
              <template #icon>
                <icon-refresh />
              </template>
            </a-button>
            <!-- 调整 -->
            <table-adjust :columns="columns"
                          :columns-hook="columnsHook"
                          @query="doFetchTableData" />
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
               :pagination="false"
               :bordered="false"
               :column-resizable="true">
        <!-- 指标标签 -->
        <template #tags="{ record }">
          <a-tag v-if="record.allEffect === 1">
            全部
          </a-tag>
          <a-space v-else-if="record.allEffect === 0">
            <a-tag v-for="tag in extraTags(record.tags)"
                   class="text-ellipsis"
                   style="display: inline-block; max-width: 100px;">
              {{ tag }}
            </a-tag>
          </a-space>
        </template>
        <!-- 告警级别 -->
        <template #level="{ record }">
          <a-tag :color="getDictValue(LevelKey, record.level, 'color')">
            {{ getDictValue(LevelKey, record.level) }}
          </a-tag>
        </template>
        <!-- 告警条件 -->
        <template #triggerCondition="{ record }">
          <span>
            <!-- 指标名称 -->
            <span class="mr4">{{ getMetricsField(record.metricsId, 'name') }}</span>
            <!-- 条件 -->
            <span class="mr4">{{ getDictValue(TriggerConditionKey, record.triggerCondition) }}</span>
            <!-- 阈值 -->
            <b>{{ record.threshold }}{{ getDictValue(MetricsUnitKey, getMetricsField(record.metricsId, 'unit'), 'alarmUnit') }}</b>
          </span>
        </template>
        <!-- 静默时间 -->
        <template #silencePeriod="{ record }">
          {{ record.silencePeriod }} 分钟
        </template>
        <!-- 持续数据点 -->
        <template #consecutiveCount="{ record }">
          {{ record.consecutiveCount }} 个
        </template>
        <!-- 规则开关 -->
        <template #ruleSwitch="{ record }">
          <a-switch v-model="record.ruleSwitch"
                    type="round"
                    :disabled="!hasPermission('monitor:alarm-policy:update-rule')"
                    :checked-value="1"
                    :unchecked-value="0"
                    @change="(s: any) => handleSwitchChange(record, s)" />
        </template>
        <!-- 操作 -->
        <template #handle="{ record }">
          <div class="table-handle-wrapper">
            <!-- 修改 -->
            <a-button v-permission="['monitor:alarm-policy:update-rule']"
                      type="text"
                      size="mini"
                      @click="emits('openUpdate', record)">
              修改
            </a-button>
            <!-- 复制 -->
            <a-button v-permission="['monitor:alarm-policy:update-rule']"
                      type="text"
                      size="mini"
                      @click="emits('openCopy', record)">
              复制
            </a-button>
            <!-- 删除 -->
            <a-popconfirm content="确认删除这条记录吗?"
                          position="left"
                          type="warning"
                          @ok="deleteRow(record)">
              <a-button v-permission="['monitor:alarm-policy:update-rule']"
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
  </div>
</template>

<script lang="ts">
  export default {
    name: 'alarmRuleTable'
  };
</script>

<script lang="ts" setup>
  import type { AlarmRuleQueryResponse } from '@/api/monitor/alarm-rule';
  import type { MetricsQueryResponse } from '@/api/monitor/metrics';
  import { ref, onMounted } from 'vue';
  import { deleteAlarmRule, getAlarmRuleList, updateAlarmRuleSwitch } from '@/api/monitor/alarm-rule';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import usePermission from '@/hooks/permission';
  import columns from '../types/table.columns';
  import { useRoute } from 'vue-router';
  import { useDictStore, useCacheStore } from '@/store';
  import { useTableColumns } from '@/hooks/table';
  import { TriggerConditionKey, LevelKey, TableName, MeasurementKey, MetricsUnitKey } from '../types/const';
  import TableAdjust from '@/components/app/table-adjust/index.vue';

  const emits = defineEmits(['openAdd', 'openUpdate', 'openCopy']);

  const { loading, setLoading } = useLoading();
  const { hasPermission } = usePermission();
  const cacheStore = useCacheStore();
  const { toOptions, getDictValue } = useDictStore();
  const { tableColumns, columnsHook } = useTableColumns(TableName, columns);

  const policyId = ref<number>(0);
  const policyName = ref<string>('');
  const measurement = ref<string>('');
  const tableRenderData = ref<Array<AlarmRuleQueryResponse>>([]);

  // 删除当前行
  const deleteRow = async (record: AlarmRuleQueryResponse) => {
    try {
      setLoading(true);
      // 调用删除接口
      await deleteAlarmRule(record.id);
      Message.success('删除成功');
      // 重新加载
      reload();
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 提取标签
  const extraTags = (tags: string) => {
    if (!tags) {
      return [];
    }
    try {
      return JSON.parse(tags).map((s: any) => `${s.key}: ${s.value}`);
    } catch (e) {
      return [];
    }
  };

  // 获取指标名称
  const getMetricsField = (metricsId: number, field: string) => {
    return (cacheStore.monitorMetrics as Array<MetricsQueryResponse> || []).find(m => m.id === metricsId)?.[field];
  };

  // 切换规则开关
  const handleSwitchChange = async (record: AlarmRuleQueryResponse, checked: number) => {
    try {
      setLoading(true);
      await updateAlarmRuleSwitch({
        id: record.id,
        ruleSwitch: checked
      });
      record.ruleSwitch = checked;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 重新加载
  const reload = () => {
    // 重新加载数据
    doFetchTableData();
  };

  defineExpose({ reload });

  // 加载数据
  const doFetchTableData = async () => {
    try {
      setLoading(true);
      const { data } = await getAlarmRuleList(policyId.value, measurement.value);
      tableRenderData.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  onMounted(() => {
    try {
      // 解析参数
      const route = useRoute();
      policyId.value = Number.parseInt(route.query.id as string);
      policyName.value = route.query.name as string;
      // 重新加载数据
      reload();
    } catch (e) {
    }
  });

</script>

<style lang="less" scoped>
  @measurement-card-width: 120px;
  .container-content {
    display: flex;
  }

  .measurement-card {
    width: @measurement-card-width;
    margin: 0 16px 0 0 !important;
    user-select: none;
  }

  .table-card {
    width: calc(100% - @measurement-card-width - 16px);
  }
</style>
