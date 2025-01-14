<template>
  <a-spin class="main-container" :loading="loading">
    <h3 class="setting-header">SFTP 设置</h3>
    <!-- 系统信息 -->
    <a-descriptions class="detail-container"
                    size="large"
                    :align="{ label: 'right', value: 'left' }"
                    :label-style="{ width: '128px' }"
                    :column="1">
      <!-- 文件预览大小 -->
      <a-descriptions-item label="文件预览大小">
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
        </a-space>
      </a-descriptions-item>
    </a-descriptions>
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

  const { loading, setLoading } = useLoading();

  const setting = ref<SftpSetting>({} as SftpSetting);

  // 保存
  const save = async () => {
    if (!setting.value.previewSize && setting.value.previewSize !== 0) {
      Message.error('请输入文件预览大小');
      return;
    }
    setLoading(true);
    try {
      await updateSystemSettingBatch({
        type: 'SFTP',
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
      const { data } = await getSystemSetting<SftpSetting>('SFTP');
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
