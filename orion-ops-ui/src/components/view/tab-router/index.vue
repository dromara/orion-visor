<template>
  <div class="tabs-container">
    <template v-for="item in items"
              :key="item.key">
      <span v-permission="item.permission || []"
            :title="item.text"
            :class="['tab-item', item.key === modelValue ? 'tab-item-active' : '']"
            @click="changeTab(item)">
        <component v-if="item.icon"
                   :is="item.icon" />
        {{ item.text }}
      </span>
    </template>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'tab-router'
  };
</script>

<script lang="ts" setup>
  import type { PropType } from 'vue';
  import type { TabRouterItem } from './types';
  import usePermission from '@/hooks/permission';
  import { onMounted, } from 'vue';

  const permission = usePermission();

  const props = defineProps({
    items: Array as PropType<Array<TabRouterItem>>,
    modelValue: [String, Number]
  });

  const emits = defineEmits(['update:modelValue', 'change']);

  // 切换 tab
  const changeTab = ({ key, text }: TabRouterItem) => {
    if (key === props.modelValue) {
      return;
    }
    emits('update:modelValue', key);
    emits('change', key, text);
  };

  onMounted(() => {
    // 获取有权限的 key
    const items = props.items?.filter(s => !s.permission || !s.permission.length || permission.hasAnyPermission(s.permission)) || [];
    if (!items.length) {
      return;
    }
    // 设置默认选中
    if (items.map(s => s.key).indexOf(props.modelValue as string | number) === -1) {
      const item = items[0];
      emits('update:modelValue', item.key);
      emits('change', item.key, item.text);
    } else {
      // 触发 change 事件
      const matchItem = items.find(s => s.key === props.modelValue);
      if (matchItem) {
        emits('change', matchItem.key, matchItem.text);
      }
    }
  });

</script>

<style lang="less" scoped>
  .tabs-container {
    display: flex;
    flex-direction: column;
    user-select: none;
    background: var(--color-bg-2);
  }

  .tab-item {
    display: flex;
    height: 32px;
    margin: 12px 12px 0 12px;
    align-items: center;
    padding: 5px 16px;
    cursor: pointer;
    border-radius: 32px;
    font-size: 14px;
    color: var(--color-text-2);

    &:first-child {
      margin-top: 0;
    }

    svg {
      margin-right: 4px;
      font-size: 16px;
    }

    &:hover {
      background: var(--color-fill-3);
    }
  }

  .tab-item-active {
    color: rgb(var(--primary-6));
    background: var(--color-fill-2);
  }
</style>
