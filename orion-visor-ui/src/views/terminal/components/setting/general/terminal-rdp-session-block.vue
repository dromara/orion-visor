<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        RDP 会话设置
      </h3>
    </div>
    <!-- 内容区域 -->
    <div class="terminal-setting-body setting-body">
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 启用音频输出 -->
        <block-setting-item label="启用音频输出" desc="启用后会将音频输出到浏览器播放">
          <a-switch v-model="formModel.enableAudioOutput" type="round" />
        </block-setting-item>
        <!-- 驱动挂载模式 -->
        <block-setting-item label="驱动挂载模式">
          <a-select v-model="formModel.driveMountMode"
                    style="width: 198px;"
                    size="small"
                    :options="toOptions(driveMountModeKey)" />
          <template #desc>
            {{ getDictValue(driveMountModeKey, formModel.driveMountMode, 'help', '设置驱动路径的挂载模式') }}
          </template>
        </block-setting-item>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalRdpSessionBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalRdpSessionSetting } from '@/store/modules/terminal/types';
  import { ref, watch } from 'vue';
  import { useDictStore, useTerminalStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { driveMountModeKey } from '@/views/terminal/types/const';
  import BlockSettingItem from '../block-setting-item.vue';

  const { toOptions, getDictValue } = useDictStore();
  const { preference, updateTerminalPreference } = useTerminalStore();

  const formModel = ref<TerminalRdpSessionSetting>({ ...preference.rdpSessionSetting });

  // 监听内容变化
  watch(formModel, (v) => {
    if (!v) {
      return;
    }
    // 同步
    updateTerminalPreference(TerminalPreferenceItem.RDP_SESSION_SETTING, formModel.value);
  }, { deep: true });

</script>

<style lang="less" scoped>
  .setting-body {
    flex-direction: column;
  }

</style>
