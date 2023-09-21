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
  import { computed } from 'vue';
  import { useCacheStore } from '@/store';
  import { SelectOptionData } from '@arco-design/web-vue';

  const props = defineProps({
    modelValue: Number,
  });

  const emits = defineEmits(['update:modelValue']);

  const value = computed({
    get() {
      return props.modelValue;
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

<style scoped>

</style>
