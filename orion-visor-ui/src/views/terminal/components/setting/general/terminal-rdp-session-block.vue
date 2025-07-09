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
                    style="width: 168px;"
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
  import { useDictStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { driveMountModeKey } from '@/views/terminal/types/const';
  import useTerminalPreference from '@/views/terminal/types/use-terminal-preference';
  import BlockSettingItem from '../block-setting-item.vue';

  const { toOptions, getDictValue } = useDictStore();

  const { formModel } = useTerminalPreference<TerminalRdpSessionSetting>(TerminalPreferenceItem.RDP_SESSION_SETTING, true);

</script>

<style lang="less" scoped>
  .setting-body {
    flex-direction: column;
  }

</style>
