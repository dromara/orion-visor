<template>
  <a-modal v-model:visible="visible"
           modal-class="modal-form-large"
           title-align="start"
           :title="title"
           :top="80"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <a-spin class="full" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :auto-label-width="true"
              :rules="formRules">
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
                    :disabled="formHandle === 'update'"
                    placeholder="请选择数据集"
                    allow-clear />
        </a-form-item>
        <!-- 指标项 -->
        <a-form-item field="value" label="指标项">
          <a-input v-model="formModel.value"
                   placeholder="请输入指标项"
                   allow-clear />
        </a-form-item>
        <!-- 单位 -->
        <a-form-item field="unit" label="单位">
          <a-select v-model="formModel.unit"
                    :options="toOptions(MetricsUnitKey)"
                    placeholder="请选择单位"
                    allow-clear />
        </a-form-item>
        <!-- 后缀文本 -->
        <a-form-item v-if="formModel.unit === MetricsUnit.TEXT"
                     field="suffix"
                     label="后缀文本">
          <a-input v-model="formModel.suffix"
                   placeholder="请输入后缀文本"
                   allow-clear />
        </a-form-item>
        <!-- 指标描述 -->
        <a-form-item field="description" label="指标描述">
          <a-textarea v-model="formModel.description"
                      :auto-size="{ minRows: 3, maxRows: 3 }"
                      placeholder="请输入指标描述"
                      allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'metricsFormModal'
  };
</script>

<script lang="ts" setup>
  import type { FormHandle } from '@/types/form';
  import type { MetricsUpdateRequest } from '@/api/monitor/metrics';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { MeasurementKey, MetricsUnitKey } from '../types/const';
  import { createMetrics, updateMetrics } from '@/api/monitor/metrics';
  import { Message } from '@arco-design/web-vue';
  import { useDictStore } from '@/store';
  import { MetricsUnit } from '@/utils/metrics';
  import { assignOmitRecord } from '@/utils';

  const emits = defineEmits(['added', 'updated']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const { toOptions } = useDictStore();

  const title = ref<string>();
  const formHandle = ref<FormHandle>('add');
  const formRef = ref<any>();
  const formModel = ref<MetricsUpdateRequest>({});

  const defaultForm = (): MetricsUpdateRequest => {
    return {
      id: undefined,
      name: undefined,
      measurement: undefined,
      value: undefined,
      unit: MetricsUnit.NONE,
      suffix: undefined,
      description: undefined,
    };
  };

  // 打开新增
  const openAdd = () => {
    title.value = '添加监控指标';
    formHandle.value = 'add';
    formModel.value = assignOmitRecord({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改监控指标';
    formHandle.value = 'update';
    formModel.value = assignOmitRecord({ ...defaultForm(), ...record });
    setVisible(true);
  };

  defineExpose({ openAdd, openUpdate });

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      // 非文本后缀置空
      if (MetricsUnit.TEXT !== formModel.value.unit) {
        formModel.value.suffix = '';
      }
      if (formHandle.value === 'add') {
        // 新增
        await createMetrics(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateMetrics(formModel.value);
        Message.success('修改成功');
        emits('updated');
      }
      // 清空
      handlerClear();
    } catch (e) {
      return false;
    } finally {
      setLoading(false);
    }
  };

  // 关闭
  const handleClose = () => {
    handlerClear();
  };

  // 清空
  const handlerClear = () => {
    setLoading(false);
  };

</script>

<style lang="less" scoped>

</style>
