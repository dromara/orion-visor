<template>
  <a-form :model="formModel"
          ref="formRef"
          label-align="right"
          :label-col-props="{ span: 5 }"
          :wrapper-col-props="{ span: 18 }">
    <!-- 认证方式 -->
    <a-form-item field="authType" label="认证方式">
      <a-radio-group type="button"
                     v-model="formModel.authType"
                     :options="toRadioOptions(extraSshAuthTypeKey)" />
    </a-form-item>
    <!-- 用户名 -->
    <a-form-item v-if="formModel.authType === ExtraHostAuthType.CUSTOM_KEY"
                 field="username"
                 label="用户名">
      <a-input v-model="formModel.username" placeholder="请输入用户名" />
    </a-form-item>
    <!-- 主机密钥 -->
    <a-form-item v-if="formModel.authType === ExtraHostAuthType.CUSTOM_KEY"
                 field="keyId"
                 label="主机密钥"
                 :rules="{ required: true, message: '请选择主机密钥' }">
      <host-key-selector v-model="formModel.keyId"
                         :authorized="true" />
    </a-form-item>
    <!-- 主机身份 -->
    <a-form-item v-if="formModel.authType === ExtraHostAuthType.CUSTOM_IDENTITY"
                 field="identityId"
                 label="主机身份"
                 :rules="{ required: true, message: '请选择主机身份' }">
      <host-identity-selector v-model="formModel.identityId"
                              :authorized="true" />
    </a-form-item>
  </a-form>
</template>

<script lang="ts">
  export default {
    name: 'sshExtraForm'
  };
</script>

<script lang="ts" setup>
  import type { HostSshExtraSettingModel } from '@/api/asset/host-extra';
  import { onMounted, ref } from 'vue';
  import { getHostExtraItem } from '@/api/asset/host-extra';
  import { ExtraHostAuthType, extraSshAuthTypeKey } from '@/views/terminal/types/const';
  import { useDictStore } from '@/store';
  import HostKeySelector from '@/components/asset/host-key/selector/index.vue';
  import HostIdentitySelector from '@/components/asset/host-identity/selector/index.vue';

  const props = defineProps<{
    hostId: number;
    item: string;
  }>();

  const { toRadioOptions } = useDictStore();

  const formRef = ref();
  const formModel = ref<Partial<HostSshExtraSettingModel>>({
    authType: ExtraHostAuthType.DEFAULT,
  });

  // 渲染表单
  const renderForm = async () => {
    const { data } = await getHostExtraItem<HostSshExtraSettingModel>({ hostId: props.hostId, item: props.item });
    formModel.value = data;
  };

  // 获取值
  const getValue = async () => {
    // 验证参数
    const error = await formRef.value.validate();
    if (error) {
      return false;
    }
    return JSON.stringify(formModel.value);
  };

  defineExpose({ getValue });

  onMounted(renderForm);

</script>

<style lang="less" scoped>

</style>
