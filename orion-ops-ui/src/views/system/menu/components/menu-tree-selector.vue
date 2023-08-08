<template>
  <a-tree-select :default-value="modelValue"
                 :data="treeData"
                 :disabled="disabled"
                 :allow-search="true"
                 :filter-tree-node="filterTreeNode"
                 placeholder="请选择上级菜单"
                 @change="onChange" />
</template>

<script lang="ts">
  export default {
    name: 'menu-tree-selector'
  };
</script>

<script lang="ts" setup>
  import { useMenuStore } from '@/store';

  defineProps(['modelValue', 'disabled']);
  const emits = defineEmits(['update:modelValue']);
  const onChange = (e: any) => {
    emits('update:modelValue', e);
  };

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
