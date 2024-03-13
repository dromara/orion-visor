<!--<template>-->
<!--  <a-modal v-model:visible="visible"-->
<!--           body-class="modal-form"-->
<!--           title-align="start"-->
<!--           :title="title"-->
<!--           :top="80"-->
<!--           :align-center="false"-->
<!--           :draggable="true"-->
<!--           :mask-closable="false"-->
<!--           :unmount-on-close="true"-->
<!--           :ok-button-props="{ disabled: loading }"-->
<!--           :cancel-button-props="{ disabled: loading }"-->
<!--           :on-before-ok="handlerOk"-->
<!--           @close="handleClose">-->
<!--    <a-spin class="full" :loading="loading">-->
<!--      <a-form :model="formModel"-->
<!--              ref="formRef"-->
<!--              label-align="right"-->
<!--              :label-col-props="{ span: 5 }"-->
<!--              :wrapper-col-props="{ span: 18 }"-->
<!--              :rules="formRules">-->
<!--        &lt;!&ndash; 任务id &ndash;&gt;-->
<!--        <a-form-item field="userId" label="任务id">-->
<!--          <a-input-number v-model="formModel.userId"-->
<!--                          placeholder="请输入任务id"-->
<!--                          hide-button />-->
<!--        </a-form-item>-->
<!--        &lt;!&ndash; 执行用户名 &ndash;&gt;-->
<!--        <a-form-item field="username" label="执行用户名">-->
<!--          <a-input v-model="formModel.username"-->
<!--                   placeholder="请输入执行用户名"-->
<!--                   allow-clear />-->
<!--        </a-form-item>-->
<!--        &lt;!&ndash; 执行来源 &ndash;&gt;-->
<!--        <a-form-item field="source" label="执行来源">-->
<!--          <a-input v-model="formModel.source"-->
<!--                   placeholder="请输入执行来源"-->
<!--                   allow-clear />-->
<!--        </a-form-item>-->
<!--        &lt;!&ndash; 执行来源id &ndash;&gt;-->
<!--        <a-form-item field="sourceId" label="执行来源id">-->
<!--          <a-input-number v-model="formModel.sourceId"-->
<!--                          placeholder="请输入执行来源id"-->
<!--                          hide-button />-->
<!--        </a-form-item>-->
<!--        &lt;!&ndash; 执行描述 &ndash;&gt;-->
<!--        <a-form-item field="description" label="执行描述">-->
<!--          <a-input v-model="formModel.description"-->
<!--                   placeholder="请输入执行描述"-->
<!--                   allow-clear />-->
<!--        </a-form-item>-->
<!--        &lt;!&ndash; 执行命令 &ndash;&gt;-->
<!--        <a-form-item field="command" label="执行命令">-->
<!--          <a-input v-model="formModel.command"-->
<!--                   placeholder="请输入执行命令"-->
<!--                   allow-clear />-->
<!--        </a-form-item>-->
<!--        &lt;!&ndash; 超时时间 &ndash;&gt;-->
<!--        <a-form-item field="timeout" label="超时时间">-->
<!--          <a-input-number v-model="formModel.timeout"-->
<!--                          placeholder="请输入超时时间"-->
<!--                          hide-button />-->
<!--        </a-form-item>-->
<!--        &lt;!&ndash; 执行状态 &ndash;&gt;-->
<!--        <a-form-item field="status" label="执行状态">-->
<!--          <a-select v-model="formModel.status"-->
<!--                    :options="toOptions(execStatusKey)"-->
<!--                    placeholder="请选择执行状态" />-->
<!--        </a-form-item>-->
<!--        &lt;!&ndash; 执行开始时间 &ndash;&gt;-->
<!--        <a-form-item field="startTime" label="执行开始时间">-->
<!--          <a-date-picker v-model="formModel.startTime"-->
<!--                         style="width: 100%"-->
<!--                         placeholder="请选择执行开始时间"-->
<!--                         show-time />-->
<!--        </a-form-item>-->
<!--        &lt;!&ndash; 执行完成时间 &ndash;&gt;-->
<!--        <a-form-item field="finishTime" label="执行完成时间">-->
<!--          <a-date-picker v-model="formModel.finishTime"-->
<!--                         style="width: 100%"-->
<!--                         placeholder="请选择执行完成时间"-->
<!--                         show-time />-->
<!--        </a-form-item>-->
<!--      </a-form>-->
<!--    </a-spin>-->
<!--  </a-modal>-->
<!--</template>-->

