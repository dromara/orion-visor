<template>
  <a-spin class="main-container" :loading="loading">
    <a-descriptions title="加密设置"
                    class="detail-container"
                    :align="{ label: 'right', value: 'left' }"
                    :label-style="{ width: '98px', 'vertical-align': 'top', 'padding-top': '8px' }"
                    :column="1">
      <!-- 提示 -->
      <a-descriptions-item>
        <a-alert>请输入 PKCS8 格式的 RSA Base64 密钥, 用于前后端传输时的数据加密</a-alert>
      </a-descriptions-item>
      <!-- 加密公钥 -->
      <a-descriptions-item label="加密公钥">
        <a-textarea v-model="setting.publicKey"
                    class="input-wrapper"
                    placeholder="RSA 公钥 Base64"
                    :auto-size="{
                      minRows: 6,
                      maxRows: 6,
                    }"
                    allow-clear />
      </a-descriptions-item>
      <!-- 加密私钥 -->
      <a-descriptions-item label="加密私钥">
        <a-textarea v-model="setting.privateKey"
                    class="input-wrapper"
                    placeholder="RSA 私钥 Base64"
                    :auto-size="{
                      minRows: 14,
                      maxRows: 14,
                    }"
                    allow-clear />
      </a-descriptions-item>
      <!-- 按钮 -->
      <a-descriptions-item>
        <a-space v-permission="['infra:system-setting:update']">
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
      </a-descriptions-item>
    </a-descriptions>
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

  const { loading, setLoading } = useLoading();

  const setting = ref<EncryptSetting>({} as EncryptSetting);

  // 保存
  const save = async () => {
    if (!setting.value.publicKey) {
      Message.error('请输入公钥');
      return;
    }
    if (!setting.value.privateKey) {
      Message.error('请输入私钥');
      return;
    }
    setLoading(true);
    try {
      await updateSystemSettingBatch({
        type: 'ENCRYPT',
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
      setting.value.publicKey = data.publicKey;
      setting.value.privateKey = data.privateKey;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 加载配置
  onMounted(async () => {
    setLoading(true);
    try {
      const { data } = await getSystemSetting<EncryptSetting>('ENCRYPT');
      setting.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
  .main-container {
    width: 728px !important;

    .input-wrapper {
      width: 100%;
    }
  }

</style>
