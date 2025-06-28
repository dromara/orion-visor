<template>
  <a-spin :loading="loading">
    <!-- 表单 -->
    <a-form :model="formModel"
            ref="formRef"
            label-align="right"
            :label-col-props="{ span: 6 }"
            :wrapper-col-props="{ span: 18 }"
            :rules="rdpFormRules">
      <!-- 端口 -->
      <a-form-item field="port"
                   label="端口"
                   hide-asterisk>
        <a-input-number v-model="formModel.port"
                        placeholder="请输入 RDP 端口"
                        hide-button />
      </a-form-item>
      <!-- 用户名 -->
      <a-form-item field="username"
                   label="用户名"
                   :rules="usernameRules"
                   :help="HostAuthType.IDENTITY === formModel.authType ? '将使用主机身份的用户名' : undefined"
                   hide-asterisk>
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
                       :options="toRadioOptions(passwordAuthTypeKey)" />
      </a-form-item>
      <!-- 主机密码 -->
      <a-form-item v-if="HostAuthType.PASSWORD === formModel.authType"
                   field="password"
                   label="主机密码"
                   :rules="passwordRules"
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
      <!-- RDP版本 -->
      <a-form-item field="versionGt81"
                   label="RDP版本"
                   tooltip="RDP 8.1 及以上版本支持动态调整分辨率"
                   hide-asterisk>
        <a-switch v-model="formModel.versionGt81"
                  type="round"
                  checked-text=">= 8.1"
                  unchecked-text="< 8.1" />
      </a-form-item>
      <a-collapse class="advanced-settings" :bordered="false">
        <a-collapse-item header="高级设置" key="1">
          <!-- 域 -->
          <a-form-item field="domain"
                       label="域"
                       hide-asterisk>
            <a-input v-model="formModel.domain"
                     placeholder="身份验证时使用的域"
                     allow-clear />
          </a-form-item>
          <!-- 系统时区 -->
          <a-form-item field="timezone"
                       label="系统时区"
                       hide-asterisk>
            <a-select v-model="formModel.timezone"
                      placeholder="请选择系统时区"
                      :options="toOptions(timezoneKey)" />
          </a-form-item>
          <!-- 键盘布局 -->
          <a-form-item field="keyboardLayout"
                       label="键盘布局"
                       hide-asterisk>
            <a-select v-model="formModel.keyboardLayout"
                      placeholder="请选择系统键盘布局"
                      :options="toOptions(keyboardLayoutKey)" />
          </a-form-item>
          <!-- 剪切板规范 -->
          <a-form-item field="clipboardNormalize"
                       label="剪切板规范"
                       hide-asterisk>
            <a-select v-model="formModel.clipboardNormalize"
                      placeholder="请选择剪切板规范"
                      :options="toOptions(clipboardNormalizeKey)" />
          </a-form-item>
          <!-- 预连接id -->
          <a-form-item field="preConnectionId"
                       label="预连接id"
                       hide-asterisk>
            <a-input v-model="formModel.preConnectionId"
                     placeholder="请输入预连接id"
                     allow-clear />
          </a-form-item>
          <!-- 预连接数据 -->
          <a-form-item field="preConnectionBlob"
                       label="预连接数据"
                       hide-asterisk>
            <a-input v-model="formModel.preConnectionBlob"
                     placeholder="请输入预连接数据"
                     allow-clear />
          </a-form-item>
          <!-- 远程应用 -->
          <a-form-item field="remoteApp"
                       label="远程应用"
                       tooltip="RemoteApp 一般使用双竖线开头, 如 ||notepad, RemoteApp 一般在 Windows Server 系统下可用"
                       hide-asterisk>
            <a-input v-model="formModel.remoteApp"
                     placeholder="请输入 RemoteApp"
                     allow-clear />
          </a-form-item>
          <!-- 远程应用路径 -->
          <a-form-item field="remoteAppDir"
                       label="远程应用路径"
                       hide-asterisk>
            <a-input v-model="formModel.remoteAppDir"
                     placeholder="请输入 RemoteApp 路径"
                     allow-clear />
          </a-form-item>
          <!-- 远程应用参数 -->
          <a-form-item field="remoteAppArgs"
                       label="远程应用参数"
                       style="margin-bottom: 0;"
                       hide-asterisk>
            <a-input v-model="formModel.remoteAppArgs"
                     placeholder="请输入 RemoteApp 参数"
                     allow-clear />
          </a-form-item>
        </a-collapse-item>
      </a-collapse>
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
                    :loading="connectLoading"
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
    name: 'hostFormRdp'
  };
