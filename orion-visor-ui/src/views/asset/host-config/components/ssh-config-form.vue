<template>
  <a-card class="general-card" :body-style="{ padding: '0 16px 0 20px'}">
    <!-- 标题 -->
    <template #title>
      <div class="config-title-wrapper">
        <span class="title">SSH 配置</span>
        <!-- 操作按钮 -->
        <a-space>
          <a-button size="small"
                    @click="emits('reset')">
            重置
          </a-button>
          <a-button type="primary"
                    size="small"
                    @click="saveConfig">
            保存
          </a-button>
        </a-space>
      </div>
    </template>
    <!-- 表单 -->
    <div class="config-form-wrapper full">
      <!-- 表单 -->
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :auto-label-width="true"
              :rules="rules">
        <!-- 系统类型 -->
        <a-form-item field="osType"
                     label="系统类型"
                     :hide-asterisk="true">
          <a-select v-model="formModel.osType"
                    :options="toOptions(sshOsTypeKey)"
                    placeholder="请选择系统类型" />
        </a-form-item>
        <!-- 用户名 -->
        <a-form-item field="username"
                     label="用户名"
                     :rules="usernameRules"
                     :help="SshAuthType.IDENTITY === formModel.authType ? '将使用主机身份的用户名' : undefined">
          <a-input v-model="formModel.username"
                   :disabled="SshAuthType.IDENTITY === formModel.authType"
                   placeholder="请输入用户名" />
        </a-form-item>
        <!-- 验证方式 -->
        <a-form-item field="authType"
                     label="验证方式"
                     :hide-asterisk="true">
          <a-radio-group type="button"
                         class="auth-type-group usn"
                         v-model="formModel.authType"
                         :options="toRadioOptions(sshAuthTypeKey)" />
        </a-form-item>
        <!-- 主机密码 -->
        <a-form-item v-if="SshAuthType.PASSWORD === formModel.authType"
                     field="password"
                     label="主机密码"
                     :rules="passwordRules">
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
        <!-- 主机密钥 -->
        <a-form-item v-if="SshAuthType.KEY === formModel.authType"
                     field="keyId"
                     label="主机密钥"
                     :hide-asterisk="true">
          <host-key-selector v-model="formModel.keyId" />
        </a-form-item>
        <!-- 主机身份 -->
        <a-form-item v-if="SshAuthType.IDENTITY === formModel.authType"
                     field="identityId"
                     label="主机身份"
                     :hide-asterisk="true">
          <host-identity-selector v-model="formModel.identityId" />
        </a-form-item>
        <!-- 连接超时时间 -->
        <a-form-item class="mt4"
                     field="connectTimeout"
                     label="连接超时时间"
                     :hide-asterisk="true">
          <a-input-number v-model="formModel.connectTimeout"
                          placeholder="请输入连接超时时间"
                          hide-button>
            <template #suffix>
              ms
            </template>
          </a-input-number>
        </a-form-item>
        <!-- SSH 输出编码 -->
        <a-form-item field="charset"
                     label="SSH输出编码"
                     :hide-asterisk="true">
          <a-input v-model="formModel.charset" placeholder="请输入 SSH 输出编码" />
        </a-form-item>
        <!-- 文件名称编码 -->
        <a-form-item field="fileNameCharset"
                     label="文件名称编码"
                     :hide-asterisk="true">
          <a-input v-model="formModel.fileNameCharset" placeholder="请输入 SFTP 文件名称编码" />
        </a-form-item>
        <!-- 文件内容编码 -->
        <a-form-item field="fileContentCharset"
                     label="文件内容编码"
                     :hide-asterisk="true">
          <a-input v-model="formModel.fileContentCharset" placeholder="请输入 SFTP 文件内容编码" />
        </a-form-item>
      </a-form>
    </div>
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'sshConfigForm'
  };
</script>

<script lang="ts" setup>
  import type { FieldRule } from '@arco-design/web-vue';
  import type { HostSshConfig } from '../types/const';
  import type { HostConfigQueryResponse } from '@/api/asset/host';
  import { ref, watch } from 'vue';
  import { sshAuthTypeKey, sshOsTypeKey, SshAuthType } from '../types/const';
  import { useDictStore } from '@/store';
  import rules from '../types/ssh-form.rules';
  import HostKeySelector from '@/components/asset/host-key/selector/index.vue';
  import HostIdentitySelector from '@/components/asset/host-identity/selector/index.vue';

  const { toOptions, toRadioOptions } = useDictStore();

  const props = defineProps<{
    hostConfig: HostConfigQueryResponse;
  }>();

  const emits = defineEmits(['save', 'reset']);

  const formRef = ref();
  const formModel = ref<HostSshConfig>({
    osType: undefined,
    username: undefined,
    password: undefined,
    authType: SshAuthType.PASSWORD,
    keyId: undefined,
    identityId: undefined,
    connectTimeout: undefined,
    charset: undefined,
    fileNameCharset: undefined,
    fileContentCharset: undefined,
    useNewPassword: false,
    hasPassword: false,
  });

  // 监听数据变化
  watch(() => props.hostConfig.current, () => {
    formModel.value = Object.assign({}, props.hostConfig.config);
    // 使用新密码默认为不包含密码
    formModel.value.useNewPassword = !formModel.value.hasPassword;
  });

  // 用户名验证
  const usernameRules = [{
    validator: (value, cb) => {
      if (value && value.length > 128) {
        cb('用户名长度不能大于128位');
        return;
      }
      if (formModel.value.authType !== SshAuthType.IDENTITY && !value) {
        cb('请输入用户名');
        return;
      }
    }
  }] as FieldRule[];

  // 密码验证
  const passwordRules = [{
    validator: (value, cb) => {
      if (value && value.length > 256) {
        cb('密码长度不能大于256位');
        return;
      }
      if (formModel.value.useNewPassword && !value) {
        cb('请输入密码');
        return;
      }
    }
  }] as FieldRule[];

  // 保存配置
  const saveConfig = async () => {
    // 验证参数
    const error = await formRef.value.validate();
    if (error) {
      return false;
    }
    // 回调
    emits('save', { ...formModel.value });
  };

</script>

<style lang="less" scoped>
  .auth-type-group {
    width: 100%;
    display: flex;
    justify-content: space-between;
  }

  .password-switch {
    width: 148px;
    margin-left: 8px;
  }

</style>
