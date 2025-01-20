<template>
  <a-spin class="main-container" :loading="loading">
    <!-- 标题 -->
    <h3 class="setting-header">日志设置</h3>
    <!-- 表单 -->
    <a-form :model="setting"
            ref="formRef"
            class="setting-form"
            label-align="right"
            :auto-label-width="true">
      <!-- 执行详细日志 -->
      <a-form-item field="log_execAppendAnsi"
                   label="执行详细日志"
                   :rules="[{required: true, message: '请选择此项'}]"
                   hide-asterisk>
        <a-switch v-model="setting.log_execAppendAnsi"
                  type="round"
                  checked-value="true"
                  unchecked-value="false"
                  checked-text="详细输出"
                  unchecked-text="原始输出" />
        <template #extra>
          开启后在命令执行时会拼接详细的日志信息, 关闭后则只显示命令的标准输出
        </template>
      </a-form-item>
      <!-- 日志加载行数 -->
      <a-form-item field="log_trackerOffset"
                   label="日志加载行数"
                   :rules="[{required: true, message: '请输入日志加载行数'}]"
                   hide-asterisk>
        <a-input-number v-model="setting.log_trackerOffset"
                        class="input-wrapper"
                        :min="0"
                        :max="99999"
                        placeholder="请输入日志加载行数"
                        allow-clear
                        hide-button>
          <template #suffix>
            行
          </template>
        </a-input-number>
        <template #extra>
          当查看日志时, 默认读取的行数
        </template>
      </a-form-item>
      <!-- 日志监听间隔 -->
      <a-form-item field="log_trackerDelay"
                   label="日志监听间隔"
                   :rules="[{required: true, message: '请输入日志监听间隔'}]"
                   hide-asterisk>
        <a-input-number v-model="setting.log_trackerDelay"
                        class="input-wrapper"
                        :min="0"
                        :max="99999"
                        placeholder="请输入日志监听间隔"
                        allow-clear
                        hide-button>
          <template #suffix>
            ms
          </template>
        </a-input-number>
        <template #extra>
          日志增量刷新的间隔 (毫秒)
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
    name: 'logSetting',
  };
</script>

<script lang="ts" setup>
  import type { LogSetting } from '@/api/system/setting';
  import { onMounted, ref } from 'vue';
  import { getSystemSetting, updateSystemSettingBatch } from '@/api/system/setting';
  import useLoading from '@/hooks/loading';
  import { Message } from '@arco-design/web-vue';
  import { toAnonymousNumber } from '@/utils';
  import { SystemSettingTypes } from '../types/const';

  const { loading, setLoading } = useLoading();

  const formRef = ref();
  const setting = ref<LogSetting>({} as LogSetting);

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
        type: SystemSettingTypes.LOG,
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
      const { data } = await getSystemSetting(SystemSettingTypes.LOG);
      setting.value = {
        ...data,
        log_trackerDelay: toAnonymousNumber(data.log_trackerDelay),
        log_trackerOffset: toAnonymousNumber(data.log_trackerOffset),
      };
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
  .input-wrapper {
    width: 368px;
  }
</style>
