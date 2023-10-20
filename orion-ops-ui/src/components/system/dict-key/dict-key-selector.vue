<template>
  <a-select v-model:model-value="value as any"
            :options="optionData()"
            :allow-search="true"
            :loading="loading"
            :disabled="loading"
            :filter-option="filterOption"
            :allow-create="allowCreate"
            placeholder="请选择配置项" />
</template>

<script lang="ts">
  export default {
    name: 'dict-key-selector'
  };
</script>

<script lang="ts" setup>
  import { computed } from 'vue';
  import { useCacheStore } from '@/store';
  import { SelectOptionData } from '@arco-design/web-vue';
  import { DictKeyQueryResponse } from '@/api/system/dict-key';

  const props = defineProps({
    modelValue: Number,
    loading: Boolean,
    allowCreate: {
      type: Boolean,
      default: false
    }
  });

  const emits = defineEmits(['update:modelValue', 'change']);

  const value = computed({
    get() {
      return props.modelValue;
    },
    set(e) {
      if (typeof e === 'string') {
        // 创建的值
        emits('update:modelValue', undefined);
        emits('change', {
          id: undefined,
          keyName: e
        });
      } else {
        // 已有的值
        emits('update:modelValue', e);
        const find = cacheStore.dictKeys.find(s => s.id === e);
        if (find) {
          emits('change', find);
        }
      }
    }
  });

  // 选项数据
  const cacheStore = useCacheStore();
  const optionData = (): SelectOptionData[] => {
    return cacheStore.dictKeys.map(s => {
      return {
        label: `${s.keyName} - ${s.description || ''}`,
        value: s.id,
      };
    });
  };

  // 搜索
  const filterOption = (searchValue: string, option: { label: string; }) => {
    return option.label.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
  };
</script>

<style scoped>

</style>