</script>

<script lang="ts" setup>
  import type { FieldRule } from '@arco-design/web-vue';
  import type { HostRdpConfig } from '@/api/asset/host-config';
  import { ref, onMounted } from 'vue';
  import useLoading from '@/hooks/loading';
  import { useDictStore } from '@/store';
  import { passwordAuthTypeKey, HostAuthType, HostType, timezoneKey, keyboardLayoutKey, clipboardNormalizeKey } from '../types/const';
  import { IdentityType } from '@/views/asset/host-identity/types/const';
  import { rdpFormRules } from '../types/form.rules';
  import { Message } from '@arco-design/web-vue';
  import { encrypt } from '@/utils/rsa';
  import { testHostConnect } from '@/api/asset/host';
  import { getHostConfig, updateHostConfig } from '@/api/asset/host-config';
  import HostIdentitySelector from '@/components/asset/host-identity/selector/index.vue';

  const props = defineProps<{
    hostId: number;
  }>();

  const { loading, setLoading } = useLoading();
  const { loading: connectLoading, setLoading: setConnectLoading } = useLoading();
  const { toOptions, toRadioOptions } = useDictStore();

  const formRef = ref();
  const formModel = ref<HostRdpConfig>({} as HostRdpConfig);

  // 用户名验证
  const usernameRules = [{
    validator: (value, cb) => {
      if (value && value.length > 128) {
        cb('用户名长度不能大于128位');
        return;
      }
      if (formModel.value.authType !== HostAuthType.IDENTITY && !value) {
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

  // 加载配置
  const fetchHostConfig = async () => {
    try {
      setLoading(true);
      // 加载配置
      const { data } = await getHostConfig<HostRdpConfig>({
        hostId: props.hostId,
        type: HostType.RDP.value,
      });
      formModel.value = data;
      // 使用新密码默认为不包含密码
      formModel.value.useNewPassword = !formModel.value.hasPassword;
    } catch ({ message }) {
      Message.error(`配置加载失败 ${message}`);
    } finally {
      setLoading(false);
    }
  };

  // 测试连接
  const testConnect = async () => {
    // 验证参数
    const error = await formRef.value.validate();
    if (error) {
      return;
    }
    try {
      setConnectLoading(true);
      // 测试连接
      await testHostConnect({
        id: props.hostId,
        type: HostType.RDP.value,
      });
      Message.success('连接成功');
    } catch (e) {
    } finally {
      setConnectLoading(false);
    }
  };

  // 保存配置
  const saveConfig = async () => {
    // 验证参数
    const error = await formRef.value.validate();
    if (error) {
      return;
    }
    // 加密参数
    const requestData = { ...formModel.value };
    try {
      requestData.password = await encrypt(formModel.value.password);
    } catch (e) {
      return;
    }
    try {
      setLoading(true);
      // 更新
      await updateHostConfig({
        hostId: props.hostId,
        type: HostType.RDP.value,
        config: JSON.stringify(requestData),
      });
      Message.success('修改成功');
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
      width: 50%;
      text-align: center;
    }
  }

  .password-switch {
    width: 148px;
    margin-left: 8px;
  }

  .advanced-settings {
    margin-bottom: 16px;

    :deep(.arco-collapse-item-header) {
      border: none;
      user-select: none;
      background: var(--color-fill-1);
    }

    :deep(.arco-collapse-item-content) {
      padding: 0;
      background: unset;

      &-box {
        padding: 12px 0 0 0;
      }
    }
  }

</style>
