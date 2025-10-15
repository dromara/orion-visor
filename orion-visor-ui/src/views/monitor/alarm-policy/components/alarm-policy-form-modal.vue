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
        <!-- 策略名称 -->
        <a-form-item field="name" label="策略名称">
          <a-input v-model="formModel.name"
                   placeholder="请输入策略名称"
                   allow-clear />
        </a-form-item>
        <!-- 策略描述 -->
        <a-form-item field="description" label="策略描述">
          <a-textarea v-model="formModel.description"
                      placeholder="请输入策略描述"
                      :auto-size="{ minRows: 3, maxRows: 3}"
                      allow-clear />
        </a-form-item>
        <!-- 通知渠道 -->
        <a-form-item field="notifyIdList" label="通知渠道">
          <notify-template-selector v-model="formModel.notifyIdList"
                                    biz-type="ALARM"
                                    multiple
                                    @change="setChangeNotify(true)" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'alarmPolicyFormModal'
  };
</script>

<script lang="ts" setup>
  import type { AlarmPolicyUpdateRequest } from '@/api/monitor/alarm-policy';
  import type { FormHandle } from '@/types/form';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { assignOmitRecord } from '@/utils';
  import { createAlarmPolicy, updateAlarmPolicy, copyAlarmPolicy, getAlarmPolicy } from '@/api/monitor/alarm-policy';
  import { useToggle } from '@vueuse/core';
  import { Message } from '@arco-design/web-vue';
  import { AlarmPolicyType } from '../types/const';
  import NotifyTemplateSelector from '@/components/system/notify-template/selector/index.vue';

  const emits = defineEmits(['added', 'updated']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const [changeNotify, setChangeNotify] = useToggle<boolean>(false);

  const title = ref<string>();
  const formHandle = ref<FormHandle>('add');
  const formRef = ref<any>();
  const formModel = ref<AlarmPolicyUpdateRequest>({});

  const defaultForm = (): AlarmPolicyUpdateRequest => {
    return {
      id: undefined,
      type: AlarmPolicyType.HOST,
      name: undefined,
      description: undefined,
      notifyIdList: [],
    };
  };

  // 打开新增
  const openAdd = () => {
    title.value = '添加告警策略';
    formHandle.value = 'add';
    formModel.value = defaultForm();
    setVisible(true);
  };

  // 打开修改
  const openUpdate = async (record: any) => {
    title.value = '修改告警策略';
    formHandle.value = 'update';
    formModel.value = defaultForm();
    setVisible(true);
    await renderForm(record.id);
  };

  // 打开修改
  const openCopy = async (record: any) => {
    title.value = '复制告警策略';
    formHandle.value = 'copy';
    formModel.value = defaultForm();
    setVisible(true);
    await renderForm(record.id);
  };

  // 渲染表单
  const renderForm = async (id: number) => {
    try {
      setLoading(true);
      const { data } = await getAlarmPolicy(id);
      formModel.value = assignOmitRecord(data);
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  defineExpose({ openAdd, openUpdate, openCopy });

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      if (formHandle.value === 'copy') {
        // 复制
        await copyAlarmPolicy(formModel.value);
        Message.success('复制成功');
        emits('added');
      } else if (formHandle.value === 'add') {
        // 新增
        await createAlarmPolicy(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateAlarmPolicy({ ...formModel.value, updateNotify: changeNotify.value });
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
    setChangeNotify(false);
  };

</script>

<style lang="less" scoped>

</style>
