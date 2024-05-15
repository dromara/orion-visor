<template>
  <a-modal v-model:visible="visible"
           body-class="modal-form-large"
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
    <a-spin class="full" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :rules="rules"
              :auto-label-width="true">
        <!-- 密码 -->
        <a-form-item field="beforePassword" label="原始密码">
          <a-input-password v-model="formModel.beforePassword" placeholder="请输入原始密码" />
        </a-form-item>
        <!-- 密码 -->
        <a-form-item field="password" label="新密码">
          <a-input-password v-model="formModel.password" placeholder="请输入新密码" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'updatePasswordModal'
  };
</script>

<script lang="ts" setup>
  import type { UserUpdatePasswordRequest } from '@/api/user/mine';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { Message } from '@arco-design/web-vue';
  import { md5 } from '@/utils';
  import { updateCurrentUserPassword } from '@/api/user/mine';

  const emits = defineEmits(['updated']);

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const rules = {
    beforePassword: [{
      required: true,
      message: '请输入原始密码'
    }],
    password: [{
      required: true,
      message: '请输入新密码'
    }, {
      minLength: 8,
      maxLength: 32,
      message: '新密码长度需要在 8-32 位之间'
    }],
  };

  const formRef = ref();
  const formModel = ref<UserUpdatePasswordRequest>({});

  // 打开
  const open = () => {
    formModel.value = {
      beforePassword: undefined,
      password: undefined
    };
    setVisible(true);
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
      // 相同校验
      if (formModel.value.beforePassword === formModel.value.password) {
        formRef.value.setFields({
          password: {
            status: 'error',
            message: '新密码不能和原始密码相同'
          }
        });
        return false;
      }
      // 修改
      await updateCurrentUserPassword({
        beforePassword: md5(formModel.value.beforePassword as string),
        password: md5(formModel.value.password as string)
      });
      Message.success('修改成功');
      // 清空
      handlerClear();
      emits('updated');
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
