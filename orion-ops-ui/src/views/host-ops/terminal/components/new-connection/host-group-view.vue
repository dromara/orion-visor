<template>
  <div class="group-view-container">
    <!-- 主机分组 -->
    <a-scrollbar>
      <a-tree v-model:selected-keys="selectedGroup"
              :data="hosts.groupTree"
              :blockNode="true"
              class="host-tree block-tree"
              @select="chooseGroup">
        <!-- 标题 -->
        <template #extra="node">
          <span class="node-host-count span-blue">{{ hosts?.treeNodes[node.key]?.length || 0 }}</span>
        </template>
      </a-tree>
    </a-scrollbar>
    <!-- 主机列表 -->
    <div class="host-list">
      <a-list size="large"
              max-height="100%"
              :hoverable="true"
              :data="[{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}]">
        <!-- 空数据 -->
        <template #empty>
          <span class="host-list-empty">当前分组未配置主机</span>
        </template>
        <!-- 数据 -->
        <template #item="{ item }">
          <a-list-item :title="`${item.name}(${item.code}) - ${item.address}`">
            {{ hosts?.treeNodes[selectedGroup[0]] }}
            <icon-desktop class="host-list-icon" />
            <span>{{ `${item.name}(${item.code}) - ` }}</span>
            <span class="span-blue">{{ item.address }}</span>
          </a-list-item>
        </template>
      </a-list>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'hostGroupView'
  };
</script>

<script lang="ts" setup>
  import type { AuthorizedHostQueryResponse } from '@/api/asset/asset-authorized-data';
  import { ref } from 'vue';

  const props = defineProps<{
    hosts: AuthorizedHostQueryResponse
  }>();

  const selectedGroup = ref([0]);

  const chooseGroup = () => {
    console.log(selectedGroup.value[0]);
  };

</script>

<style lang="less" scoped>
  @tree-width: 298px;
  @tree-gap: 32px;

  .group-view-container {
    display: flex;
    justify-content: space-between;
    max-height: 100%;
    width: 100%;

    .host-tree {
      min-width: 100%;
      width: max-content;
      user-select: none;
      overflow: hidden;

      .node-host-count {
        margin-right: 10px;
        font-size: 13px;
        user-select: none;
        display: flex;
        justify-content: flex-end;
        align-items: center;
      }
    }

    .host-list {
      width: calc(100% - @tree-width - @tree-gap);
      border-radius: 4px;
    }
  }

  :deep(.arco-scrollbar) {
    width: @tree-width;
    margin-right: @tree-gap;
    border-radius: 4px;

    &-container {
      width: 100%;
      max-height: 100%;
      overflow: auto;
    }
  }

</style>
