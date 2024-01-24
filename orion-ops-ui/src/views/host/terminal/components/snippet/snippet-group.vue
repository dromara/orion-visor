<template>
  <a-collapse :bordered="false">
    <a-collapse-item v-for="group in snippet.groups"
                     :key="group.id"
                     :header="group.name">
      <!-- 总量 -->
      <template #extra>
        {{ 1 }} 条
      </template>
      <snippet-item v-for="item in snippet.items"
                    :key="item.id"
                    :item="item" />
    </a-collapse-item>
  </a-collapse>
</template>

<script lang="ts">
  export default {
    name: 'snippetGroup'
  };
</script>

<script lang="ts" setup>
  import type { CommandSnippetWrapperResponse } from '@/api/asset/command-snippet';
  import SnippetItem from './snippet-item.vue';

  defineProps<{
    snippet: CommandSnippetWrapperResponse
  }>();

</script>

<style lang="less" scoped>
  :deep(.arco-collapse-item) {
    border: none;

    .arco-collapse-item-header-title {
      user-select: none;
    }

    .arco-collapse-item-header {
      border: none;
    }

    .arco-collapse-item-content {
      background-color: unset;
      padding: 0;
    }

    .arco-collapse-item-content-box {
      padding: 0;
    }
  }
</style>
