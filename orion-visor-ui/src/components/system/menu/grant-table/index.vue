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
    <a-checkbox-group v-model="checkedKeys" style="display: contents;">
      <template v-for="parentMenu in menuData" :key="parentMenu.id">
        <!-- 有子菜单 -->
        <template v-if="parentMenu.children?.length">
          <tr v-for="(childrenMenu, i) in parentMenu.children"
              :key="childrenMenu.id">
            <!-- 父菜单 -->
            <td v-if="i === 0" :rowspan="parentMenu.children.length">
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
        <!-- 无子菜单 -->
        <template v-else>
          <tr>
            <!-- 父菜单 -->
            <td>
              <a-checkbox :value="parentMenu.id">
                {{ parentMenu.name }}
              </a-checkbox>
            </td>
            <!-- 子菜单 -->
            <td>
            </td>
            <!-- 功能 -->
            <td>
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
    name: 'menuGrantTable'
  };
</script>

<script lang="ts" setup>
  import type { MenuQueryResponse } from '@/api/system/menu';
  import { useCacheStore } from '@/store';
  import { ref, watch } from 'vue';
  import { findNode, flatNodeKeys, flatNodes } from '@/utils/tree';

  const cacheStore = useCacheStore();

  const unTriggerChange = ref(false);
  const menuData = ref<Array<MenuQueryResponse>>([]);
  const checkedKeys = ref<Array<number>>([]);

  // 初始化
  const init = (keys: Array<number>) => {
    unTriggerChange.value = true;
    checkedKeys.value = keys;
    if (!menuData.value.length) {
      cacheStore.loadMenus().then(menus => {
        menuData.value = [...menus];
      });
    }
  };

  // 获取值
  const getValue = () => {
    return checkedKeys.value;
  };

  // 通过规则 选择/取消选择
  const checkOrUncheckByFilter = (filter: undefined | ((perm: string) => boolean), check: boolean) => {
    unTriggerChange.value = true;
    const nodes: Array<MenuQueryResponse> = [];
    flatNodes(menuData.value, nodes);
    if (filter) {
      const ruleNodes = nodes.filter(s => s.permission)
        .filter(s => filter(s?.permission))
        .map(s => s.id)
        .filter(Boolean);
      if (check) {
        // 选择
        checkedKeys.value = [...new Set([...checkedKeys.value, ...ruleNodes])];
      } else {
        // 取消选择
        checkedKeys.value = [...checkedKeys.value].filter(s => !ruleNodes.includes(s));
      }
    } else {
      if (check) {
        // 选择
        checkedKeys.value = nodes.map(s => s.id).filter(Boolean);
      } else {
        // 取消选择
        checkedKeys.value = [];
      }
    }
  };

  defineExpose({ init, getValue, checkOrUncheckByFilter });

  // 监听级联变化
  watch(checkedKeys, (after: Array<number>, before: Array<number>) => {
    if (unTriggerChange.value) {
      unTriggerChange.value = false;
      return;
    }
    const afterLength = after.length;
    const beforeLength = before.length;
    if (afterLength > beforeLength) {
      // 选择 一定是最后一个
      checkOrUncheckMenu(after[afterLength - 1], true);
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
      checkOrUncheckMenu(uncheckedId, false);
    }
  });

  // 级联选择/取消选择菜单
  const checkOrUncheckMenu = (id: number, check: boolean) => {
    unTriggerChange.value = true;
    // 查询当前节点
    const node = findNode(id, menuData.value, 'id');
    if (!node) {
      return;
    }
    const childrenId: number[] = [];
    // 获取所在子节点id
    flatNodeKeys(node.children, childrenId, 'id');
    if (check) {
      // 选中
      checkedKeys.value = [...new Set([...checkedKeys.value, ...childrenId])];
    } else {
      // 取消选择
      checkedKeys.value = checkedKeys.value.filter(s => !childrenId.includes(s));
    }
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
        border: 1px solid var(--color-fill-3);
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
