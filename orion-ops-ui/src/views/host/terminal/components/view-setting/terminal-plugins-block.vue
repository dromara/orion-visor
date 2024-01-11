<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        插件设置
      </h3>
    </div>
    <!-- 内容区域 -->
    <div class="terminal-setting-body setting-body">
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 超链接插件 -->
        <block-setting-item label="超链接插件" desc="自动检测 http url 并可以点击">
          <a-switch type="round"
                    v-model="formModel.enableWeblinkPlugin"
                    checked-value="alt"
                    unchecked-value="none" />
        </block-setting-item>
        <!-- WebGL 渲染插件 -->
        <block-setting-item label="WebGL 渲染插件" desc="使用 WebGL 加速渲染终端 (建议开启, 若无法开启终端请关闭)">
          <a-switch type="round"
                    v-model="formModel.enableWebglPlugin" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 图片渲染插件 -->
        <block-setting-item label="图片渲染插件" desc="支持使用 sixel 打开图片 (一般不需要开启)">
          <a-switch type="round"
                    v-model="formModel.enableImagePlugin" />
        </block-setting-item>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'TerminalPluginsBlock'
  };
</script>

<script lang="ts" setup>

  // fixme
  // 自动检测 url 并可以点击  enableWeblinkPlugin
  // 启用 webgl 支持  enableWebglPlugin
  // 支持显示图片 使用 sixel 打开图片 enableImagePlugin

  import { ref } from 'vue';
  import BlockSettingItem from './block-setting-item.vue';

  const formModel = ref<Record<string, any>>({});

</script>

<style lang="less" scoped>
  .setting-body {
    flex-direction: column;
  }

</style>
