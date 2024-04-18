<template>
  <a-modal v-model:visible="visible"
           body-class="modal-form-large"
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
        <!-- 用户名 -->
        <a-form-item field="username" label="用户名">
          <a-input v-model="formModel.username" :disabled="!isAddHandle" placeholder="请输入用户名" />
        </a-form-item>
        <!-- 花名 -->
        <a-form-item field="nickname" label="花名">
          <a-input v-model="formModel.nickname" placeholder="请输入花名" />
        </a-form-item>
        <!-- 密码 -->
        <a-form-item v-if="isAddHandle"
                     field="password"
                     label="密码">
          <a-input-password v-model="formModel.password" placeholder="请输入密码" />
        </a-form-item>
        <!-- 手机号 -->
        <a-form-item field="mobile" label="手机号">
          <a-input v-model="formModel.mobile" placeholder="请输入手机号" />
        </a-form-item>
        <!-- 邮箱 -->
        <a-form-item field="email" label="邮箱">
          <a-input v-model="formModel.email" placeholder="请输入邮箱" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'userFormModal'
  };
</script>

<script lang="ts" setup>
  import type { UserUpdateRequest } from '@/api/user/user';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import formRules from '../types/form.rules';
  import { createUser, updateUser } from '@/api/user/user';
  import { Message } from '@arco-design/web-vue';
  import { md5 } from '@/utils';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const isAddHandle = ref<boolean>(true);

  const defaultForm = (): UserUpdateRequest => {
    return {
      id: undefined,
      username: undefined,
      password: undefined,
      nickname: undefined,
      mobile: undefined,
      email: undefined,
    };
  };

  const formRef = ref();
  const formModel = ref<UserUpdateRequest>({});

  const emits = defineEmits(['added', 'updated']);

  // 打开新增
  const openAdd = () => {
    title.value = '添加用户';
    isAddHandle.value = true;
    renderForm({ ...defaultForm() });
    setVisible(true);
  };

  // 打开修改
  const openUpdate = (record: any) => {
    title.value = '修改用户';
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
        await createUser({ ...formModel.value, password: md5(formModel.value.password as string) });
        Message.success('创建成功');
        emits('added');
      } else {
        // 修改
        await updateUser(formModel.value);
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
