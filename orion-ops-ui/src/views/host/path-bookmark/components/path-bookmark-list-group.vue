<template>
  <a-collapse :bordered="false">
    <template v-for="group in value.groups">
      <a-collapse-item v-if="calcTotal(group) > 0"
                       :key="group.id"
                       :header="group.name">
        <!-- 总量 -->
        <template #extra>
          {{ calcTotal(group) }} 条
        </template>
        <!-- 路径 -->
        <template v-for="item in group.items">
          <path-bookmark-list-item v-if="item.visible"
                                   :key="item.id"
                                   :item="item" />
        </template>
      </a-collapse-item>
    </template>
  </a-collapse>
</template>

<script lang="ts">
  export default {
    name: 'pathBookmarkListGroup'
  };
</script>

<script lang="ts" setup>
  import type { PathBookmarkGroupQueryResponse } from '@/api/asset/path-bookmark-group';
  import type { PathBookmarkWrapperResponse } from '@/api/asset/path-bookmark';
  import PathBookmarkListItem from './path-bookmark-list-item.vue';

  defineProps<{
    value: PathBookmarkWrapperResponse;
  }>();

  // 计算总量
  const calcTotal = (group: PathBookmarkGroupQueryResponse) => {
    return group.items.filter(s => s.visible).length;
  };

</script>

<style lang="less" scoped>
  :deep(.arco-collapse-item) {
    border: none;

    &-header {
      border: none;

      &-title {
        user-select: none;
      }

      &-extra {
        user-select: none;
      }
    }

    &-content {
      background-color: unset;
      padding: 0;
    }

    &-content-box {
      padding: 0;
    }
  }
</style>
