<template>
  <a-select v-model:model-value="value"
            :options="optionData()"
            placeholder="请选择主机身份"
            allow-clear />
</template>

<script lang="ts">
  export default {
    name: 'host-identity-selector'
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
    return cacheStore.hostIdentities.map(s => {
      return {
        label: `${s.name} (${s.username})`,
        value: s.id,
      };
    });
  };
</script>

<style scoped>

</style>
