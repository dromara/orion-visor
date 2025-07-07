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
                        placeholder="请输入 SSH 端口"
                        hide-button />
      </a-form-item>
      <!-- 用户名 -->
      <a-form-item field="username"
                   label="用户名"
                   :help="HostAuthType.IDENTITY === formModel.authType ? '将使用主机身份的用户名' : undefined">
        <a-input v-model="formModel.username"
                 :disabled="HostAuthType.IDENTITY === formModel.authType"
                 placeholder="请输入用户名" />
      </a-form-item>
      <!-- 认证方式 -->
      <a-form-item field="authType"
                   label="认证方式"
                   hide-asterisk>
        <a-radio-group type="button"
                       class="auth-type-group usn"
                       v-model="formModel.authType"
                       :options="toRadioOptions(sshAuthTypeKey)" />
      </a-form-item>
      <!-- 主机密码 -->
      <a-form-item v-if="HostAuthType.PASSWORD === formModel.authType"
                   field="password"
                   label="主机密码">
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
      <a-form-item v-if="HostAuthType.KEY === formModel.authType"
                   field="keyId"
                   label="主机密钥"
                   hide-asterisk>
        <host-key-selector v-model="formModel.keyId" />
      </a-form-item>
      <!-- 主机身份 -->
      <a-form-item v-if="HostAuthType.IDENTITY === formModel.authType"
                   field="identityId"
                   label="主机身份"
                   hide-asterisk>
        <host-identity-selector v-model="formModel.identityId" />
      </a-form-item>
      <!-- 连接超时时间 -->
      <a-form-item field="connectTimeout"
                   label="连接超时时间"
                   hide-asterisk>
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
                   hide-asterisk>
        <a-input v-model="formModel.charset" placeholder="请输入 SSH 输出编码" />
      </a-form-item>
      <!-- 文件名称编码 -->
      <a-form-item field="fileNameCharset"
                   label="文件名称编码"
                   hide-asterisk>
        <a-input v-model="formModel.fileNameCharset" placeholder="请输入 SFTP 文件名称编码" />
      </a-form-item>
      <!-- 文件内容编码 -->
      <a-form-item field="fileContentCharset"
                   label="文件内容编码"
                   hide-asterisk>
        <a-input v-model="formModel.fileContentCharset" placeholder="请输入 SFTP 文件内容编码" />
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
    name: 'hostFormSsh'
  };
</script>

<script lang="ts" setup>
  import type { HostSshConfig } from '@/api/asset/host-config';
  import { ref, onMounted } from 'vue';
  import useLoading from '@/hooks/loading';
  import { useDictStore } from '@/store';
  import { sshAuthTypeKey, HostAuthType, HostType } from '../types/const';
  import { sshFormRules } from '../types/form.rules';
  import { Message } from '@arco-design/web-vue';
  import { testHostConnect } from '@/api/asset/host';
  import useHostConfigForm from '../types/use-host-config';
  import HostIdentitySelector from '@/components/asset/host-identity/selector/index.vue';
  import HostKeySelector from '@/components/asset/host-key/selector/index.vue';

  const props = defineProps<{
    hostId: number;
  }>();

  const { loading, setLoading } = useLoading();
  const { toRadioOptions } = useDictStore();

  const formModel = ref<HostSshConfig>({} as HostSshConfig);

  const {
    formRef,
    formRules,
    fetchHostConfig,
    saveConfig,
  } = useHostConfigForm({
    type: HostType.SSH.value,
    hostId: props.hostId,
    rules: sshFormRules,
    formModel,
    setLoading,
  });

  // 测试连接
  const testConnect = async () => {
    const error = await formRef.value.validate();
    if (error) {
      return;
    }
    try {
      setLoading(true);
      // 测试连接
      await testHostConnect({ id: props.hostId, type: HostType.SSH.value });
      Message.success('连接成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  onMounted(fetchHostConfig);

</script>

<style lang="less" scoped>
  .auth-type-group {
    width: 100%;
    display: flex;
    justify-content: space-between;

    :deep(.arco-radio-button) {
      width: 33%;
      text-align: center;
    }
  }

</style>