<!--<script lang="ts">-->
<!--  export default {-->
<!--    name: 'execLogFormModal'-->
<!--  };-->
<!--</script>-->

<!--<script lang="ts" setup>-->
<!--  import type { ExecLogUpdateRequest } from '@/api/exec/exec-log';-->
<!--  import { ref } from 'vue';-->
<!--  import useLoading from '@/hooks/loading';-->
<!--  import useVisible from '@/hooks/visible';-->
<!--  import formRules from '../types/form.rules';-->
<!--  import { execStatusKey } from '../types/const';-->
<!--  import { createExecLog, updateExecLog } from '@/api/exec/exec-log';-->
<!--  import { Message } from '@arco-design/web-vue';-->
<!--  import { useDictStore } from '@/store';-->

<!--  const emits = defineEmits(['added', 'updated']);-->

<!--  const { visible, setVisible } = useVisible();-->
<!--  const { loading, setLoading } = useLoading();-->
<!--  const { toOptions } = useDictStore();-->

<!--  const title = ref<string>();-->
<!--  const isAddHandle = ref<boolean>(true);-->
<!--  const formRef = ref<any>();-->
<!--  const formModel = ref<ExecLogUpdateRequest>({});-->

<!--  const defaultForm = (): ExecLogUpdateRequest => {-->
<!--    return {-->
<!--      id: undefined,-->
<!--      userId: undefined,-->
<!--      username: undefined,-->
<!--      source: undefined,-->
<!--      sourceId: undefined,-->
<!--      description: undefined,-->
<!--      command: undefined,-->
<!--      timeout: undefined,-->
<!--      status: undefined,-->
<!--      startTime: undefined,-->
<!--      finishTime: undefined,-->
<!--    };-->
<!--  };-->

<!--  // 打开新增-->
<!--  const openAdd = () => {-->
<!--    title.value = '添加执行记录';-->
<!--    isAddHandle.value = true;-->
<!--    renderForm({ ...defaultForm() });-->
<!--    setVisible(true);-->
<!--  };-->

<!--  // 打开修改-->
<!--  const openUpdate = (record: any) => {-->
<!--    title.value = '修改执行记录';-->
<!--    isAddHandle.value = false;-->
<!--    renderForm({ ...defaultForm(), ...record });-->
<!--    setVisible(true);-->
<!--  };-->

<!--  // 渲染表单-->
<!--  const renderForm = (record: any) => {-->
<!--    formModel.value = Object.assign({}, record);-->
<!--  };-->

<!--  defineExpose({ openAdd, openUpdate });-->

<!--  // 确定-->
<!--  const handlerOk = async () => {-->
<!--    setLoading(true);-->
<!--    try {-->
<!--      // 验证参数-->
<!--      const error = await formRef.value.validate();-->
<!--      if (error) {-->
<!--        return false;-->
<!--      }-->
<!--      if (isAddHandle.value) {-->
<!--        // 新增-->
<!--        await createExecLog(formModel.value);-->
<!--        Message.success('创建成功');-->
<!--        emits('added');-->
<!--      } else {-->
<!--        // 修改-->
<!--        await updateExecLog(formModel.value);-->
<!--        Message.success('修改成功');-->
<!--        emits('updated');-->
<!--      }-->
<!--      // 清空-->
<!--      handlerClear();-->
<!--    } catch (e) {-->
<!--      return false;-->
<!--    } finally {-->
<!--      setLoading(false);-->
<!--    }-->
<!--  };-->

<!--  // 关闭-->
<!--  const handleClose = () => {-->
<!--    handlerClear();-->
<!--  };-->

<!--  // 清空-->
<!--  const handlerClear = () => {-->
<!--    setLoading(false);-->
<!--  };-->

<!--</script>-->

<!--<style lang="less" scoped>-->

<!--</style>-->
