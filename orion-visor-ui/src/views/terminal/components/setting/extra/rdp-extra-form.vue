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
                     :options="toRadioOptions(extraPasswordAuthTypeKey)" />
    </a-form-item>
    <!-- 主机身份 -->
    <a-form-item v-if="formModel.authType === ExtraHostAuthType.CUSTOM_IDENTITY"
                 field="identityId"
                 label="主机身份"
                 :rules="{ required: true, message: '请选择主机身份' }">
      <host-identity-selector v-model="formModel.identityId"
                              :authorized="true"
                              :type="IdentityType.PASSWORD" />
    </a-form-item>
    <!-- 低带宽模式 -->
    <a-form-item field="lowBandwidthMode"
                 style="margin-bottom: 8px;"
                 label="低带宽模式"
                 help="调整图形化配置为最低以及禁用音频, 提升慢速网络下的响应速度">
      <a-switch v-model="formModel.lowBandwidthMode" type="round" />
    </a-form-item>
    <!-- 初始化程序 -->
    <a-form-item field="initialProgram"
                 label="初始化程序"
                 help="会话启动后自动执行的程序 (若支持)">
      <a-input v-model="formModel.initialProgram"
               placeholder="程序的完整路径"
               allow-clear />
    </a-form-item>
  </a-form>
</template>

<script lang="ts">
  export default {
    name: 'rdpExtraForm'
  };
</script>

<script lang="ts" setup>
  import type { HostRdpExtraSettingModel } from '@/api/asset/host-extra';
  import { onMounted, ref } from 'vue';
  import { getHostExtraItem } from '@/api/asset/host-extra';
  import { ExtraHostAuthType, extraPasswordAuthTypeKey } from '@/views/terminal/types/const';
  import { useDictStore } from '@/store';
  import { IdentityType } from '@/views/asset/host-identity/types/const';
  import HostIdentitySelector from '@/components/asset/host-identity/selector/index.vue';

  const props = defineProps<{
    hostId: number;
    item: string;
  }>();

  const { toRadioOptions } = useDictStore();

  const formRef = ref();
  const formModel = ref<Partial<HostRdpExtraSettingModel>>({
    authType: ExtraHostAuthType.DEFAULT,
  });

  // 渲染表单
  const renderForm = async () => {
    const { data } = await getHostExtraItem<HostRdpExtraSettingModel>({ hostId: props.hostId, item: props.item });
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
