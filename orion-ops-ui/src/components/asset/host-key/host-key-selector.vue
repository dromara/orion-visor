<template>
  <a-select v-model:model-value="value"
            :options="optionData()"
            placeholder="请选择主机秘钥"
            allow-clear />
</template>

<script lang="ts">
  export default {
    name: 'host-key-selector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { computed } from 'vue';
  import { useCacheStore } from '@/store';

  const props = defineProps({
    modelValue: Number,
  });

  const emits = defineEmits(['update:modelValue']);

  const value = computed<number>({
    get() {
      return props.modelValue as number;
    },
    set(e) {
      if (e) {
        emits('update:modelValue', e);
      } else {
        emits('update:modelValue', null);
      }
    }
  });

  // 选项数据
  const cacheStore = useCacheStore();
  const optionData = (): SelectOptionData[] => {
    return cacheStore.hostKeys.map(s => {
      return {
        label: s.name,
        value: s.id,
      };
    });
  };
</script>

<style lang="less" scoped>

</style>
