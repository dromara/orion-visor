<template>
  <a-select v-model:model-value="value as any"
            :options="optionData"
            :allow-search="true"
            :loading="loading"
            :disabled="loading"
            :filter-option="labelFilter"
            :allow-create="allowCreate"
            placeholder="请选择计划任务" />
</template>

<script lang="ts">
  export default {
    name: 'execJobSelector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { computed, onBeforeMount, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import { labelFilter } from '@/types/form';
  import useLoading from '@/hooks/loading';

  const props = withDefaults(defineProps<Partial<{
    modelValue: number;
    name: string;
    allowCreate: boolean;
  }>>(), {
    allowCreate: false,
  });

  const emits = defineEmits(['update:modelValue', 'update:name']);

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const value = computed({
    get() {
      if (props.allowCreate) {
        return props.modelValue || props.name;
      } else {
        return props.modelValue;
      }
    },
    set(e) {
      if (typeof e === 'string') {
        emits('update:modelValue', null);
        emits('update:name', e);
      } else {
        // 已有的值
        emits('update:modelValue', e);
        emits('update:name', null);
      }
    }
  });
  const optionData = ref<Array<SelectOptionData>>([]);

  // 初始化选项
  onBeforeMount(async () => {
    setLoading(true);
    try {
      const dictKeys = await cacheStore.loadExecJobs();
      optionData.value = dictKeys.map(s => {
        return {
          label: s.name,
          value: s.id,
        };
      });
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>

</style>
