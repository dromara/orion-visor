<template>
  <a-tree-select v-model:model-value="value"
                 :data="treeData"
                 :disabled="disabled"
                 :allow-search="true"
                 :filter-tree-node="filterTreeNode"
                 placeholder="请选择上级菜单" />
</template>

<script lang="ts">
  export default {
    name: 'menu-tree-selector'
  };
</script>

<script lang="ts" setup>
  import { useMenuStore } from '@/store';
  import { computed } from 'vue';

  const props = defineProps({
    modelValue: Number,
    disabled: Boolean
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

  // 树数据
  const menuStore = useMenuStore();
  const treeData = menuStore.treeData;

  // 搜索
  const filterTreeNode = (searchValue: string, nodeData: { title: string; }) => {
    return nodeData.title.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
  };

</script>

<style scoped>

</style>;
