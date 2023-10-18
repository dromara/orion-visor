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
        <!-- 配置项 -->
        <a-form-item field="keyName" label="配置项">
          <a-input v-model="formModel.keyName" placeholder="请输入配置项" allow-clear />
        </a-form-item>
        <!-- 配置值定义 -->
        <a-form-item field="valueType" label="配置值定义">
          <a-select v-model="formModel.valueType"
                    :options="toOptions(ValueTypeEnum)"
                    placeholder="请选择配置值定义" />
        </a-form-item>
        <!-- 额外配置定义 -->
        <a-form-item field="extraSchema" label="额外配置定义">
          <a-input v-model="formModel.extraSchema" placeholder="请输入额外配置定义" allow-clear />
        </a-form-item>
        <!-- 配置描述 -->
        <a-form-item field="description" label="配置描述">
          <a-input v-model="formModel.description" placeholder="请输入配置描述" allow-clear />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'system-dict-key-form-modal'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/dict-key.form.rules';
  import { createDictKey, updateDictKey, DictKeyUpdateRequest } from '@/api/system/dict-key';
  import { Message } from '@arco-design/web-vue';
  import {} from '../types/const';
  import { ValueTypeEnum } from '../types/enum.types';
  import { toOptions } from '@/utils/enum';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): DictKeyUpdateRequest => {
    return {
      id: undefined,
      keyName: undefined,
      valueType: undefined,
      extraSchema: undefined,
      description: undefined,
    };
  };

  const formRef = ref<any>();
  const formModel = ref<DictKeyUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加字典配置项';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改字典配置项';
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
        await createDictKey(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateDictKey(formModel.value);
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
