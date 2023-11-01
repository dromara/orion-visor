<template>
  <a-select v-model:model-value="value as any"
            :options="optionData()"
            :allow-search="true"
            :multiple="multiple"
            :loading="loading"
            :disabled="loading"
            :filter-option="filterOption"
            placeholder="请选择用户" />
</template>

<script lang="ts">
  export default {
    name: 'user-selector'
  };
</script>

<script lang="ts" setup>
  import type { PropType } from 'vue';
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { computed } from 'vue';
  import { useCacheStore } from '@/store';
  import { RoleStatus } from '@/views/user/role/types/const';

  const props = defineProps({
    modelValue: [Number, Array] as PropType<number | Array<number>>,
    loading: Boolean,
    multiple: Boolean,
  });

  const emits = defineEmits(['update:modelValue']);

  const value = computed({
    get() {
      return props.modelValue;
    },
    set(e) {
      emits('update:modelValue', e);
    }
  });

  // 选项数据
  const cacheStore = useCacheStore();
  const optionData = (): SelectOptionData[] => {
    return cacheStore.users.map(s => {
      return {
        label: `${s.nickname} (${s.username})`,
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
