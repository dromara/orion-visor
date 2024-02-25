<template>
  <a-card class="general-card"
          :body-style="{ padding: config.status === EnabledStatus.ENABLED ? '' : '0' }">
    <!-- 标题 -->
    <template #title>
      <div class="config-title-wrapper">
        <span>SSH 配置</span>
        <a-switch v-model="config.status"
                  :disabled="loading"
                  type="round"
                  :checked-value="1"
                  :unchecked-value="0"
                  :beforeChange="s => updateStatus(s as number)" />
      </div>
    </template>
    <a-spin v-show="config.status" :loading="loading" class="config-form-wrapper">
      <!-- 表单 -->
      <a-form :model="formModel"
              ref="formRef"
              label-align="right"
              :label-col-props="{ span: 6 }"
              :wrapper-col-props="{ span: 18 }"
              :rules="rules">
        <!-- 用户名 -->
        <a-form-item field="username"
                     label="用户名"
                     :rules="usernameRules"
                     label-col-flex="60px"
                     :help="SshAuthType.IDENTITY === formModel.authType ? '将使用主机身份的用户名' : undefined">
          <a-input v-model="formModel.username"
                   :disabled="SshAuthType.IDENTITY === formModel.authType"
                   placeholder="请输入用户名" />
        </a-form-item>
        <!-- SSH 端口 -->
        <a-form-item field="port"
                     label="SSH端口"
                     :hide-asterisk="true"
                     label-col-flex="60px">
          <a-input-number v-model="formModel.port"
                          placeholder="请输入SSH端口"
                          hide-button />
        </a-form-item>
        <!-- 验证方式 -->
        <a-form-item field="authType"
                     label="验证方式"
                     :hide-asterisk="true"
                     label-col-flex="60px">
          <a-radio-group type="button"
                         class="auth-type-group usn"
                         v-model="formModel.authType"
                         :options="toRadioOptions(sshAuthTypeKey)" />
        </a-form-item>
        <!-- 主机密码 -->
        <a-form-item v-if="SshAuthType.PASSWORD === formModel.authType"
                     field="password"
                     label="主机密码"
                     :rules="passwordRules"
                     label-col-flex="60px">
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
        <!-- 主机秘钥 -->
        <a-form-item v-if="SshAuthType.KEY === formModel.authType"
                     field="keyId"
                     label="主机秘钥"
                     :hide-asterisk="true"
                     label-col-flex="60px">
          <host-key-selector v-model="formModel.keyId" />
        </a-form-item>
        <!-- 主机身份 -->
        <a-form-item v-if="SshAuthType.IDENTITY === formModel.authType"
                     field="identityId"
                     label="主机身份"
                     :hide-asterisk="true"
                     label-col-flex="60px">
          <host-identity-selector v-model="formModel.identityId" />
        </a-form-item>
        <!-- 用户名 -->
        <a-form-item field="connectTimeout"
                     label="连接超时时间"
                     :hide-asterisk="true"
                     label-col-flex="86px">
          <a-input-number v-model="formModel.connectTimeout"
                          placeholder="请输入连接超时时间"
                          hide-button>
            <template #suffix>
              ms
            </template>
          </a-input-number>
        </a-form-item>
        <!-- SSH输出编码 -->
        <a-form-item field="charset"
                     label="SSH输出编码"
                     :hide-asterisk="true"
                     label-col-flex="86px">
          <a-input v-model="formModel.charset" placeholder="请输入 SSH 输出编码" />
        </a-form-item>
        <!-- 文件名称编码 -->
        <a-form-item field="fileNameCharset"
                     label="文件名称编码"
                     :hide-asterisk="true"
                     label-col-flex="86px">
          <a-input v-model="formModel.fileNameCharset" placeholder="请输入 SFTP 文件名称编码" />
        </a-form-item>
        <!-- 文件内容编码 -->
        <a-form-item field="fileContentCharset"
                     label="文件内容编码"
                     :hide-asterisk="true"
                     label-col-flex="86px">
          <a-input v-model="formModel.fileContentCharset" placeholder="请输入 SFTP 文件内容编码" />
        </a-form-item>
      </a-form>
      <!-- 操作按钮 -->
      <div class="config-button-group">
        <a-space>
          <a-button @click="resetConfig">重置</a-button>
          <a-button type="primary" @click="saveConfig">保存</a-button>
        </a-space>
      </div>
    </a-spin>
  </a-card>
</template>

<script lang="ts">
  export default {
    name: 'sshConfigForm'
  };
</script>

<script lang="ts" setup>
  import type { FieldRule } from '@arco-design/web-vue';
  import type { HostSshConfig } from './types/const';
  import { reactive, ref, watch } from 'vue';
  import { updateHostConfigStatus, updateHostConfig } from '@/api/asset/host-config';
  import { sshAuthTypeKey, SshAuthType } from './types/const';
  import rules from './types/form.rules';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import { useDictStore } from '@/store';
  import { EnabledStatus } from '@/types/const';
  import { HostConfigType } from '../../../types/const';
  import HostKeySelector from '@/components/asset/host-key/host-key-selector.vue';
  import HostIdentitySelector from '@/components/asset/host-identity/host-identity-selector.vue';

  const { loading, setLoading } = useLoading();
  const { toRadioOptions } = useDictStore();

  const props = defineProps({
    content: Object,
    hostId: Number,
  });

  const emits = defineEmits(['submitted']);

  const config = reactive({
    status: undefined,
    version: undefined,
  });

  const formRef = ref();
  const formModel = ref<HostSshConfig>({
    username: undefined,
    port: undefined,
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
  watch(() => props.content, (v: any) => {
    config.status = v?.status;
    config.version = v?.version;
    resetConfig();
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

  // 修改状态
  const updateStatus = (e: number) => {
    setLoading(true);
    return updateHostConfigStatus({
      hostId: props?.hostId,
      type: HostConfigType.SSH,
      status: e,
      version: config.version
    }).then(({ data }) => {
      config.version = data;
      setLoading(false);
      return true;
    }).catch(() => {
      setLoading(false);
      return false;
    });
  };

  // 重置配置
  const resetConfig = () => {
    formModel.value = Object.assign({}, props.content?.config);
    // 使用新密码默认为不包含密码
    formModel.value.useNewPassword = !formModel.value.hasPassword;
  };

  // 保存配置
  const saveConfig = async () => {
    try {
      // 验证参数
      const error = await formRef.value.validate();
      if (error) {
        return false;
      }
      setLoading(true);
      // 更新
      const { data } = await updateHostConfig({
        hostId: props?.hostId,
        type: HostConfigType.SSH,
        version: config.version,
        config: JSON.stringify(formModel.value)
      });
      config.version = data;
      setLoading(false);
      Message.success('修改成功');
      // 回调 props
      emits('submitted', { ...props.content, config: { ...formModel.value } });
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

</script>

<style lang="less" scoped>
  .config-title-wrapper {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .config-form-wrapper {
    width: 100%;
  }

  .config-button-group {
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }

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
