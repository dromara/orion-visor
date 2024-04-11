<template>
  <a-drawer v-model:visible="visible"
            :title="title"
            :width="470"
            :mask-closable="false"
            :unmount-on-close="true"
            :ok-button-props="{ disabled: loading }"
            :cancel-button-props="{ disabled: loading }"
            :on-before-ok="handlerOk"
            @cancel="handleClose">
    <a-spin class="full modal-form" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :label-col-props="{ span: 5 }"
              :wrapper-col-props="{ span: 18 }"
              :rules="formRules">
        <!-- 任务名称 -->
        <a-form-item field="name" label="任务名称">
          <a-input v-model="formModel.name"
                   placeholder="请输入任务名称"
                   allow-clear />
        </a-form-item>
        <!-- 执行序列 -->
        <a-form-item field="execSeq" label="执行序列">
          <a-input-number v-model="formModel.execSeq"
                          placeholder="请输入执行序列"
                          hide-button />
        </a-form-item>
        <!-- cron 表达式 -->
        <a-form-item field="expression" label="cron 表达式">
          <a-input v-model="formModel.expression"
                   placeholder="请输入cron 表达式"
                   allow-clear />
        </a-form-item>
        <!-- 超时时间 -->
        <a-form-item field="timeout" label="超时时间">
          <a-input-number v-model="formModel.timeout"
                          placeholder="请输入超时时间"
                          hide-button />
        </a-form-item>
        <!-- 执行命令 -->
        <a-form-item field="command" label="执行命令">
          <a-input v-model="formModel.command"
                   placeholder="请输入执行命令"
                   allow-clear />
        </a-form-item>
        <!-- 命令参数 -->
        <a-form-item field="parameterSchema" label="命令参数">
          <a-input v-model="formModel.parameterSchema"
                   placeholder="请输入命令参数"
                   allow-clear />
        </a-form-item>
        <!-- 任务状态 -->
        <a-form-item field="status" label="任务状态">
          <a-select v-model="formModel.status"
                    :options="toOptions(execJobStatusKey)"
                    placeholder="任务状态" />
        </a-form-item>
        <!-- 最近执行id -->
        <a-form-item field="recentLogId" label="最近执行id">
          <a-input-number v-model="formModel.recentLogId"
                          placeholder="请输入最近执行id"
                          hide-button />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'execJobDetailDrawer'
  };
</script>

<script lang="ts" setup>
  import type { ExecJobUpdateRequest } from '@/api/exec/exec-job';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { execJobStatusKey } from '../types/const';
  import { createExecJob, updateExecJob } from '@/api/exec/exec-job';
  import { Message } from '@arco-design/web-vue';
  import { useDictStore } from '@/store';

  const emits = defineEmits(['added', 'updated']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const { toOptions } = useDictStore();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);
  const formRef = ref<any>();
  const formModel = ref<ExecJobUpdateRequest>({});

  const defaultForm = (): ExecJobUpdateRequest => {
    return {
      id: undefined,
      name: undefined,
      execSeq: undefined,
      expression: undefined,
      timeout: undefined,
      command: undefined,
      parameterSchema: undefined,
      status: undefined,
      recentLogId: undefined,
    };
  };

  // 打开新增
  const openAdd = () => {
    title.value = '添加计划执行';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改计划执行';
    isAddHandle.value = false;
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    formModel.value = Object.assign({}, record);
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
      if (isAddHandle.value) {
        // 新增
        await createExecJob(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateExecJob(formModel.value);
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
