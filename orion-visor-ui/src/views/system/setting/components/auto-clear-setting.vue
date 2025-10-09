<template>
  <a-spin class="main-container" :loading="loading">
    <!-- 标题 -->
    <h3 class="setting-header">自动清理设置</h3>
    <!-- 表单 -->
    <a-form :model="setting"
            ref="formRef"
            class="setting-form"
            label-align="right"
            :auto-label-width="true">
      <!-- 自动清理执行记录 -->
      <a-form-item label="自动清理执行记录"
                   :rules="[{required: true, message: '请选择此项'}]"
                   hide-asterisk>
        <a-switch v-model="setting['auto-clear.exec-log.enabled']"
                  type="round"
                  checked-value="true"
                  unchecked-value="false"
                  checked-text="开启"
                  unchecked-text="关闭" />
        <template #extra>
          开启后将会在每天凌晨自动清理命令执行记录
        </template>
      </a-form-item>
      <!-- 执行记录保留天数 -->
      <a-form-item label="执行记录保留天数"
                   :rules="[{required: true, message: '请输入执行记录保留天数'}]"
                   hide-asterisk>
        <a-input-number v-model="setting['auto-clear.exec-log.keep-days']"
                        class="input-wrapper"
                        :min="0"
                        :max="99999"
                        placeholder="请输入执行记录保留天数"
                        allow-clear
                        hide-button>
          <template #suffix>
            天
          </template>
        </a-input-number>
        <template #extra>
          自动清理命令执行记录时保留的天数
        </template>
      </a-form-item>
      <!-- 自动清理终端记录 -->
      <a-form-item label="自动清理终端记录"
                   :rules="[{required: true, message: '请选择此项'}]"
                   hide-asterisk>
        <a-switch v-model="setting['auto-clear.terminal-log.enabled']"
                  type="round"
                  checked-value="true"
                  unchecked-value="false"
                  checked-text="开启"
                  unchecked-text="关闭" />
        <template #extra>
          开启后将会在每天凌晨自动清理终端连接记录
        </template>
      </a-form-item>
      <!-- 终端记录保留天数 -->
      <a-form-item label="终端记录保留天数"
                   :rules="[{required: true, message: '请输入终端记录保留天数'}]"
                   hide-asterisk>
        <a-input-number v-model="setting['auto-clear.terminal-log.keep-days']"
                        class="input-wrapper"
                        :min="0"
                        :max="99999"
                        placeholder="请输入终端记录保留天数"
                        allow-clear
                        hide-button>
          <template #suffix>
            天
          </template>
        </a-input-number>
        <template #extra>
          自动清理终端连接记录时保留的天数
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
    name: 'autoClearSetting',
  };
</script>

<script lang="ts" setup>
  import type { AutoClearSetting } from '@/api/system/setting';
  import { onMounted, ref } from 'vue';
  import { getSystemSetting, updateSystemSettingBatch } from '@/api/system/setting';
  import useLoading from '@/hooks/loading';
  import { Message } from '@arco-design/web-vue';
  import { toAnonymousNumber } from '@/utils';
  import { SystemSettingTypes } from '../types/const';

  const { loading, setLoading } = useLoading();

  const formRef = ref();
  const setting = ref<AutoClearSetting>({} as AutoClearSetting);

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
        type: SystemSettingTypes.AUTO_CLEAR,
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
      const { data } = await getSystemSetting(SystemSettingTypes.AUTO_CLEAR);
      setting.value = {
        ...data,
        'auto-clear.exec-log.keep-days': toAnonymousNumber(data['auto-clear.exec-log.keep-days']),
        'auto-clear.terminal-log.keep-days': toAnonymousNumber(data['auto-clear.terminal-log.keep-days']),
      };
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
</style>
