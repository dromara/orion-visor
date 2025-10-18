<template>
  <a-drawer v-model:visible="visible"
            :title="title"
            :width="590"
            :mask-closable="false"
            :unmount-on-close="true"
            :ok-button-props="{ disabled: loading }"
            :cancel-button-props="{ disabled: loading }"
            :on-before-ok="handleOk"
            @cancel="handleClose">
    <a-spin class="full drawer-form-large" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :auto-label-width="true"
              :rules="formRules">
        <!-- 监控指标 -->
        <a-form-item field="metricsId" label="监控指标">
          <monitor-metrics-selector v-model="formModel.metricsId"
                                    :class="[ hasTags ? 'metrics-selector-has-tag' : 'metrics-selector-no-tag']"
                                    placeholder="请选择监控指标"
                                    allow-clear />
          <!-- 添加标签 -->
          <a-button v-if="hasTags"
                    title="添加标签"
                    :disabled="formModel.allEffect === 1"
                    @click="addTag">
            <template #icon>
              <icon-tags />
            </template>
          </a-button>
        </a-form-item>
        <!-- tags -->
        <template v-for="(tag, index) in tags">
          <a-form-item v-if="formModel.allEffect === 0"
                       :field="'tag-' + (index + 1)"
                       :label="'指标标签-' + (index + 1)">
            <a-space :size="12" class="tag-wrapper">
              <!-- 标签名称 -->
              <a-input v-model="tag.key"
                       style="width: 108px;"
                       placeholder="标签名称" />
              <!-- 标签值 -->
              <a-select v-model="tag.value"
                        class="tag-values"
                        style="width: 260px"
                        :max-tag-count="2"
                        :options="measurementTags[measurement] || []"
                        placeholder="输入或选择标签值"
                        multiple
                        allow-create>
                <template #empty>
                  <a-empty>
                    请输入标签值
                  </a-empty>
                </template>
              </a-select>
              <!-- 移除 -->
              <a-button title="移除"
                        style="width: 32px"
                        @click="removeTag(index)">
                <template #icon>
                  <icon-minus />
                </template>
              </a-button>
            </a-space>
          </a-form-item>
        </template>
        <a-row>
          <!-- 规则开关 -->
          <a-col :span="12" style="padding-right: 24px;">
            <a-form-item field="ruleSwitch"
                         label="规则开关"
                         hide-asterisk>
              <a-switch v-model="formModel.ruleSwitch"
                        type="round"
                        :checked-value="1"
                        :unchecked-value="0" />
            </a-form-item>
          </a-col>
          <!-- 全部生效 -->
          <a-col v-if="hasTags" :span="12">
            <a-form-item field="allEffect"
                         label="全部生效"
                         tooltip="开启后则忽略标签, 并生效与已配置标签的规则 (通常用于默认策略)"
                         hide-asterisk>
              <a-switch v-model="formModel.allEffect"
                        type="round"
                        :checked-value="1"
                        :unchecked-value="0" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <!-- 持续数据点 -->
          <a-col :span="12" style="padding-right: 24px;">
            <a-form-item field="silencePeriod"
                         label="持续数据点"
                         hide-asterisk>
              <a-input-number v-model="formModel.consecutiveCount"
                              :min="0"
                              :max="100"
                              placeholder="持续数据点"
                              hide-button
                              allow-clear>
                <template #append>
                  <span>个</span>
                </template>
              </a-input-number>
            </a-form-item>
          </a-col>
          <!-- 静默时间 -->
          <a-col :span="12">
            <a-form-item field="silencePeriod"
                         label="静默时间"
                         tooltip="再次发生告警后沉默的时间"
                         hide-asterisk>
              <a-input-number v-model="formModel.silencePeriod"
                              :min="0"
                              placeholder="请输入静默时间"
                              hide-button
                              allow-clear>
                <template #append>
                  <span>分钟</span>
                </template>
              </a-input-number>
            </a-form-item>
          </a-col>
        </a-row>
        <!-- 告警条件 -->
        <a-row>
          <a-col :span="7">
            <a-form-item field="level"
                         label="告警条件"
                         class="alarm-level-select">
              <a-select v-model="formModel.level"
                        style="padding: 0;"
                        :options="toOptions(LevelKey)"
                        :bordered="false"
                        placeholder="级别">
                <template #label="{ data: { label, value } }">
                  <a-tag :color="getDictValue(LevelKey, value,'color')">{{ label }}</a-tag>
                </template>
                <template #option="{ data: { label, value } }">
                  <a-tag style="padding: 0 3px;" :color="getDictValue(LevelKey, value,'color')">{{ label }}</a-tag>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <!-- 告警条件 -->
          <a-col :span="6">
            <a-form-item field="triggerCondition"
                         class="condition-select"
                         hide-label>
              <a-select v-model="formModel.triggerCondition"
                        :options="toOptions(TriggerConditionKey)"
                        placeholder="请选择告警条件" />
            </a-form-item>
          </a-col>
          <!-- 触发阈值 -->
          <a-col :span="11">
            <a-form-item field="threshold"
                         style="padding-left: 16px;"
                         hide-label>
              <a-input-number v-model="formModel.threshold"
                              :precision="4"
                              placeholder="触发阈值"
                              hide-button
                              allow-clear>
                <template v-if="metricsUnit" #append>
                  {{ metricsUnit }}
                </template>
              </a-input-number>
            </a-form-item>
          </a-col>
        </a-row>
        <!-- 规则描述 -->
        <a-form-item field="description" label="规则描述">
          <a-textarea v-model="formModel.description"
                      :auto-size="{ minRows: 4, maxRows: 4 }"
                      placeholder="请输入规则描述"
                      allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'alarmRuleFormDrawer'
  };
