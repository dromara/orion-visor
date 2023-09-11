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
    <a-spin :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :style="{ width: '460px' }"
              :label-col-props="{ span: 6 }"
              :wrapper-col-props="{ span: 18 }"
              :rules="formRules">
        <!-- 主机名称 -->
        <a-form-item field="name" label="主机名称">
          <a-input v-model="formModel.name" placeholder="请输入主机名称" />
        </a-form-item>
        <!-- 主机编码 -->
        <a-form-item field="code" label="主机编码">
          <a-input v-model="formModel.code" placeholder="请输入主机编码" />
        </a-form-item>
        <!-- 主机地址 -->
        <a-form-item field="address" label="主机地址">
          <a-input v-model="formModel.address" placeholder="请输入主机地址" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'asset-host-form-modal'
  };
</script>

<script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { createHost, updateHost } from '@/api/asset/host';
  import { Message } from '@arco-design/web-vue';
  import {} from '../types/enum.types';
  import { toOptions } from '@/utils/enum';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = () => {
    return {
      id: undefined,
      name: undefined,
      code: undefined,
      address: undefined,
      favorite: undefined,
    };
  };

  const formRef = ref<any>();
  const formModel = reactive<Record<string, any>>(defaultForm());

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加主机';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改主机';
    isAddHandle.value = false;
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    Object.keys(formModel).forEach(k => {
      formModel[k] = record[k];
    });
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
        await createHost(formModel as any);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateHost(formModel as any);
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
    setVisible(false);
  };

</script>

<style lang="less" scoped>

</style>
