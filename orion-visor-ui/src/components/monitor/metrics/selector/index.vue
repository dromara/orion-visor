<template>
  <a-select v-model:model-value="modelValue"
            :options="optionData"
            :allow-search="true"
            :loading="loading"
            :disabled="loading"
            placeholder="请选择监控指标" />
</template>

<script lang="ts">
  export default {
    name: 'monitorMetricsSelector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { onActivated, onMounted, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import useLoading from '@/hooks/loading';

  const modelValue = defineModel({ type: Number });

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const optionData = ref<Array<SelectOptionData>>([]);

  // 初始化选项
  const initOptions = async () => {
    setLoading(true);
    try {
      const values = await cacheStore.loadMonitorMetricsList();
      optionData.value = values.map(s => {
        return {
          label: s.name,
          value: s.id,
        };
      });
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 初始化选项
  onMounted(initOptions);
  onActivated(initOptions);

</script>

<style lang="less" scoped>

</style>
