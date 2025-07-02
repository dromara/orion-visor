<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        RDP 图形化设置
      </h3>
    </div>
    <!-- 提示 -->
    <a-alert class="mb16">修改后会立刻保存, 重新打开终端后生效. 配置调整后可能会占用更多的带宽, 若发生卡顿/无法加载请调整配置</a-alert>
    <!-- 内容区域 -->
    <div class="terminal-setting-body setting-body">
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 显示分辨率 -->
        <block-setting-item label="显示分辨率" desc="若选择为自适应, 则会根据窗口大小自动调整">
          <a-select v-model="formModel.displaySize"
                    style="width: 198px;"
                    size="small"
                    :options="toOptions(screenResolutionKey)"
                    allow-create />
        </block-setting-item>
        <!-- 颜色深度 -->
        <block-setting-item label="颜色深度" desc="显示的颜色深度, 越高显示效果越好">
          <a-select v-model="formModel.colorDepth"
                    style="width: 198px;"
                    size="small"
                    :options="toOptions(graphColorDepthKey)" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 无损压缩 -->
        <block-setting-item label="无损压缩" desc="是否启用对图形更新的无损压缩">
          <a-switch v-model="formModel.forceLossless" type="round" />
        </block-setting-item>
        <!-- 启用壁纸 -->
        <block-setting-item label="启用壁纸" desc="是否启用桌面壁纸的渲染">
          <a-switch v-model="formModel.enableWallpaper" type="round" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 启用主题 -->
        <block-setting-item label="启用主题" desc="允许使用窗口和控件的主题">
          <a-switch v-model="formModel.enableTheming" type="round" />
        </block-setting-item>
        <!-- 启动平滑字体 -->
        <block-setting-item label="启动平滑字体" desc="允许文本将以平滑的边缘呈现">
          <a-switch v-model="formModel.enableFontSmoothing" type="round" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 启用窗口拖动 -->
        <block-setting-item label="启用窗口拖动" desc="开启后当移动窗口时将显示窗口内容">
          <a-switch v-model="formModel.enableFullWindowDrag" type="round" />
        </block-setting-item>
        <!-- 启用桌面合成 -->
        <block-setting-item label="启用桌面合成" desc="开启后将显示高级图形效果 (如透明窗口、阴影等)">
          <a-switch v-model="formModel.enableDesktopComposition" type="round" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 启用菜单动画 -->
        <block-setting-item label="启用菜单动画" desc="开启后将显示菜单打开和关闭的过渡动画">
          <a-switch v-model="formModel.enableMenuAnimations" type="round" />
        </block-setting-item>
        <!-- 禁用位图缓存 -->
        <block-setting-item label="禁用位图缓存" desc="在某些实现存在兼容性问题时, 禁用后可绕过位图缓存">
          <a-switch v-model="formModel.disableBitmapCaching" type="round" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 禁用离屏缓存 -->
        <block-setting-item label="禁用离屏缓存" desc="在某些实现存在兼容性问题时, 禁用后将不再缓存屏幕外区域">
          <a-switch v-model="formModel.disableOffscreenCaching" type="round" />
        </block-setting-item>
        <!-- 禁用字形缓存 -->
        <block-setting-item label="禁用字形缓存" desc="在某些实现存在兼容性问题时, 禁用后将不再缓存常用字体和符号">
          <a-switch v-model="formModel.disableGlyphCaching" type="round" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 禁用图形加速 -->
        <block-setting-item label="禁用图形加速" desc="禁用后将不再使用 GFX 进行数据编码">
          <a-switch v-model="formModel.disableGfx" type="round" />
        </block-setting-item>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalRdpGraphBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalRdpGraphSetting } from '@/store/modules/terminal/types';
  import { ref, watch } from 'vue';
  import { useTerminalStore, useDictStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { screenResolutionKey, graphColorDepthKey, fitDisplayValue } from '@/views/terminal/types/const';
  import { getDisplaySize } from '@/views/terminal/types/utils';
  import BlockSettingItem from '../block-setting-item.vue';

  const { toOptions, getDictValue } = useDictStore();
  const { preference, updateTerminalPreference } = useTerminalStore();

  const formModel = ref<TerminalRdpGraphSetting>({ ...preference.rdpGraphSetting });

  // 监听内容变化
  watch(formModel, (v) => {
    if (!v) {
      return;
    }
    // 同步大小
    if (v.displaySize) {
      // 自适应大小
      if (v.displaySize === fitDisplayValue) {
        v.displayWidth = 0;
        v.displayHeight = 0;
      } else {
        // 解析大小
        try {
          const [width, height] = getDisplaySize(v.displaySize, true);
          v.displayWidth = width;
          v.displayHeight = height;
        } catch (e) {
          return;
        }
      }
    }
    // 同步
    updateTerminalPreference(TerminalPreferenceItem.RDP_GRAPH_SETTING, formModel.value, true);
  }, { deep: true });

</script>

<style lang="less" scoped>
  .setting-body {
    flex-direction: column;
  }

</style>
