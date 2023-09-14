<template>
  <div>
    <!-- 按钮组 -->
    <a-button-group class="mb4">
      <!-- 全选 -->
      <a-button type="text" size="mini" @click="toggleChecked">
        {{ checkedKeys?.length === allCheckedKeys?.length ? '反选' : '全选' }}
      </a-button>
      <!-- 展开 -->
      <a-button type="text" size="mini" @click="toggleExpanded">
        {{ expandedKeys?.length ? '折叠' : '展开' }}
      </a-button>
    </a-button-group>
    <!-- 菜单树 -->
    <a-tree
      checked-strategy="child"
      :checkable="true"
      :animation="false"
      v-model:checked-keys="checkedKeys"
      v-model:expanded-keys="expandedKeys"
      :data="treeData" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'menu-selector-tree'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { TreeNodeData } from '@arco-design/web-vue';
  import { useCacheStore } from '@/store';

  const treeData = ref<Array<TreeNodeData>>([]);

  const allCheckedKeys = ref<Array<number>>([]);
  const allExpandedKeys = ref<Array<number>>([]);

  const checkedKeys = ref<Array<number>>([]);
  const expandedKeys = ref<Array<number>>([]);

  // 修改选中状态
  const toggleChecked = () => {
    checkedKeys.value = checkedKeys.value.length === allCheckedKeys.value.length ? [] : allCheckedKeys.value;
  };

  // 修改折叠状态
  const toggleExpanded = () => {
    expandedKeys.value = expandedKeys?.value.length ? [] : allExpandedKeys.value;
  };

  // 循环选中的 key
  const eachAllCheckKeys = (arr: Array<any>) => {
    arr.forEach((item) => {
      allCheckedKeys.value.push(item.key);
      if (item.children && item.children.length) {
        eachAllCheckKeys(item.children);
      }
    });
  };

  // 循环展开的 key
  const eachAllExpandKeys = (arr: Array<any>) => {
    arr.forEach((item) => {
      if (item.children && item.children.length) {
        allExpandedKeys.value.push(item.key);
        eachAllExpandKeys(item.children);
      }
    });
  };

  // 渲染数据
  const init = (keys: Array<number>) => {
    // 初始化数据
    allCheckedKeys.value = [];
    allExpandedKeys.value = [];
    checkedKeys.value = keys;
    expandedKeys.value = [];

    // 渲染菜单
    const cacheStore = useCacheStore();
    let render = (arr: any[]): TreeNodeData[] => {
      return arr.map((s) => {
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

    // 加载菜单
    treeData.value = render([...cacheStore.menus]);
    // 加载所有选中的key
    eachAllCheckKeys(treeData.value);
    // 加载所有展开的key
    eachAllExpandKeys(treeData.value);
  };
  init([]);

  // 获取值
  const getValue = () => {
    if (!checkedKeys.value.length) {
      return [];
    }
    // 查询子节点上级父节点
    const mixed: number[] = [];
    const findParent = (arr: Array<TreeNodeData>, key: number) => {
      for (let node of arr) {
        // 是子节点 并且相同
        if (node.key === key) {
          mixed.push(key);
          return true;
        }
        if (node.children?.length) {
          const isFind = findParent(node.children, key);
          if (isFind) {
            mixed.push(node.key as number);
            return true;
          }
        }
      }
      return false;
    };

    // 设置所有节点
    for (let key of checkedKeys.value) {
      findParent(treeData.value, key);
    }
    return new Set(mixed);
  };

  defineExpose({ init, getValue });

</script>

<style scoped>

</style>;
