<template>
  <v-charts v-if="renderChart"
            :option="options"
            :autoresize="autoResize"
            :style="{ width, height }" />
</template>

<script lang="ts" setup>
  import type { EChartsOption } from 'echarts';
  import { nextTick, ref } from 'vue';
  import { useAppStore } from '@/store';
  import VCharts from 'vue-echarts';

  const props = withDefaults(defineProps<Partial<{
    options: EChartsOption,
    autoResize: boolean,
    width: string,
    height: string,
  }>>(), {
    options: () => {
      return {};
    },
    autoResize: true,
    width: '100%',
    height: '100%',
  });

  const appStore = useAppStore();

  const renderChart = ref(false);

  nextTick(() => {
    renderChart.value = true;
  });

</script>

<style lang="less" scoped>
</style>
