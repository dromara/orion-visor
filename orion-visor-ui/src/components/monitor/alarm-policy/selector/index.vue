<template>
  <a-select v-model:model-value="modelValue"
            :options="optionData"
            :allow-search="true"
            :loading="loading"
            :disabled="loading"
            placeholder="请选择告警策略" />
</template>

<script lang="ts">
  export default {
    name: 'alarmPolicySelector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { onActivated, onMounted, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import useLoading from '@/hooks/loading';

  const props = withDefaults(defineProps<Partial<{
    type?: string;
  }>>(), {
    type: 'all',
  });

  const modelValue = defineModel({ type: Number });

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const optionData = ref<Array<SelectOptionData>>([]);

  // 初始化选项
  const initOptions = async () => {
    setLoading(true);
    try {
      const values = await cacheStore.loadMonitorAlarmPolicy(props.type);
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