</script>

<script lang="ts" setup>
  import type { AlarmRuleUpdateRequest } from '@/api/monitor/alarm-rule';
  import type { MetricsQueryResponse } from '@/api/monitor/metrics';
  import type { RuleTag } from '../types/const';
  import type { FormHandle } from '@/types/form';
  import { ref, computed, watch } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { MetricsUnitKey, MeasurementKey } from '../types/const';
  import { assignOmitRecord } from '@/utils';
  import { TriggerConditionKey, LevelKey, DefaultCondition, DefaultLevel, } from '../types/const';
  import { createAlarmRule, updateAlarmRule } from '@/api/monitor/alarm-rule';
  import { Message } from '@arco-design/web-vue';
  import { useDictStore, useCacheStore } from '@/store';
  import { getMonitorHostTags } from '@/api/monitor/monitor-host';
  import MonitorMetricsSelector from '@/components/monitor/metrics/selector/index.vue';

  const emits = defineEmits(['added', 'updated']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();
  const { getDictValue, toOptions } = useDictStore();

  const title = ref<string>();
  const formHandle = ref<FormHandle>('add');
  const formRef = ref<any>();
  const formModel = ref<AlarmRuleUpdateRequest>({});
  const tags = ref<Array<RuleTag>>([]);
  const hasTags = ref(false);
  const measurement = ref('');
  const measurementTags = ref<Record<string, string[]>>({});

  const defaultForm = (): AlarmRuleUpdateRequest => {
    return {
      id: undefined,
      policyId: undefined,
      metricsId: undefined,
      tags: undefined,
      level: DefaultLevel,
      ruleSwitch: 1,
      allEffect: 1,
      triggerCondition: DefaultCondition,
      threshold: undefined,
      consecutiveCount: 1,
      silencePeriod: 0,
      description: undefined,
    };
  };

  // 检查是否有 tags
  watch(() => formModel.value.metricsId, (metricsId) => {
    if (!metricsId) {
      hasTags.value = false;
      return;
    }
    // 获取数据集
    const measurementValue = (cacheStore.monitorMetrics as Array<MetricsQueryResponse> || []).find(m => m.id === metricsId)?.measurement;
    if (!measurementValue) {
      hasTags.value = false;
      return;
    }
    measurement.value = measurementValue;
    // 获取标签
    const value = getDictValue(MeasurementKey, measurementValue, 'hasTags');
    if (value === true) {
      hasTags.value = true;
      // 加载全部标签
      loadTags();
    } else {
      hasTags.value = false;
    }
  });

  // 打开新增
  const openAdd = (policyId: number) => {
    title.value = '添加监控告警规则';
    formHandle.value = 'add';
    renderForm({ ...defaultForm(), policyId });
    setVisible(true);
  };

  // 打开复制
  const openCopy = (record: any) => {
    title.value = '添加监控告警规则';
    formHandle.value = 'add';
    renderForm({ ...defaultForm(), ...record, id: undefined });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改监控告警规则';
    formHandle.value = 'update';
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    formModel.value = assignOmitRecord({ ...defaultForm(), ...record }, 'tags');
    if (record.tags) {
      tags.value = JSON.parse(record.tags);
    } else {
      tags.value = [];
    }
  };

  defineExpose({ openAdd, openCopy, openUpdate });

  // 添加标签
  const addTag = () => {
    const hasNameTag = tags.value.some(s => s.key === 'name');
    tags.value.push({ key: hasNameTag ? '' : 'name', value: [] });
  };

  // 移除标签
  const removeTag = (index: number) => {
    tags.value.splice(index, 1);
  };

  // 指标单位
  const metricsUnit = computed(() => {
    const metricsId = formModel.value.metricsId;
    if (!metricsId) {
      return '';
    }
    // 读取指标单位
    const unit = (cacheStore.monitorMetrics as Array<MetricsQueryResponse> || []).find(m => m.id === metricsId)?.unit;
    if (!unit) {
      return '';
    }
    return getDictValue(MetricsUnitKey, unit, 'alarmUnit');
  });

  // 加载全部标签
  const loadTags = () => {
    const tags = measurementTags.value[measurement.value];
    if (tags) {
      return;
    }
    // 加载标签
    getMonitorHostTags({
      measurement: measurement.value,
      policyId: formModel.value.policyId,
    }).then(({ data }) => {
      measurementTags.value[measurement.value as any] = data;
    });
  };

  // 确定
  const handleOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      if (!hasTags.value) {
        // 无 tag
        formModel.value.allEffect = 1;
      } else {
        // 有 tag
        if (formModel.value.allEffect === 1) {
          // 全部生效
          tags.value = [];
        } else {
          // 检查 tag
          if (!tags.value.length) {
            Message.error('请选择全部生效或添加对应的标签');
            return false;
          }
          for (let tag of tags.value) {
            if (!tag.key) {
              Message.error('请输入标签名称');
              return false;
            }
            if (!tag.value) {
              Message.error('请输入标签值');
              return false;
            }
          }
        }
      }
      if (formHandle.value == 'add') {
        // 新增
        await createAlarmRule({
          ...formModel.value,
          tags: JSON.stringify(tags.value)
        });
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateAlarmRule({
          ...formModel.value,
          tags: JSON.stringify(tags.value)
        });
        Message.success('修改成功');
        emits('updated');
      }
      handleClose();
      return true;
    } catch (e) {
      return false;
    } finally {
      setLoading(false);
    }
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
  :deep(.metrics-selector-no-tag) {
    width: 100%;
  }

  :deep(.metrics-selector-has-tag) {
    width: calc(100% - 42px);
    margin-right: 12px;
  }

  .tag-wrapper {
    width: 100%;
    justify-content: space-between;
  }

  .alarm-level-select, .condition-select {

    :deep(.arco-select-view-suffix) {
      display: none !important;
    }
  }

  :deep(.tag-values .arco-select-view-inner) {
    flex-wrap: nowrap !important;
  }

</style>
