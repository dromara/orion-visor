<template>
  <a-tree-select v-model:model-value="value"
                 :data="treeData"
                 :disabled="disabled"
                 :loading="loading"
                 :allow-search="true"
                 :filter-tree-node="titleFilter"
                 placeholder="请选择菜单" />
</template>

<script lang="ts">
  export default {
    name: 'menuTreeSelector'
  };
</script>

<script lang="ts" setup>
  import type { TreeNodeData } from '@arco-design/web-vue';
  import { useCacheStore } from '@/store';
  import { computed, onBeforeMount, ref } from 'vue';
  import { MenuType } from '@/views/system/menu/types/const';
  import { titleFilter } from '@/types/form';
  import useLoading from '@/hooks/loading';

  const props = defineProps<{
    modelValue: number;
    disabled: boolean;
  }>();

  const emits = defineEmits(['update:modelValue']);

  const { loading, setLoading } = useLoading();
  const cacheStore = useCacheStore();

  const treeData = ref<Array<TreeNodeData>>([]);

  const value = computed({
    get() {
      return props.modelValue;
    },
    set(e) {
      emits('update:modelValue', e);
    }
  });

  onBeforeMount(async () => {
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

    // 加载数据
    try {
      setLoading(true);
      const menus = await cacheStore.loadMenus();
      treeData.value = [{
        key: 0,
        title: '根目录',
        children: render([...menus])
      }];
    } catch (e) {
    } finally {
      setLoading(false);
    }
  });

</script>

<style lang="less" scoped>

</style>;
