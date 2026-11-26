<template>
  <a-modal v-model:visible="visible"
           modal-class="modal-form-large"
           title-align="start"
           title="处理告警"
           :align-center="false"
           :draggable="true"
           :mask-closable="false"
           :unmount-on-close="true"
           ok-text="处理"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handleOk"
           @close="handleClose">
    <a-spin class="full" :loading="loading">
      <a-form ref="formRef"
              :model="formModel"
              label-align="right"
              :rules="handleRules"
              :auto-label-width="true">
        <!-- 处理状态 -->
        <a-form-item field="handleStatus" label="处理状态">
          <a-select v-model="formModel.handleStatus"
                    :options="toOptions(HandleStatusKey)"
                    placeholder="请选择处理状态"
                    allow-clear />
        </a-form-item>
        <!-- 处理时间 -->
        <a-form-item field="handleTime" label="处理时间">
          <a-date-picker v-model="formModel.handleTime"
                         style="width: 100%"
                         placeholder="请选择处理时间"
                         show-time
                         allow-clear />
        </a-form-item>
        <!-- 处理备注 -->
        <a-form-item field="handleRemark" label="处理备注">
          <a-textarea v-model="formModel.handleRemark"
                      :auto-size="{ minRows: 4, maxRows: 4 }"
                      placeholder="请输入处理备注"
                      allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'alarmEventHandleModal'
  };
</script>

<script lang="ts" setup>
  import type { AlarmEventHandleRequest } from '@/api/monitor/alarm-event';
  import { ref } from 'vue';
  import { handleAlarmEvent } from '@/api/monitor/alarm-event';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { Message } from '@arco-design/web-vue';
  import { useDictStore } from '@/store';
  import { HandleStatusKey } from '../types/const';
  import { assignOmitRecord } from '@/utils';
  import { handleRules } from '../types/form.rules';

  const { toOptions } = useDictStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const defaultForm = (): AlarmEventHandleRequest => {
    return {
      idList: undefined,
      handleStatus: undefined,
      handleRemark: undefined,
      handleTime: Date.now(),
    };
  };

  const formRef = ref();
  const formModel = ref<AlarmEventHandleRequest>({});

  const emits = defineEmits(['handled']);

  // 打开
  const open = (idList: Array<number>) => {
    formModel.value = assignOmitRecord({ ...defaultForm(), idList });
    setVisible(true);
  };

  defineExpose({ open });

  // 确定
  const handleOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      // 处理
      await handleAlarmEvent(formModel.value);
      Message.success('已处理');
      emits('handled', { ...formModel.value });
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

</style>
