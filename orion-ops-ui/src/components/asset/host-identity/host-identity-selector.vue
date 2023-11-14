<template>
  <a-select v-model:model-value="value"
            :options="optionData"
            placeholder="请选择主机身份"
            allow-clear />
</template>

<script lang="ts">
  export default {
    name: 'host-identity-selector'
  };
</script>

<script lang="ts" setup>
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { computed, onBeforeMount, ref } from 'vue';
  import { useCacheStore } from '@/store';

  const props = defineProps({
    modelValue: Number,
  });

  const emits = defineEmits(['update:modelValue']);

  const cacheStore = useCacheStore();

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
  const optionData = ref<Array<SelectOptionData>>([]);

  // 初始化选项
  onBeforeMount(() => {
    optionData.value = cacheStore.hostIdentities.map(s => {
      return {
        label: `${s.name} (${s.username})`,
        value: s.id,
      };
    });
  });

</script>

<style lang="less" scoped>

</style>
