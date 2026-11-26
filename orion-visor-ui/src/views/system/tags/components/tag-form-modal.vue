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
           :on-before-ok="handleOk"
           @close="handleClose">
    <a-spin class="full" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :auto-label-width="true"
              :rules="formRules">
        <!-- 标签名称 -->
        <a-form-item field="name" label="标签名称">
          <a-input v-model="formModel.name"
                   placeholder="请输入标签名称"
                   allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'systemTagFormModal'
  };
</script>

<script lang="ts" setup>
  import type { TagUpdateRequest } from '@/api/meta/tag';
  import type { FormHandle } from '@/types/form';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { createTag, updateTag } from '@/api/meta/tag';
  import { Message } from '@arco-design/web-vue';
  import { assignOmitRecord } from '@/utils';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const formHandle = ref<FormHandle>('add');

  const defaultForm = (): TagUpdateRequest => {
    return {
      id: undefined,
      name: undefined,
    };
  };

  const formRef = ref();
  const formModel = ref<TagUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = (_type: string) => {
    title.value = '添加标签';
    formHandle.value = 'add';
    formModel.value = assignOmitRecord({ ...defaultForm(), type: _type });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改标签';
    formHandle.value = 'update';
    formModel.value = assignOmitRecord({ ...defaultForm(), ...record });
    setVisible(true);
  };

  defineExpose({ openAdd, openUpdate });

  // 确定
  const handleOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      if (formHandle.value === 'add') {
        // 新增
        await createTag(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateTag(formModel.value);
        Message.success('修改成功');
        emits('updated');
      }
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
