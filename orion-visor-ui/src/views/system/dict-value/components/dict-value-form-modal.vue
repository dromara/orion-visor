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
           :on-before-ok="handlerOk"
           @close="handleClose">
    <a-spin class="full" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :auto-label-width="true"
              :rules="formRules">
        <!-- 配置项 -->
        <a-form-item v-if="visible"
                     field="keyId"
                     label="配置项">
          <dict-key-selector v-model="formModel.keyId" @change="changeKey" />
        </a-form-item>
        <!-- 配置描述 -->
        <a-form-item field="label" label="配置描述">
          <a-input v-model="formModel.label" placeholder="请输入配置描述" allow-clear />
        </a-form-item>
        <!-- 配置值 -->
        <a-form-item field="value" label="配置值">
          <a-input v-model="formModel.value" placeholder="请输入配置值" allow-clear />
        </a-form-item>
        <!-- 排序 -->
        <a-form-item field="sort" label="排序">
          <a-input-number v-model="formModel.sort"
                          placeholder="请输入排序"
                          hide-button />
        </a-form-item>
        <!-- 额外配置 -->
        <a-form-item v-for="{ name, type } in keyExtraSchemas"
                     :key="name"
                     :field="name as string"
                     :label="name">
          <!-- 字符串 -->
          <a-input v-if="ValueType.STRING === type"
                   v-model="extraValue[name]"
                   :placeholder="`请输入 ${name}`"
                   allow-clear />
          <!-- 数字 -->
          <a-input-number v-else-if="ValueType.INTEGER === type || ValueType.DECIMAL === type"
                          v-model="extraValue[name]"
                          :placeholder="`请输入 ${name}`"
                          :precision="ValueType.INTEGER === type ? 0 : 4"
                          allow-clear
                          hide-button />
          <!-- 布尔值 -->
          <a-switch v-else-if="ValueType.BOOLEAN === type"
                    type="round"
                    v-model="extraValue[name]"
                    checked-text="TRUE"
                    unchecked-text="FALSE" />
          <!-- 颜色 -->
          <template v-else-if="ValueType.COLOR === type">
            <a-input v-model="extraValue[name]"
                     class="item-color-input"
                     :placeholder="`请输入 ${name}`"
                     allow-clear
                     hide-button />
            <div class="item-color-block-wrapper">
              <span class="item-color-block" :style="{
                background: extraValue[name] === '#' ? undefined : (extraValue[name] || undefined)
              }" />
            </div>
          </template>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'dictValueFormModal'
  };
</script>

<script lang="ts" setup>
  import type { DictKeyQueryResponse } from '@/api/system/dict-key';
  import type { DictValueUpdateRequest } from '@/api/system/dict-value';
  import type { ExtraParamType } from '../../dict-key/types/const';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { createDictValue, updateDictValue } from '@/api/system/dict-value';
  import { Message } from '@arco-design/web-vue';
  import { ValueType, sortStep } from '../../dict-key/types/const';
  import { useCacheStore } from '@/store';
  import DictKeySelector from '@/components/system/dict-key/selector/index.vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): DictValueUpdateRequest => {
    return {
      id: undefined,
      keyId: undefined,
      value: undefined,
      label: undefined,
      extra: undefined,
      sort: undefined,
    };
  };

  const keyExtraSchemas = ref<Array<ExtraParamType>>([]);
  const extraValue = ref<Record<string, any>>({});
  const formRef = ref<any>();
  const formModel = ref<DictValueUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = async () => {
    title.value = '添加字典配置值';
    isAddHandle.value = true;
    await renderForm({ ...defaultForm(), keyId: formModel.value.keyId, sort: (formModel.value.sort || 0) + sortStep });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = async (record: any) => {
    title.value = '修改字典配置值';
    isAddHandle.value = false;
    await renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = async (record: any) => {
    formModel.value = Object.assign({}, record);
    // schema
    const dictKeys = await cacheStore.loadDictKeys();
    const find = record.keyId && dictKeys.find((item) => item.id === record.keyId);
    keyExtraSchemas.value = (find && find.extraSchema && JSON.parse(find.extraSchema)) || [];
    // 额外参数
    extraValue.value = (formModel.value.extra && JSON.parse(formModel.value.extra)) || {};
  };

  defineExpose({ openAdd, openUpdate });

  // 切换 key
  const changeKey = ({ extraSchema }: DictKeyQueryResponse) => {
    keyExtraSchemas.value = (extraSchema && JSON.parse(extraSchema)) || [];
  };

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      // 验证额外参数
      if (keyExtraSchemas.value.length) {
        for (let { name, type } of keyExtraSchemas.value) {
          const nameKey = name as string;
          const value = extraValue.value[nameKey];
          if (value === undefined) {
            if (type === ValueType.BOOLEAN) {
              extraValue.value[nameKey] = false;
              continue;
            }
            formRef.value.setFields({
              [nameKey]: {
                status: 'error',
                message: `${name} 不能为空`
              }
            });
            return false;
          }
        }
      }
      if (isAddHandle.value) {
        // 新增
        await createDictValue({ ...formModel.value, extra: JSON.stringify(extraValue.value) });
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateDictValue({ ...formModel.value, extra: JSON.stringify(extraValue.value) });
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

  // 关闭回调
  const handleClose = () => {
    if (!isAddHandle.value) {
      // 修改关闭后设置排序为 0 下次进入的排序字段为 10
      formModel.value.sort = 0;
    }
    handlerClear();
  };

  // 清空
  const handlerClear = () => {
    setLoading(false);
  };

</script>

<style lang="less" scoped>
  .item-color-input {
    width: calc(100% - 40px);
  }

  .item-color-block-wrapper {
    width: 32px;
    height: 32px;
    padding: 4px;
    margin-left: 8px;
    border-radius: 4px;
    background: var(--color-fill-2);
  }

  .item-color-block {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 2px;
  }
</style>
