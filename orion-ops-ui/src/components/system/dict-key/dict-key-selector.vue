<template>
  <a-select v-model:model-value="value as any"
            :options="optionData"
            :allow-search="true"
            :loading="loading"
            :disabled="loading"
            :filter-option="labelFilter"
            :allow-create="allowCreate"
            placeholder="请选择配置项" />
</template>

<script lang="ts">
  export default {
    name: 'dict-key-selector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { computed, onBeforeMount, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import { labelFilter } from '@/types/form';
  import useLoading from '@/hooks/loading';

  const props = defineProps({
    modelValue: Number,
    allowCreate: {
      type: Boolean,
      default: false
    }
  });

  const emits = defineEmits(['update:modelValue', 'change']);

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const value = computed({
    get() {
      return props.modelValue;
    },
    set(e) {
      if (typeof e === 'string') {
        // 创建的值 这里必须为 null, 否则 table 的重置不生效
        emits('update:modelValue', null);
        emits('change', {
          id: null,
          keyName: e
        });
      } else {
        // 已有的值
        emits('update:modelValue', e);
        const find = optionData.value.find(s => s.value === e);
        if (find) {
          emits('change', find.origin);
        }
      }
    }
  });
  const optionData = ref<Array<SelectOptionData>>([]);

  // 初始化选项
  onBeforeMount(async () => {
    setLoading(true);
    try {
      const dictKeys = await cacheStore.loadDictKeys();
      optionData.value = dictKeys.map(s => {
        return {
          label: `${s.keyName} - ${s.description || ''}`,
          value: s.id,
          origin: s
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
