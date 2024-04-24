<template>
  <a-drawer v-model:visible="visible"
            :width="388"
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
              :rules="formRules">
        <!-- 名称 -->
        <a-form-item field="name" label="名称">
          <a-input v-model="formModel.name"
                   placeholder="请输入名称"
                   allow-clear />
        </a-form-item>
        <!-- 分组 -->
        <a-form-item field="groupId" label="分组">
          <path-bookmark-group-select v-model="formModel.groupId" />
        </a-form-item>
        <!-- 文件路径 -->
        <a-form-item field="path" label="路径">
          <a-textarea v-model="formModel.path"
                      placeholder="文件路径"
                      :auto-size="{ minRows: 8, maxRows: 8 }"
                      allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-drawer>
</template>

<script lang="ts">
  export default {
    name: 'pathBookmarkFormDrawer'
  };
</script>

<script lang="ts" setup>
  import type { PathBookmarkUpdateRequest } from '@/api/asset/path-bookmark';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { createPathBookmark, updatePathBookmark } from '@/api/asset/path-bookmark';
  import formRules from '../types/form.rules';
  import { Message } from '@arco-design/web-vue';
  import PathBookmarkGroupSelect from './path-bookmark-group-select.vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): PathBookmarkUpdateRequest => {
    return {
      id: undefined,
      groupId: undefined,
      name: undefined,
      path: undefined,
    };
  };

  const formRef = ref<any>();
  const formModel = ref<PathBookmarkUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加路径标签';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改路径标签';
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
        const { data: id } = await createPathBookmark(formModel.value);
        formModel.value.id = id;
        Message.success('创建成功');
        emits('added', formModel.value);
      } else {
        // 修改
        await updatePathBookmark(formModel.value);
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

</style>
