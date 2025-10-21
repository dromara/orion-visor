<template>
  <a-spin class="main-container" :loading="loading">
    <!-- 标题 -->
    <h3 class="setting-header">加密设置</h3>
    <!-- 表单 -->
    <a-form :model="setting"
            ref="formRef"
            class="setting-form"
            label-align="right"
            :auto-label-width="true">
      <!-- 提示 -->
      <a-form-item style="margin-bottom: 8px">
        <a-alert>请输入 PKCS8 格式的 RSA Base64 密钥, 用于前后端传输时的数据加密</a-alert>
      </a-form-item>
      <!-- 加密公钥 -->
      <a-form-item label="加密公钥"
                   :rules="[{required: true, message: '请输入加密公钥'}]"
                   hide-asterisk>
        <a-textarea v-model="setting['encrypt.public-key']"
                    class="text-wrapper"
                    placeholder="RSA 公钥 Base64"
                    :auto-size="{ minRows: 5, maxRows: 5 }"
                    allow-clear />
      </a-form-item>
      <!-- 加密私钥 -->
      <a-form-item label="加密私钥"
                   :rules="[{required: true, message: '请输入加密私钥'}]"
                   hide-asterisk>
        <a-textarea v-model="setting['encrypt.private-key']"
                    class="text-wrapper"
                    placeholder="RSA 私钥 Base64"
                    :auto-size="{ minRows: 14, maxRows: 14 }"
                    allow-clear />
      </a-form-item>
      <!-- 按钮 -->
      <a-form-item v-permission="['infra:system-setting:update']">
        <a-space>
          <!-- 保存 -->
          <a-button type="primary"
                    size="small"
                    @click="save">
            保存
          </a-button>
          <!-- 生成密钥对 -->
          <a-button size="small" @click="generator">
            生成密钥对
          </a-button>
        </a-space>
      </a-form-item>
    </a-form>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'encryptSetting',
  };
</script>

<script lang="ts" setup>
  import type { EncryptSetting } from '@/api/system/setting';
  import { onMounted, ref } from 'vue';
  import { generatorKeypair, getSystemSetting, updateSystemSettingBatch } from '@/api/system/setting';
  import useLoading from '@/hooks/loading';
  import { Message } from '@arco-design/web-vue';
  import { SystemSettingTypes } from '../types/const';

  const { loading, setLoading } = useLoading();

  const formRef = ref();
  const setting = ref<EncryptSetting>({} as EncryptSetting);

  // 保存
  const save = async () => {
    // 验证参数
    const error = await formRef.value.validate();
    if (error) {
      return false;
    }
    setLoading(true);
    try {
      await updateSystemSettingBatch({
        type: SystemSettingTypes.ENCRYPT,
        settings: { ...setting.value }
      });
      Message.success('修改成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 生成密钥对
  const generator = async () => {
    setLoading(true);
    try {
      const { data } = await generatorKeypair();
      setting.value['encrypt.public-key'] = data.publicKey;
      setting.value['encrypt.private-key'] = data.privateKey;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 加载配置
  onMounted(async () => {
    setLoading(true);
    try {
      const { data } = await getSystemSetting(SystemSettingTypes.ENCRYPT);
      setting.value = {
        ...data
      };
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
  .main-container {
    width: 728px !important;

    .text-wrapper {
      width: 100%;
    }
  }

</style>
