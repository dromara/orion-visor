<template>
  <div class="group-view-container">
    <!-- 主机分组 -->
    <div class="host-group-container">
      <a-scrollbar>
        <a-tree v-model:selected-keys="selectedGroup"
                :data="hosts.groupTree"
                :blockNode="true"
                class="host-tree block-tree"
                @select="chooseGroup">
          <!-- 组内数量 -->
          <template #extra="node">
            <span class="node-host-count span-blue">{{ hosts?.treeNodes[node.key]?.length || 0 }}</span>
          </template>
        </a-tree>
      </a-scrollbar>
    </div>
    <!-- 主机列表 -->
    <div class="host-list">
      <host-list :hosts="hosts" />
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
  import HostList from './host-list.vue';

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
  }

  .host-group-container {
    :deep(.arco-scrollbar) {
      width: @tree-width;
      height: 100%;
      margin-right: @tree-gap;
      border-radius: 4px;

      &-container {
        width: 100%;
        max-height: 100%;
        overflow: auto;
      }
    }

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
        font-weight: bold;
      }
    }
  }

  .host-list {
    width: calc(100% - @tree-width - @tree-gap);
    border-radius: 4px;
    max-height: 100%;
    overflow: auto;
    position: relative;
  }

</style>
