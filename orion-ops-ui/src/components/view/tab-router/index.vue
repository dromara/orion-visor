<template>
  <div class="simple-card tabs-container">
    <template v-for="item in items"
              :key="item.key">
      <span v-permission="item.permission"
            :title="item.text"
            :class="['tab-item', item.key === modelValue ? 'tab-item-active' : '']"
            @click="changeTab(item.key)">
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
  const changeTab = (key: string | number) => {
    if (key === props.modelValue) {
      return;
    }
    emits('update:modelValue', key);
    emits('change', key);
  };

  onMounted(() => {
    // 获取有权限的 key
    const keys = props.items?.filter(s => !s.permission || !s.permission.length || permission.hasAnyPermission(s.permission))
    .map(s => s.key) || [];
    if (!keys.length) {
      return;
    }
    // 设置默认选中
    if (keys.indexOf(props.modelValue as string | number) === -1) {
      emits('update:modelValue', keys[0]);
    }
  });

</script>

<style lang="less" scoped>
  .tabs-container {
    display: flex;
    flex-direction: column;
    user-select: none;
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
