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
    </a-descriptions>
    <!-- 按钮 -->
    <a-space v-permission="['infra:system-setting:update']"
             class="button-container">
      <!-- 保存 -->
      <a-button type="primary"
                size="small"
                @click="save">
        保存
      </a-button>
    </a-space>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'systemSettingSftpSetting',
  };
</script>

<script lang="ts" setup>
  import type { SftpSetting } from '@/api/system/setting';
  import { onMounted, ref } from 'vue';
  import { getSystemSetting, updatePartialSystemSetting } from '@/api/system/setting';
  import useLoading from '@/hooks/loading';
  import { Message } from '@arco-design/web-vue';

  const { loading, setLoading } = useLoading();

  const setting = ref<SftpSetting>({} as SftpSetting);

  // 保存
  const save = async () => {
    setLoading(true);
    try {
      await updatePartialSystemSetting({
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
  @form-width: 628px;
  @input-width: 328px;

  .main-container {
    width: @form-width;
    padding-left: 24px;

    .setting-header {
      color: var(--color-text-1);
    }

    .alert-href {
      text-decoration: none;
    }

    .alert-wrapper {
      margin-bottom: 12px;
    }

    .input-wrapper {
      width: @input-width;
    }

    .button-container {
      margin-left: 128px;
      margin-bottom: 12px;
    }
  }
</style>
