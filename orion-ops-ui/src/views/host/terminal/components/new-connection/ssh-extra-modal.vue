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
           ok-text="保存"
           :ok-button-props="{ disabled: loading }"
           :cancel-button-props="{ disabled: loading }"
           :on-before-ok="handlerOk"
           @close="handleClose">
    <a-spin class="full" :loading="loading">
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :style="{ width: '460px' }"
              :label-col-props="{ span: 6 }"
              :wrapper-col-props="{ span: 18 }"
              :rules="{}">
        <!-- 验证方式 -->
        <a-form-item field="authType" label="验证方式">
          <a-radio-group type="button"
                         v-model="formModel.authType"
                         :options="toOptions(extraSshAuthTypeKey)" />
        </a-form-item>
        <!-- 用户名 -->
        <a-form-item v-if="formModel.authType === ExtraSshAuthType.CUSTOM_KEY"
                     field="username"
                     label="用户名">
          <a-input v-model="formModel.username" placeholder="请输入用户名" />
        </a-form-item>
        <!-- 主机秘钥 -->
        <a-form-item v-if="formModel.authType === ExtraSshAuthType.CUSTOM_KEY"
                     field="keyId"
                     label="主机秘钥"
                     :rules="{ required: true, message: '请选择主机秘钥' }">
          <host-key-selector v-model="formModel.keyId"
                             :authorized="true" />
        </a-form-item>
        <!-- 主机身份 -->
        <a-form-item v-if="formModel.authType === ExtraSshAuthType.CUSTOM_IDENTITY"
                     field="identityId"
                     label="主机身份"
                     :rules="{ required: true, message: '请选择主机身份' }">
          <host-identity-selector v-model="formModel.identityId"
                                  :authorized="true" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'sshExtraModal'
  };
</script>

<script lang="ts" setup>
  import type { HostQueryResponse } from '@/api/asset/host';
  import type { SshExtraModel } from '../../types/terminal.const';
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import useVisible from '@/hooks/visible';
  import { Message } from '@arco-design/web-vue';
  import { getHostExtraItem, updateHostExtra } from '@/api/asset/host-extra';
  import { ExtraSshAuthType, extraSshAuthTypeKey } from '../../types/terminal.const';
  import { useDictStore } from '@/store';
  import HostIdentitySelector from '@/components/asset/host-identity/host-identity-selector.vue';
  import HostKeySelector from '@/components/asset/host-key/host-key-selector.vue';

  const { toOptions } = useDictStore();
  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const title = ref<string>();
  const hostId = ref<number>();
  const formRef = ref();
  const formModel = ref<SshExtraModel>({});

  // 打开配置
  const open = (record: HostQueryResponse) => {
    hostId.value = record.id;
    title.value = record.alias || `${record.name} (${record.code})`;
    renderForm();
    setVisible(true);
  };

  defineExpose({ open });

  // 渲染表单
  const renderForm = async () => {
    try {
      setLoading(true);
      const { data } = await getHostExtraItem<SshExtraModel>({ hostId: hostId.value, item: 'ssh' });
      formModel.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
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
      // 修改
      await updateHostExtra({
        hostId: hostId.value,
        item: 'ssh',
        extra: JSON.stringify(formModel.value)
      });
      Message.success('保存成功');
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
