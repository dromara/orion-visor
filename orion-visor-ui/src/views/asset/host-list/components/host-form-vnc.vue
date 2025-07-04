<template>
  <a-spin :loading="loading">
    <!-- 表单 -->
    <a-form :model="formModel"
            ref="formRef"
            label-align="right"
            :label-col-props="{ span: 6 }"
            :wrapper-col-props="{ span: 18 }"
            :rules="formRules">
      <!-- 端口 -->
      <a-form-item field="port"
                   label="端口"
                   hide-asterisk>
        <a-input-number v-model="formModel.port"
                        placeholder="请输入 VNC 端口"
                        hide-button />
      </a-form-item>
      <a-row>
        <a-col :span="8">
          <!-- 无用户名 -->
          <a-form-item field="noUsername"
                       label="无用户名"
                       :label-col-props="{ span: 18 }"
                       :wrapper-col-props="{ span: 6 }"
                       hide-asterisk>
            <a-switch v-model="formModel.noUsername" type="round" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <!-- 无密码 -->
          <a-form-item field="noPassword"
                       label="无密码"
                       :label-col-props="{ span: 18 }"
                       :wrapper-col-props="{ span: 6 }"
                       hide-asterisk>
            <a-switch v-model="formModel.noPassword" type="round" />
          </a-form-item>
        </a-col>
      </a-row>
      <!-- 用户名 -->
      <a-form-item v-if="formModel.noUsername !== true"
                   field="username"
                   label="用户名"
                   :help="HostAuthType.IDENTITY === formModel.authType ? '将使用主机身份的用户名' : undefined"
                   hide-asterisk>
        <a-input v-model="formModel.username"
                 :disabled="HostAuthType.IDENTITY === formModel.authType"
                 placeholder="请输入用户名" />
      </a-form-item>
      <!-- 认证方式 -->
      <a-form-item v-if="formModel.noPassword !== true"
                   field="authType"
                   label="认证方式"
                   hide-asterisk>
        <a-radio-group type="button"
                       class="password-auth-type-group usn"
                       v-model="formModel.authType"
                       :options="toRadioOptions(passwordAuthTypeKey)" />
      </a-form-item>
      <!-- 主机密码 -->
      <a-form-item v-if="formModel.noPassword !== true && HostAuthType.PASSWORD === formModel.authType"
                   field="password"
                   label="主机密码"
                   hide-asterisk>
        <a-input-password v-model="formModel.password"
                          :disabled="!formModel.useNewPassword && formModel.hasPassword"
                          placeholder="主机密码" />
        <a-switch v-if="formModel.hasPassword"
                  v-model="formModel.useNewPassword"
                  class="password-switch"
                  type="round"
                  checked-text="使用新密码"
                  unchecked-text="使用原密码" />
      </a-form-item>
      <!-- 主机身份 -->
      <a-form-item v-if="HostAuthType.IDENTITY === formModel.authType"
                   field="identityId"
                   label="主机身份"
                   hide-asterisk>
        <host-identity-selector v-model="formModel.identityId"
                                :type="IdentityType.PASSWORD" />
      </a-form-item>
      <!-- 系统时区 -->
      <a-form-item field="timezone"
                   label="系统时区"
                   hide-asterisk>
        <a-select v-model="formModel.timezone"
                  placeholder="请选择系统时区"
                  :options="toOptions(timezoneKey)" />
      </a-form-item>
      <!-- 剪切板编码 -->
      <a-form-item field="clipboardEncoding"
                   label="剪切板编码"
                   hide-asterisk>
        <a-select v-model="formModel.clipboardEncoding"
                  placeholder="请选择剪切板编码"
                  :options="toOptions(clipboardEncodingKey)" />
      </a-form-item>
      <!-- 操作 -->
      <a-form-item style="margin-bottom: 0;">
        <!-- 保存 -->
        <a-button type="primary"
                  long
                  @click="saveConfig">
          保存
        </a-button>
        <!-- 测试连接 -->
        <a-tooltip position="tr"
                   content="请先保存后测试连接"
                   mini>
          <a-button class="extra-button"
                    type="primary"
                    long
                    @click="testConnect">
            测试连接
          </a-button>
        </a-tooltip>
      </a-form-item>
    </a-form>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'hostFormVnc'
  };
</script>

<script lang="ts" setup>
  import type { HostVncConfig } from '@/api/asset/host-config';
  import { ref, onMounted } from 'vue';
  import useLoading from '@/hooks/loading';
  import { useDictStore } from '@/store';
  import { passwordAuthTypeKey, HostAuthType, HostType, clipboardEncodingKey, timezoneKey } from '../types/const';
  import { IdentityType } from '@/views/asset/host-identity/types/const';
  import { vncFormRules } from '../types/form.rules';
  import useHostConfigForm from '../types/use-host-config';
  import HostIdentitySelector from '@/components/asset/host-identity/selector/index.vue';

  const props = defineProps<{
    hostId: number;
  }>();

  const { loading, setLoading } = useLoading();
  const { toOptions, toRadioOptions } = useDictStore();

  const formModel = ref<HostVncConfig>({} as HostVncConfig);

  const {
    formRef,
    formRules,
    fetchHostConfig,
    testConnect,
    saveConfig,
  } = useHostConfigForm({
    type: HostType.VNC.value,
    hostId: props.hostId,
    rules: vncFormRules,
    formModel,
    setLoading,
  });

  onMounted(fetchHostConfig);

</script>

<style lang="less" scoped>
</style>
