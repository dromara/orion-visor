<template>
  <a-select v-model:model-value="value as any"
            :options="optionData()"
            :allow-search="true"
            :multiple="multiple"
            :loading="loading"
            :disabled="loading"
            :filter-option="labelFilter"
            placeholder="请选择角色" />
</template>

<script lang="ts">
  export default {
    name: 'user-role-selector'
  };
</script>

<script lang="ts" setup>
  import type { PropType } from 'vue';
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { computed } from 'vue';
  import { useCacheStore } from '@/store';
  import { RoleStatus } from '@/views/user/role/types/const';
  import { labelFilter } from '@/types/form';

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
    return cacheStore.roles.map(s => {
      return {
        label: `${s.name} (${s.code})`,
        disabled: s.status === RoleStatus.DISABLED,
        value: s.id,
      };
    });
  };

</script>

<style lang="less" scoped>

</style>
