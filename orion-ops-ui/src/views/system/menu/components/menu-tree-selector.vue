<template>
  <a-tree-select v-model:model-value="value"
                 :data="treeData()"
                 :disabled="disabled"
                 :allow-search="true"
                 :filter-tree-node="filterTreeNode"
                 placeholder="请选择菜单" />
</template>

<script lang="ts">
  export default {
    name: 'menu-tree-selector'
  };
</script>

<script lang="ts" setup>
  import type { TreeNodeData } from '@arco-design/web-vue';
  import { useCacheStore } from '@/store';
  import { computed } from 'vue';
  import { MenuType } from '../types/const';

  const props = defineProps({
    modelValue: Number,
    disabled: Boolean,
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
  const cacheStore = useCacheStore();
  const treeData = (): TreeNodeData[] => {
    let render = (arr: any[]): TreeNodeData[] => {
      return arr.map((s) => {
        // 为 function 返回空
        if (s.type === MenuType.FUNCTION) {
          return null as unknown as TreeNodeData;
        }
        // 当前节点
        const node = {
          key: s.id,
          title: s.name,
          children: undefined as unknown
        } as TreeNodeData;
        // 子节点
        if (s.children && s.children.length) {
          node.children = render(s.children);
        }
        return node;
      }).filter(Boolean);
    };
    return [{
      key: 0,
      title: '根目录',
      children: render([...cacheStore.menus])
    }];
  };

  // 搜索
  const filterTreeNode = (searchValue: string, nodeData: { title: string; }) => {
    return nodeData.title.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
  };

</script>

<style scoped>

</style>;
