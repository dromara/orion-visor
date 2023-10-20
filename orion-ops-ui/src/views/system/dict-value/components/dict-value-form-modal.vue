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
        <!-- 配置项id -->
        <a-form-item field="keyId" label="配置项id">
          <a-input-number v-model="formModel.keyId"
                          placeholder="请输入配置项id"
                          hide-button />
        </a-form-item>
        <!-- 配置项 -->
        <a-form-item field="keyName" label="配置项">
          <a-input v-model="formModel.keyName" placeholder="请输入配置项" allow-clear />
        </a-form-item>
        <!-- 配置名称 -->
        <a-form-item field="name" label="配置名称">
          <a-input v-model="formModel.name" placeholder="请输入配置名称" allow-clear />
        </a-form-item>
        <!-- 配置值 -->
        <a-form-item field="value" label="配置值">
          <a-input v-model="formModel.value" placeholder="请输入配置值" allow-clear />
        </a-form-item>
        <!-- 配置描述 -->
        <a-form-item field="label" label="配置描述">
          <a-input v-model="formModel.label" placeholder="请输入配置描述" allow-clear />
        </a-form-item>
        <!-- 额外参数 -->
        <a-form-item field="extra" label="额外参数">
          <a-input v-model="formModel.extra" placeholder="请输入额外参数" allow-clear />
        </a-form-item>
        <!-- 排序 -->
        <a-form-item field="sort" label="排序">
          <a-input-number v-model="formModel.sort"
                          placeholder="请输入排序"
                          hide-button />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'system-dict-value-form-modal'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { createDictValue, updateDictValue, DictValueUpdateRequest } from '@/api/system/dict-value';
  import { Message } from '@arco-design/web-vue';
  import {} from '../types/const';
  import {} from '../types/enum.types';
  import { toOptions } from '@/utils/enum';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): DictValueUpdateRequest => {
    return {
      id: undefined,
      keyId: undefined,
      keyName: undefined,
      name: undefined,
      value: undefined,
      label: undefined,
      extra: undefined,
      sort: undefined,
    };
  };

  const formRef = ref<any>();
  const formModel = ref<DictValueUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加字典配置值';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改字典配置值';
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
        await createDictValue(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateDictValue(formModel.value);
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
