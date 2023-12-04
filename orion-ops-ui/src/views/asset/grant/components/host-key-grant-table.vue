<template>
  <a-table row-key="id"
           class="host-key-main-table"
           label-align="left"
           :columns="hostKeyColumns"
           v-model:selected-keys="selectedKeys"
           :row-selection="rowSelection"
           :sticky-header="true"
           :data="hostKeys"
           :pagination="false"
           :bordered="false" />
</template>

<script lang="ts">
  export default {
    name: 'host-key-grant-table'
  };
</script>

<script lang="ts" setup>
  import type { PropType } from 'vue';
  import type { HostKeyQueryResponse } from '@/api/asset/host-key';
  import { hostKeyColumns } from '../types/table.columns';
  import { useRowSelection } from '@/types/table';
  import { computed, ref, onMounted } from 'vue';
  import { useCacheStore } from '@/store';

  const props = defineProps({
    modelValue: {
      type: Array as PropType<Array<number>>,
      default: () => []
    }
  });
  const emits = defineEmits(['loading', 'update:modelValue']);

  const cacheStore = useCacheStore();
  const rowSelection = useRowSelection();

  const hostKeys = ref<Array<HostKeyQueryResponse>>([]);

  const selectedKeys = computed({
    get() {
      return props.modelValue;
    },
    set(e) {
      emits('update:modelValue', e);
    }
  });

  // 初始化数据
  onMounted(async () => {
    emits('loading', true);
    try {
      hostKeys.value = await cacheStore.loadHostKeys();
    } catch (e) {
    } finally {
      emits('loading', false);
    }
  });

</script>

<style lang="less" scoped>

</style>
