<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        会话设置
      </h3>
    </div>
    <!-- 内容区域 -->
    <div class="terminal-setting-body setting-body">
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 终端类型 -->
        <block-setting-item label="终端类型" desc="若显示异常请尝试切换此选项 兼容性 vt100 > xterm > 16color > 256color">
          <a-select style="width: 160px;"
                    v-model="formModel.terminalEmulationType"
                    size="small"
                    :options="toOptions(emulationTypeKey)" />
        </block-setting-item>
        <!-- 缓冲区行数 -->
        <block-setting-item label="缓冲区行数" desc="保存在缓冲区的行数, 多出的行数会被忽略, 此值越大占用内存的内存会更多">
          <a-input-number v-model="formModel.scrollBackLine"
                          size="small"
                          :min="1"
                          :max="10000"
                          placeholder="缓冲区行数 默认 1000 行"
                          allow-clear
                          hide-button />
        </block-setting-item>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalSessionBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalSessionSetting } from '@/store/modules/terminal/types';
  import { ref, watch } from 'vue';
  import { useDictStore, useTerminalStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { emulationTypeKey } from '../../../types/const';
  import BlockSettingItem from '../block-setting-item.vue';

  const { toOptions } = useDictStore();
  const { preference, updateTerminalPreference } = useTerminalStore();

  const formModel = ref<TerminalSessionSetting>({ ...preference.sessionSetting });

  // 监听内容变化
  watch(formModel, (v) => {
    if (!v) {
      return;
    }
    // 同步
    updateTerminalPreference(TerminalPreferenceItem.SESSION_SETTING, formModel.value);
  }, { deep: true });

</script>

<style lang="less" scoped>
  .setting-body {
    flex-direction: column;
  }

</style>
