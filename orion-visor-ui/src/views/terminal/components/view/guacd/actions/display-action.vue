<template>
  <!-- 分辨率 -->
  <a-space>
    <span class="display-size-label">分辨率</span>
    <a-select v-model="displaySize"
              class="display-size-input"
              placeholder="请选择分辨率"
              :options="toOptions(screenResolutionKey)"
              allow-create />
  </a-space>
  <!-- 按钮 -->
  <a-space class="action-bar-content-footer">
    <a-button type="primary"
              size="small"
              @click="fitOnce">
      临时自适应
    </a-button>
    <a-button type="primary"
              size="small"
              @click="setDisplaySize">
      设置
    </a-button>
  </a-space>
</template>

<script lang="ts">
  export default {
    name: 'displayAction'
  };
</script>

<script lang="ts" setup>
  import type { IGuacdSession } from '@/views/terminal/interfaces';
  import { ref, onMounted } from 'vue';
  import { useDictStore } from '@/store';
  import { getDisplaySize } from '@/views/terminal/types/utils';
  import { screenResolutionKey, fitDisplayValue } from '@/views/terminal/types/const';

  const { toOptions, getDictValue } = useDictStore();

  const props = defineProps<{
    session: IGuacdSession;
  }>();
  const emits = defineEmits(['close']);

  const displaySize = ref(fitDisplayValue);

  // 临时自适应
  const fitOnce = () => {
    props.session.displayHandler?.fit(true);
    emits('close');
  };

  // 设置显示大小
  const setDisplaySize = () => {
    const displayHandler = props.session.displayHandler;
    if (!displayHandler) {
      return;
    }
    if (displaySize.value === fitDisplayValue) {
      // 设置自适应
      displayHandler.autoFit = true;
      displayHandler.fit(true);
    } else {
      try {
        // 获取大小
        const [width, height] = getDisplaySize(displaySize.value, true);
        // 取消自适应
        displayHandler.autoFit = false;
        // 设置大小
        displayHandler.resize(width, height);
      } catch (e) {
        return;
      }
    }
    emits('close');
  };

  // 初始化
  onMounted(() => {
    if (props.session.displayHandler?.autoFit) {
      displaySize.value = fitDisplayValue;
    } else {
      displaySize.value = `${props.session.displayHandler?.displayWidth || 0}x${props.session.displayHandler?.displayHeight || 0}`;
    }
  });

</script>

<style lang="less" scoped>
</style>
