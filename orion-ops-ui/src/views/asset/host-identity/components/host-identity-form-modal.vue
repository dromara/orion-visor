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
        <!-- 名称 -->
        <a-form-item field="name" label="名称">
          <a-input v-model="formModel.name" placeholder="请输入名称" />
        </a-form-item>
        <!-- 用户名 -->
        <a-form-item field="username" label="用户名">
          <a-input v-model="formModel.username" placeholder="请输入用户名" />
        </a-form-item>
        <!-- 用户密码 -->
        <a-form-item field="password"
                     label="用户密码"
                     style="justify-content: space-between;"
                     :rules="passwordRules">
          <a-input-password v-model="formModel.password"
                            :disabled="!isAddHandle && !formModel.useNewPassword"
                            :class="[isAddHandle ? 'password-input-full' : 'password-input']"
                            placeholder="请输入用户密码" />
          <a-switch v-model="formModel.useNewPassword"
                    v-if="!isAddHandle"
                    class="password-switch"
                    type="round"
                    size="large"
                    checked-text="使用新密码"
                    unchecked-text="使用原密码" />
        </a-form-item>
        <!-- 秘钥id -->
        <a-form-item field="keyId" label="主机秘钥">
          <host-key-selector v-model="formModel.keyId" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'asset-host-identity-form-modal'
  };
</script>

<script lang="ts" setup>
  import type { HostIdentityUpdateRequest } from '@/api/asset/host-identity';
  import type { FieldRule } from '@arco-design/web-vue';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { createHostIdentity, updateHostIdentity } from '@/api/asset/host-identity';
  import { Message } from '@arco-design/web-vue';
  import HostKeySelector from '@/components/asset/host-key/host-key-selector.vue';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): HostIdentityUpdateRequest => {
    return {
      id: undefined,
      name: undefined,
      username: undefined,
      password: undefined,
      useNewPassword: false,
      keyId: undefined,
    };
  };

  const formRef = ref();
  const formModel = ref<HostIdentityUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加主机身份';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改主机身份';
    isAddHandle.value = false;
    renderForm({ ...defaultForm(), ...record });
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    formModel.value = Object.assign({}, record);
  };

  defineExpose({ openAdd, openUpdate });

  // 密码验证
  const passwordRules = [{
    validator: (value, cb) => {
      if (value && value.length > 512) {
        cb('密码长度不能大于512位');
        return;
      }
      if (formModel.value.useNewPassword && !value) {
        cb('请输入密码');
        return;
      }
    }
  }] as FieldRule[];

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
        if (!formModel.value.password && !formModel.value.keyId) {
          formRef.value.setFields({
            password: {
              status: 'error',
              message: '创建时密码和秘钥不能同时为空'
            }
          });
          return false;
        }
        // 新增
        await createHostIdentity(formModel.value);
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateHostIdentity(formModel.value);
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
  .password-input {
    width: 240px;
  }

  .password-input-full {
    width: 100%;
  }

  .password-switch {
    margin-left: 16px;
  }
</style>
