<template>
  <a-select v-model:model-value="value as any"
            :options="optionData"
            :allow-search="true"
            :multiple="multiple"
            :loading="loading"
            :disabled="loading"
            :filter-option="labelFilter"
            placeholder="请选择用户" />
</template>

<script lang="ts">
  export default {
    name: 'userSelector'
  };
</script>

<script lang="ts" setup>
  import type { PropType } from 'vue';
  import type { SelectOptionData } from '@arco-design/web-vue';
  import { computed, ref, onMounted } from 'vue';
  import { useCacheStore } from '@/store';
  import { labelFilter } from '@/types/form';
  import useLoading from '@/hooks/loading';

  const props = defineProps({
    modelValue: [Number, Array] as PropType<number | Array<number>>,
    multiple: Boolean,
  });

  const emits = defineEmits(['update:modelValue']);

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const value = computed({
    get() {
      return props.modelValue;
    },
    set(e) {
      emits('update:modelValue', e);
    }
  });
  const optionData = ref<Array<SelectOptionData>>([]);

  // 初始化选项
  onMounted(async () => {
    setLoading(true);
    try {
      // 加载用户列表
      const users = await cacheStore.loadUsers();
      optionData.value = users.map(s => {
        return {
          label: `${s.nickname} (${s.username})`,
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
