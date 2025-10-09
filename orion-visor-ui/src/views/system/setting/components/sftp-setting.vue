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
      <!-- 重复文件备份 -->
      <a-form-item label="重复文件备份"
                   :rules="[{required: true, message: '请选择此项'}]"
                   hide-asterisk>
        <a-switch v-model="setting['sftp.upload-present-backup']"
                  type="round"
                  checked-value="true"
                  unchecked-value="false"
                  checked-text="备份"
                  unchecked-text="覆盖" />
        <template #extra>
          文件上传时, 若文件存在是否备份原始文件
        </template>
      </a-form-item>
      <!-- 备份文件名称 -->
      <a-form-item label="备份文件名称"
                   :rules="[{required: true, message: '请输入备份文件名称'}]"
                   hide-asterisk>
        <a-input v-model="setting['sftp.upload-backup-file-name']"
                 class="input-wrapper"
                 placeholder="请输入备份文件名称模板"
                 allow-clear />
        <template #extra>
          ${fileName} 文件名称, ${timestamp} 时间戳, ${time} 时间
        </template>
      </a-form-item>
      <!-- 文件预览大小 -->
      <a-form-item label="文件预览大小"
                   :rules="[{required: true, message: '请输入文件预览大小'}]"
                   hide-asterisk>
        <a-input-number v-model="setting['sftp.preview-size']"
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
        <template #extra>
          可以直接查看或编辑小于等于该大小的普通文件
        </template>
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
  import { toAnonymousNumber } from '@/utils';
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
        settings: setting.value,
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
      const { data } = await getSystemSetting(SystemSettingTypes.SFTP);
      setting.value = {
        ...data,
        'sftp.preview-size': toAnonymousNumber(data['sftp.preview-size']),
      };
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
</style>
