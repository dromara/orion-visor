<template>
  <div class="terminal-setting-block">
    <!-- 顶部 -->
    <div class="terminal-setting-subtitle-wrapper">
      <h3 class="terminal-setting-subtitle">
        VNC 图形化设置
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
                    style="width: 168px;"
                    size="small"
                    :options="toOptions(screenResolutionKey)"
                    allow-create />
        </block-setting-item>
        <!-- 颜色深度 -->
        <block-setting-item label="颜色深度" desc="显示的颜色深度, 越高显示效果越好">
          <a-select v-model="formModel.colorDepth"
                    style="width: 168px;"
                    size="small"
                    :options="toOptions(graphColorDepthKey)" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 无损压缩 -->
        <block-setting-item label="无损压缩" desc="是否启用对图像更新的无损压缩">
          <a-switch v-model="formModel.forceLossless" type="round" />
        </block-setting-item>
        <!-- 光标 -->
        <block-setting-item label="光标" desc="光标渲染方式, 远程光标会比本地光标慢">
          <a-select v-model="formModel.cursor"
                    style="width: 168px;"
                    size="small"
                    :options="toOptions(vcnCursorKey)" />
        </block-setting-item>
      </a-row>
      <a-row class="mb16" align="stretch" :gutter="16">
        <!-- 压缩等级 -->
        <block-setting-item label="压缩等级" desc="设置服务器请求的压缩级别 0表示不压缩, 9表示最高压缩级别">
          <a-input-number v-model="formModel.compressLevel"
                          style="width: 168px;"
                          size="small"
                          mode="button"
                          :min="0"
                          :max="9"
                          placeholder="图像压缩等级 0 ~ 9" />
        </block-setting-item>
        <!-- 质量等级 -->
        <block-setting-item label="质量等级" desc="设置图像的质量等级 0表示最低的图像质量, 9表示最高的图像质量">
          <a-input-number v-model="formModel.qualityLevel"
                          style="width: 168px;"
                          size="small"
                          mode="button"
                          :min="0"
                          :max="9"
                          placeholder="图像质量等级 0 ~ 9" />
        </block-setting-item>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'terminalVncGraphBlock'
  };
</script>

<script lang="ts" setup>
  import type { TerminalVncGraphSetting } from '@/store/modules/terminal/types';
  import { useDictStore } from '@/store';
  import { TerminalPreferenceItem } from '@/store/modules/terminal';
  import { graphColorDepthKey, vcnCursorKey, fitDisplayValue, screenResolutionKey } from '@/views/terminal/types/const';
  import { getDisplaySize } from '@/views/terminal/types/utils';
  import useTerminalPreference from '@/views/terminal/types/use-terminal-preference';
  import BlockSettingItem from '../block-setting-item.vue';

  const { toOptions, getDictValue } = useDictStore();

  const { formModel } = useTerminalPreference<TerminalVncGraphSetting>(TerminalPreferenceItem.VNC_GRAPH_SETTING, true, (v) => {
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
  });

</script>

<style lang="less" scoped>
  .setting-body {
    flex-direction: column;
  }

  :deep(.arco-input-number) {
    input {
      text-align: center;
    }
  }
</style>
