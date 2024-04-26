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
        <!-- snippet -->
        <template v-for="item in group.items">
          <command-snippet-list-item v-if="item.visible"
                                     :key="item.id"
                                     :item="item" />
        </template>
      </a-collapse-item>
    </template>
  </a-collapse>
</template>

<script lang="ts">
  export default {
    name: 'commandSnippetListGroup'
  };
</script>

<script lang="ts" setup>
  import type { CommandSnippetGroupQueryResponse } from '@/api/asset/command-snippet-group';
  import type { CommandSnippetWrapperResponse } from '@/api/asset/command-snippet';
  import CommandSnippetListItem from './command-snippet-list-item.vue';

  defineProps<{
    value: CommandSnippetWrapperResponse;
  }>();

  // 计算总量
  const calcTotal = (group: CommandSnippetGroupQueryResponse) => {
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
