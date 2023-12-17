<template>
  <div class="group-view-container">
    <!-- 主机分组 -->
    <div class="host-group-container">
      <a-scrollbar>
        <a-tree v-model:selected-keys="selectedGroup"
                class="host-tree block-tree"
                :data="groupTree"
                :blockNode="true">
          <!-- 组内数量 -->
          <template #extra="node">
            <span class="node-host-count span-blue">{{ treeNodes[node.key]?.length || 0 }}</span>
          </template>
        </a-tree>
      </a-scrollbar>
    </div>
    <!-- 主机列表 -->
    <host-list-view class="host-list"
                    :hostList="hostList"
                    empty-value="当前分组内无授权主机!" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'hostGroupView'
  };
</script>

<script lang="ts" setup>
  import { computed } from 'vue';
  import { HostQueryResponse } from '@/api/asset/host';
  import { HostGroupQueryResponse } from '@/api/asset/host-group';
  import HostListView from './host-list-view.vue';

  const props = defineProps<{
    modelValue: number,
    groupTree: Array<HostGroupQueryResponse>;
    hostList: Array<HostQueryResponse>;
    treeNodes: Record<string, Array<number>>;
  }>();

  const emits = defineEmits(['update:modelValue']);

  const selectedGroup = computed({
    get() {
      return [props.modelValue];
    },
    set(e) {
      emits('update:modelValue', e[0]);
    }
  });

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
