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
      <a-form-item label="执行详细日志"
                   :rules="[{required: true, message: '请选择此项'}]"
                   hide-asterisk>
        <a-switch v-model="setting['log.exec-detail.enabled']"
                  type="round"
                  checked-value="true"
                  unchecked-value="false"
                  checked-text="详细输出"
                  unchecked-text="原始输出" />
        <template #extra>
          开启后在命令执行时会展示详细的日志信息(执行主机、命令等), 关闭后则只显示命令的标准输出
        </template>
      </a-form-item>
      <!-- 最大显示行数 -->
      <a-form-item label="最大显示行数"
                   :rules="[{required: true, message: '请输入日志最大显示行数'}]"
                   hide-asterisk>
        <a-input-number v-model="setting['log.web-scroll-lines']"
                        class="input-wrapper"
                        :min="0"
                        :max="999999"
                        placeholder="请输入日志最大显示行数"
                        allow-clear
                        hide-button>
          <template #suffix>
            行
          </template>
        </a-input-number>
        <template #extra>
          前端日志组件最大显示的行数, 超出部分将会被覆盖 (数值越大内存占用越多)
        </template>
      </a-form-item>
      <!-- 日志加载行数 -->
      <a-form-item label="日志加载行数"
                   :rules="[{required: true, message: '请输入日志加载行数'}]"
                   hide-asterisk>
        <a-input-number v-model="setting['log.tracker-load-lines']"
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
      <a-form-item label="日志监听间隔"
                   :rules="[{required: true, message: '请输入日志监听间隔'}]"
                   hide-asterisk>
        <a-input-number v-model="setting['log.tracker-load-interval']"
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
        'log.web-scroll-lines': toAnonymousNumber(data['log.web-scroll-lines']),
        'log.tracker-load-interval': toAnonymousNumber(data['log.tracker-load-interval']),
        'log.tracker-load-lines': toAnonymousNumber(data['log.tracker-load-lines']),
      };
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>
</style>
