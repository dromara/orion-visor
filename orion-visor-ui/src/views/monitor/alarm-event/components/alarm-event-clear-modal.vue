<template>
  <a-modal v-model:visible="visible"
           modal-class="modal-form-large"
           title-align="start"
           title="清理操作日志"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           ok-text="清理"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handleOk"
           @close="handleClose">
    <a-spin class="full" :loading="loading">
      <a-form :model="formModel"
              label-align="right"
              :auto-label-width="true">
        <!-- 处理状态 -->
        <a-form-item field="handleStatus" label="处理状态">
          <a-select v-model="formModel.handleStatus"
                    :options="toOptions(HandleStatusKey)"
                    placeholder="请选择处理状态"
                    allow-clear />
        </a-form-item>
        <!-- 告警来源 -->
        <a-form-item field="agentKey" label="告警来源">
          <!-- 选择告警来源 -->
          <monitor-host-selector v-if="sourceType === AlarmSourceType.HOST"
                                 v-model="formModel.agentKey"
                                 placeholder="请选择告警来源"
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
        <a-form-item field="metricsMeasurement" label="数据集">
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
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'alarmEventClearModal'
  };
</script>

<script lang="ts" setup>
  import type { AlarmEventQueryRequest } from '@/api/monitor/alarm-event';
  import { clearMonitorAlarmEvent, getAlarmEventCount } from '@/api/monitor/alarm-event';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { Message, Modal } from '@arco-design/web-vue';
  import { useDictStore } from '@/store';
  import { maxClearLimit, HandleStatusKey, AlarmLevelKey, MetricsMeasurementKey, FalseAlarmKey, AlarmSourceType } from '../types/const';
  import { assignOmitRecord } from '@/utils';
  import UserSelector from '@/components/user/user/selector/index.vue';
  import MonitorMetricsSelector from '@/components/monitor/metrics/selector/index.vue';
  import AlarmPolicySelector from '@/components/monitor/alarm-policy/selector/index.vue';
  import MonitorHostSelector from '@/components/monitor/host/selector/index.vue';

  const { toOptions } = useDictStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const props = defineProps<{
    sourceType: string;
  }>();

  const defaultForm = (): AlarmEventQueryRequest => {
    return {
      id: undefined,
      sourceType: undefined,
      agentKey: undefined,
      policyId: undefined,
      metricsId: undefined,
      metricsMeasurement: undefined,
      alarmLevel: undefined,
      falseAlarm: undefined,
      handleStatus: undefined,
      handleRemark: undefined,
      handleUserId: undefined,
      createTimeRange: undefined,
      limit: maxClearLimit,
    };
  };

  const formModel = ref<AlarmEventQueryRequest>({});

  const emits = defineEmits(['clear']);

  // 打开
  const open = (record: AlarmEventQueryRequest) => {
    formModel.value = assignOmitRecord({ ...defaultForm(), ...record });
    setVisible(true);
  };

  defineExpose({ open });

  // 确定
  const handleOk = async () => {
    if (!formModel.value.limit) {
      Message.error('请输入数量限制');
      return false;
    }
    setLoading(true);
    try {
      // 获取总数量
      const { data } = await getAlarmEventCount(formModel.value);
      if (data) {
        // 清空
        doClear(data);
      } else {
        // 无数据
        Message.warning('当前条件未查询到数据');
      }
    } catch (e) {
    } finally {
      setLoading(false);
    }
    return false;
  };

  // 执行删除
  const doClear = (count: number) => {
    Modal.confirm({
      title: '清理确认',
      content: `确定要删除 ${count} 条数据吗? 确定后将立即删除且无法恢复!`,
      onOk: async () => {
        setLoading(true);
        try {
          // 调用清理
          const { data } = await clearMonitorAlarmEvent(formModel.value);
          Message.success(`已成功清理 ${data} 条数据`);
          emits('clear');
          handleClose();
        } catch (e) {
        } finally {
          setLoading(false);
        }
      }
    });
  };

  // 关闭
  const handleClose = () => {
    handleClear();
    setVisible(false);
  };

  // 清空
  const handleClear = () => {
    setLoading(false);
  };

</script>

<style lang="less" scoped>

</style>
