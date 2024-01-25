<template>
  <a-drawer v-model:visible="visible"
            :title="title"
            :width="388"
            :mask-closable="false"
            :unmount-on-close="true"
            :ok-button-props="{ disabled: loading }"
            :cancel-button-props="{ disabled: loading }"
            :on-before-ok="handlerOk"
            @cancel="handleClose">
    <a-spin class="full form-container" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              layout="vertical"
              :label-col-props="{ span: 6 }"
              :wrapper-col-props="{ span: 18 }"
              :rules="formRules">
        <!-- 名称 -->
        <a-form-item field="name" label="名称">
          <a-input v-model="formModel.name"
                   placeholder="请输入名称"
                   allow-clear />
        </a-form-item>
        <!-- 分组 -->
        <a-form-item field="groupId" label="命令分组">
          <command-snippet-group-select v-model="formModel.groupId" />
        </a-form-item>
        <!-- 代码片段 -->
        <a-form-item field="command"
                     label="代码片段"
                     style="margin: 0;">
          <editor v-model="formModel.command"
                  containerClass="command-editor"
                  language="shell"
                  :auto-focus="true"
                  :theme="preference.theme.dark ? 'vs-dark' : 'vs'" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'assetCommandSnippetFormDrawer'
  };
</script>

<script lang="ts" setup>
  import type { CommandSnippetUpdateRequest } from '@/api/asset/command-snippet';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { createCommandSnippet, updateCommandSnippet } from '@/api/asset/command-snippet';
  import formRules from '../types/form.rules';
  import { Message } from '@arco-design/web-vue';
  import { useTerminalStore } from '@/store';
  import CommandSnippetGroupSelect from './command-snippet-group-select.vue';

  const { preference } = useTerminalStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): CommandSnippetUpdateRequest => {
    return {
      id: undefined,
      groupId: undefined,
      name: undefined,
      command: undefined,
    };
  };

  const formRef = ref<any>();
  const formModel = ref<CommandSnippetUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加命令片段';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改命令片段';
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
        const { data: id } = await createCommandSnippet(formModel.value);
        formModel.value.id = id;
        Message.success('创建成功');
        emits('added', formModel.value);
      } else {
        // 修改
        await updateCommandSnippet(formModel.value);
        Message.success('修改成功');
        emits('updated', formModel.value);
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
  .form-container {
    padding: 12px;
  }

  .command-editor {
    height: calc(100vh - 330px);
  }

</style>
