<template>
  <a-drawer v-model:visible="visible"
            width="40%"
            :title="title"
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
              :auto-label-width="true"
              :rules="commandSnippetFormRules">
        <!-- 名称 -->
        <a-form-item field="name" label="名称">
          <a-input v-model="formModel.name"
                   placeholder="请输入名称"
                   allow-clear />
        </a-form-item>
        <!-- 分组 -->
        <a-form-item field="groupId" label="分组">
          <command-snippet-group-selector v-model="formModel.groupId" />
        </a-form-item>
        <!-- 代码片段 -->
        <a-form-item field="command"
                     label="代码片段"
                     :hide-label="true">
          <editor v-model="formModel.command"
                  container-class="command-editor"
                  language="shell"
                  theme="vs-dark"
                  :suggestions="true"
                  :auto-focus="true" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'commandSnippetFormDrawer'
  };
</script>

<script lang="ts" setup>
  import type { CommandSnippetUpdateRequest } from '@/api/terminal/command-snippet';
  import { ref } from 'vue';
  import { createCommandSnippet, updateCommandSnippet } from '@/api/terminal/command-snippet';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { Message } from '@arco-design/web-vue';
  import { commandSnippetFormRules } from '../../types/form.rules';
  import CommandSnippetGroupSelector from '@/components/terminal/command-snippet/gruop/selector/index.vue';

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
  const openAdd = (command: string = '') => {
    title.value = '添加命令片段';
    isAddHandle.value = true;
    renderForm({ ...defaultForm(), command });
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
    padding: 16px;
  }

  .command-editor {
    height: calc(100vh - 262px);
  }

</style>
