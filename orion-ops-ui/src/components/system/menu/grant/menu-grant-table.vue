<template>
  <table class="grant-table" border="1">
    <thead>
    <tr class="header-row">
      <th class="parent-view-header">父页面</th>
      <th class="child-view-header">子页面</th>
      <th>功能</th>
    </tr>
    </thead>
    <tbody>
    <a-checkbox-group v-model="checkedKeys" style="display: contents">
      <template v-for="parentMenu in menuData" :key="parentMenu.id">
        <template v-for="(childrenMenu, index) in parentMenu.children" :key="childrenMenu.id">
          <tr>
            <!-- 父菜单 -->
            <td v-if="index === 0" :rowspan="parentMenu.children.length">
              <a-checkbox :value="parentMenu.id">
                {{ parentMenu.name }}
              </a-checkbox>
            </td>
            <!-- 子菜单 -->
            <td>
              <a-checkbox :value="childrenMenu.id">
                {{ childrenMenu.name }}
              </a-checkbox>
            </td>
            <!-- 功能 -->
            <td>
              <a-row v-if="childrenMenu.children && childrenMenu.children.length">
                <a-col v-for="item in childrenMenu.children"
                       :key="item.id"
                       :span="8">
                  <a-checkbox :value="item.id">
                    {{ item.name }}
                  </a-checkbox>
                </a-col>
              </a-row>
            </td>
          </tr>
        </template>
      </template>
    </a-checkbox-group>
    </tbody>
  </table>
</template>

<script lang="ts">
  export default {
    name: 'menu-grant-table'
  };
</script>

<script lang="ts" setup>
  import type { MenuQueryResponse } from '@/api/system/menu';
  import { useCacheStore } from '@/store';
  import { ref, watch } from 'vue';

  const cacheStore = useCacheStore();

  const isFirst = ref(false);
  const menuData = ref<Array<MenuQueryResponse>>([]);
  const checkedKeys = ref<Array<number>>([]);

  // 初始化
  const init = (keys: Array<number>) => {
    isFirst.value = true;
    checkedKeys.value = keys;
    if (!menuData.value.length) {
      menuData.value = [...cacheStore.menus];
    }
  };

  // 获取值
  const getValue = () => {
    return checkedKeys.value;
  };

  defineExpose({ init, getValue });

  // 监听级联变化
  watch(checkedKeys, (after: Array<number>, before: Array<number>) => {
    if (isFirst.value) {
      isFirst.value = false;
      return;
    }
    const afterLength = after.length;
    const beforeLength = before.length;
    if (afterLength > beforeLength) {
      // 选择 一定是最后一个
      checkMenu(after[afterLength - 1]);
    } else if (afterLength < beforeLength) {
      // 取消
      let uncheckedId = null;
      for (let i = 0; i < afterLength; i++) {
        if (after[i] !== before[i]) {
          uncheckedId = before[i];
          break;
        }
      }
      if (uncheckedId == null) {
        uncheckedId = before[beforeLength - 1];
      }
      uncheckMenu(uncheckedId);
    }
  });

  // 寻找当前节点
  const findNode = (id: number, arr: Array<MenuQueryResponse>): MenuQueryResponse | undefined => {
    for (let node of arr) {
      if (node.id === id) {
        return node;
      }
    }
    // 寻找子级
    for (let node of arr) {
      if (node?.children?.length) {
        const inChildNode = findNode(id, node.children);
        if (inChildNode) {
          return inChildNode;
        }
      }
    }
    return undefined;
  };

  // 获取所有子节点id
  const flatChildrenId = (nodes: MenuQueryResponse[] | undefined, result: number[]) => {
    if (!nodes || !nodes.length) {
      return;
    }
    for (let node of nodes) {
      result.push(node.id);
      if (node.children) {
        flatChildrenId(node.children, result);
      }
    }
  };

  // 级联选择菜单
  const checkMenu = (id: number) => {
    // 查询当前节点
    const node = findNode(id, menuData.value);
    const childrenId: number[] = [];
    // 获取所在子节点id
    flatChildrenId(node?.children, childrenId);
    checkedKeys.value.push(...childrenId);
  };

  // 级联取消选择菜单
  const uncheckMenu = (id: number) => {
    // 查询当前节点
    const node = findNode(id, menuData.value);
    const childrenId: number[] = [];
    // 获取所在子节点id
    flatChildrenId(node?.children, childrenId);
    checkedKeys.value = checkedKeys.value.filter(s => !childrenId.includes(s));
  };

</script>

<style lang="less" scoped>

  .grant-table {
    width: 100%;
    border: 1px solid var(--color-fill-3);
    text-indent: initial;
    border-spacing: 2px;
    border-collapse: collapse;
    user-select: none;

    tbody {
      td {
        padding: 6px 16px;
      }
    }
  }

  .header-row {

    th {
      font-size: 17px;
      padding: 4px;
      font-weight: 500;
      text-align: center;
      background-color: var(--color-fill-1);
      color: var(--color-text-2);
    }

    .parent-view-header, .child-view-header {
      width: 200px;
    }
  }

</style>
