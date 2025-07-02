<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        SSH 插件设置
      </h3>
    </div>
    <!-- 内容区域 -->
    <div class="terminal-setting-body setting-body">
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 超链接插件 -->
        <block-setting-item label="超链接插件" desc="自动检测 http(https) url 并可以点击">
          <a-switch v-model="formModel.enableWeblinkPlugin" type="round" />
        </block-setting-item>
        <!-- WebGL 渲染插件 -->
        <block-setting-item label="WebGL 渲染插件" desc="使用 WebGL 加速渲染终端 (建议开启, 若无法开启终端请关闭)">
          <a-switch v-model="formModel.enableWebglPlugin" type="round" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- unicode11 插件 -->
        <block-setting-item label="unicode11 插件" desc="支持 Unicode 11 字符集">
          <a-switch v-model="formModel.enableUnicodePlugin" type="round" />
        </block-setting-item>
        <!-- 图片渲染插件 -->
        <block-setting-item label="图片渲染插件" desc="支持使用 sixel 打开图片 (一般不需要开启)">
          <a-switch v-model="formModel.enableImagePlugin" type="round" />
        </block-setting-item>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalSshPluginsBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalSshPluginsSetting } from '@/store/modules/terminal/types';
  import { ref, watch } from 'vue';
  import { useTerminalStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import BlockSettingItem from '../block-setting-item.vue';

  const { preference, updateTerminalPreference } = useTerminalStore();

  const formModel = ref<TerminalSshPluginsSetting>({ ...preference.sshPluginsSetting });

  // 监听内容变化
  watch(formModel, (v) => {
    if (!v) {
      return;
    }
    // 同步
    updateTerminalPreference(TerminalPreferenceItem.SSH_PLUGINS_SETTING, formModel.value);
  }, { deep: true });

</script>

<style lang="less" scoped>
  .setting-body {
    flex-direction: column;
  }

</style>
