<template>
  <a-modal v-model:visible="visible"
           body-class="modal-form"
           title-align="start"
           title="重置密码"
           :top="120"
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
              :wrapper-col-props="{ span: 18 }">
        <!-- 用户名 -->
        <a-form-item field="username" label="用户名">
          <a-input v-model="updateUser.username" :disabled="true" />
        </a-form-item>
        <!-- 花名 -->
        <a-form-item field="nickname" label="花名">
          <a-input v-model="updateUser.nickname" :disabled="true" />
        </a-form-item>
        <!-- 密码 -->
        <a-form-item field="password" label="新密码" :rules="password">
          <a-input-password v-model="formModel.password" placeholder="请输入新密码" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'user-user-reset-password-form-modal'
  };
</script>

<script lang="ts" setup>
  import type { UserQueryResponse, UserUpdateRequest } from '@/api/user/user';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { password } from '../types/form.rules';
  import { resetUserPassword } from '@/api/user/user';
  import { Message } from '@arco-design/web-vue';
  import { md5 } from '@/utils';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const formRef = ref();
  const formModel = ref<UserUpdateRequest>({});
  const updateUser = ref<UserQueryResponse>({} as UserQueryResponse);

  // 打开
  const open = (record: any) => {
    renderForm(record);
    setVisible(true);
  };

  // 渲染表单
  const renderForm = (record: any) => {
    updateUser.value = Object.assign({}, record);
    formModel.value = {
      password: undefined
    };
  };

  defineExpose({ open });

  // 确定
  const handlerOk = async () => {
    setLoading(true);
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      // 修改
      await resetUserPassword({
        id: updateUser.value.id,
        password: md5(formModel.value.password as string)
      });
      Message.success('修改成功');
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
