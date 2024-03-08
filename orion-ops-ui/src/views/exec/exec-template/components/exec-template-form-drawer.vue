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
        <!-- 名称 -->
        <a-form-item field="name" label="名称">
          <a-input v-model="formModel.name"
                   placeholder="请输入名称"
                   allow-clear/>
        </a-form-item>
        <!-- 命令 -->
        <a-form-item field="command" label="命令">
          <a-input v-model="formModel.command"
                   placeholder="请输入命令"
                   allow-clear/>
        </a-form-item>
        <!-- 超时时间 -->
        <a-form-item field="timeout" label="超时时间">
          <a-input-number v-model="formModel.timeout"
                          placeholder="请输入超时时间 秒 0不超时"
                          hide-button />
        </a-form-item>
        <!-- 参数 -->
        <a-form-item field="parameter" label="参数">
          <a-input v-model="formModel.parameter"
                   placeholder="请输入参数"
                   allow-clear/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'execTemplateFormDrawer'
  };
</script>

<script lang="ts" setup>
  import type { ExecTemplateUpdateRequest } from '@/api/exec/exec-template';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import {} from '../types/const';
  import { createExecTemplate, updateExecTemplate } from '@/api/exec/exec-template';
  import { Message } from '@arco-design/web-vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): ExecTemplateUpdateRequest => {
    return {
      id: undefined,
      name: undefined,
      command: undefined,
      timeout: undefined,
      parameter: undefined,
    };
  };

  const formRef = ref<any>();
  const formModel = ref<ExecTemplateUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加执行模板';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改执行模板';
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
        await createExecTemplate(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateExecTemplate(formModel.value);
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
