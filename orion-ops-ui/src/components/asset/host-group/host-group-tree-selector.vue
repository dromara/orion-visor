<template>
  <a-tree-select v-model="value"
                 :multiple="true"
                 :data="treeData"
                 placeholder="请选择主机分组"
                 :allow-clear="true"
                 :allow-search="true">
  </a-tree-select>
</template>

<script lang="ts">
  export default {
    name: 'host-group-tree-selector'
  };
</script>

<script lang="ts" setup>
  import type { TreeNodeData } from '@arco-design/web-vue';
  import type { PropType } from 'vue';
  import { computed, onBeforeMount, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import { getHostGroupTree } from '@/api/asset/host-group';

  const props = defineProps({
    modelValue: Array as PropType<Array<Number>>,
  });

  const emits = defineEmits(['update:modelValue']);

  const cacheStore = useCacheStore();

  const value = computed<Array<number>>({
    get() {
      return props.modelValue as Array<number>;
    },
    set(e) {
      if (e) {
        emits('update:modelValue', e);
      } else {
        emits('update:modelValue', null);
      }
    }
  });
  const treeData = ref<Array<TreeNodeData>>([]);

  // 初始化选项
  onBeforeMount(async () => {
    if (cacheStore.hostGroups.length) {
      treeData.value = cacheStore.hostGroups;
    } else {
      // 加载数据
      const { data } = await getHostGroupTree();
      treeData.value = data;
      cacheStore.set('hostGroups', data);
    }
  });

</script>

<style lang="less" scoped>

</style>
