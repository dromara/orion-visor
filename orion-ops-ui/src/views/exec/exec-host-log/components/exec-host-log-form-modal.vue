<template>
  <a-modal v-model:visible="visible"
           body-class="modal-form"
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
              :label-col-props="{ span: 5 }"
              :wrapper-col-props="{ span: 18 }"
              :rules="formRules">
        <!-- 执行日志id -->
        <a-form-item field="logId" label="执行日志id">
          <a-input-number v-model="formModel.logId"
                          placeholder="请输入执行日志id"
                          hide-button />
        </a-form-item>
        <!-- 主机id -->
        <a-form-item field="hostId" label="主机id">
          <a-input-number v-model="formModel.hostId"
                          placeholder="请输入主机id"
                          hide-button />
        </a-form-item>
        <!-- 主机名称 -->
        <a-form-item field="hostName" label="主机名称">
          <a-input v-model="formModel.hostName"
                   placeholder="请输入主机名称"
                   allow-clear />
        </a-form-item>
        <!-- 执行状态 -->
        <a-form-item field="status" label="执行状态">
          <a-select v-model="formModel.status"
                    :options="toOptions(execHostStatusKey)"
                    placeholder="请选择执行状态" />
        </a-form-item>
        <!-- 执行命令 -->
        <a-form-item field="command" label="执行命令">
          <a-input v-model="formModel.command"
                   placeholder="请输入执行命令"
                   allow-clear />
        </a-form-item>
        <!-- 执行参数 -->
        <a-form-item field="parameter" label="执行参数">
          <a-input v-model="formModel.parameter"
                   placeholder="请输入执行参数"
                   allow-clear />
        </a-form-item>
        <!-- 退出码 -->
        <a-form-item field="exitStatus" label="退出码">
          <a-input-number v-model="formModel.exitStatus"
                          placeholder="请输入退出码"
                          hide-button />
        </a-form-item>
        <!-- 日志路径 -->
        <a-form-item field="logPath" label="日志路径">
          <a-input v-model="formModel.logPath"
                   placeholder="请输入日志路径"
                   allow-clear />
        </a-form-item>
        <!-- 错误信息 -->
        <a-form-item field="errorMessage" label="错误信息">
          <a-input v-model="formModel.errorMessage"
                   placeholder="请输入错误信息"
                   allow-clear />
        </a-form-item>
        <!-- 执行开始时间 -->
        <a-form-item field="startTime" label="执行开始时间">
          <a-date-picker v-model="formModel.startTime"
                         style="width: 100%"
                         placeholder="请选择执行开始时间"
                         show-time />
        </a-form-item>
        <!-- 执行结束时间 -->
        <a-form-item field="finishTime" label="执行结束时间">
          <a-date-picker v-model="formModel.finishTime"
                         style="width: 100%"
                         placeholder="请选择执行结束时间"
                         show-time />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'execHostLogFormModal'
  };
</script>

<script lang="ts" setup>
  import type { ExecHostLogUpdateRequest } from '@/api/exec/exec-host-log';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { execHostStatusKey } from '../types/const';
  import { createExecHostLog, updateExecHostLog } from '@/api/exec/exec-host-log';
  import { Message } from '@arco-design/web-vue';
  import { useDictStore } from '@/store';

  const emits = defineEmits(['added', 'updated']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const { toOptions } = useDictStore();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);
  const formRef = ref<any>();
  const formModel = ref<ExecHostLogUpdateRequest>({});

  const defaultForm = (): ExecHostLogUpdateRequest => {
    return {
      id: undefined,
      logId: undefined,
      hostId: undefined,
      hostName: undefined,
      status: undefined,
      command: undefined,
      parameter: undefined,
      exitStatus: undefined,
      logPath: undefined,
      errorMessage: undefined,
      startTime: undefined,
      finishTime: undefined,
    };
  };

  // 打开新增
  const openAdd = () => {
    title.value = '添加主机执行记录';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改主机执行记录';
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
        await createExecHostLog(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateExecHostLog(formModel.value);
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
