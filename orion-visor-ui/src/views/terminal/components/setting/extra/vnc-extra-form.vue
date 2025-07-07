<template>
  <a-form :model="formModel"
          ref="formRef"
          label-align="right"
          :label-col-props="{ span: 5 }"
          :wrapper-col-props="{ span: 18 }">
    <!-- 自定义端口 -->
    <a-form-item field="port"
                 label="自定义端口"
                 style="margin-bottom: 8px;"
                 help="通常情况下端口号为 5900 + N (屏幕)"
                 :rules="{ min: 1, max: 65535, message: '输入的端口不合法' }">
      <a-input-number v-model="formModel.port"
                      placeholder="VNC 自定义端口"
                      allow-clear />
    </a-form-item>
    <!-- 交换红蓝 -->
    <a-form-item field="swapRedBlue"
                 label="交换红蓝"
                 help="若颜色显示错误(如蓝色显示为橙红色), 则需启用此选项">
      <a-switch v-model="formModel.swapRedBlue" type="round" />
    </a-form-item>
    <!-- 低带宽模式 -->
    <a-form-item field="lowBandwidthMode"
                 label="低带宽模式"
                 help="调整图形化配置为最低, 提升慢速网络下的响应速度">
      <a-switch v-model="formModel.lowBandwidthMode" type="round" />
    </a-form-item>
  </a-form>
</template>

<script lang="ts">
  export default {
    name: 'vncExtraForm'
  };
</script>

<script lang="ts" setup>
  import type { HostVncExtraSettingModel } from '@/api/asset/host-extra';
  import { onMounted, ref } from 'vue';
  import { getHostExtraItem } from '@/api/asset/host-extra';
  import { useDictStore } from '@/store';

  const props = defineProps<{
    hostId: number;
    item: string;
  }>();

  const { toRadioOptions } = useDictStore();

  const formRef = ref();
  const formModel = ref<Partial<HostVncExtraSettingModel>>({});

  // 渲染表单
  const renderForm = async () => {
    const { data } = await getHostExtraItem<HostVncExtraSettingModel>({ hostId: props.hostId, item: props.item });
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
