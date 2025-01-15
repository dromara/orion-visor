<template>
  <a-spin class="main-container" :loading="loading">
    <!-- 标题 -->
    <h3 class="setting-header">SFTP 设置</h3>
    <!-- 表单 -->
    <a-form :model="setting"
            ref="formRef"
            class="setting-form"
            label-align="right"
            :auto-label-width="true">
      <!-- 文件预览大小 -->
      <a-form-item field="previewSize"
                   label="文件预览大小"
                   :rules="[{required: true, message: '请输入文件预览大小'}]"
                   hide-asterisk>
        <a-input-number v-model="setting.previewSize"
                        class="input-wrapper"
                        :min="0"
                        :max="200"
                        placeholder="请输入文件预览大小"
                        allow-clear
                        hide-button>
          <template #suffix>
            MB
          </template>
        </a-input-number>
      </a-form-item>
      <!-- 按钮 -->
      <a-form-item v-permission="['infra:system-setting:update']">
        <!-- 保存 -->
        <a-button type="primary"
                  size="small"
                  @click="save">
          保存
        </a-button>
      </a-form-item>
    </a-form>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'sftpSetting',
  };
</script>

<script lang="ts" setup>
  import type { SftpSetting } from '@/api/system/setting';
  import { onMounted, ref } from 'vue';
  import { getSystemSetting, updateSystemSettingBatch } from '@/api/system/setting';
  import useLoading from '@/hooks/loading';
  import { Message } from '@arco-design/web-vue';
  import { SystemSettingTypes } from '../types/const';

  const { loading, setLoading } = useLoading();

  const formRef = ref();
  const setting = ref<SftpSetting>({} as SftpSetting);

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
        type: SystemSettingTypes.SFTP,
        settings: setting.value
      });
      Message.success('修改成功');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 加载配置
  onMounted(async () => {
    setLoading(true);
    try {
      const { data } = await getSystemSetting<SftpSetting>(SystemSettingTypes.SFTP);
      setting.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
  .input-wrapper {
    width: 328px;
  }
</style>
