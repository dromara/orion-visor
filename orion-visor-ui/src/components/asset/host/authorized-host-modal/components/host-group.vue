<template>
  <div class="group-view-container">
    <!-- 主机分组 -->
    <div class="host-group-container">
      <a-scrollbar>
        <a-tree v-model:selected-keys="selectedGroup"
                class="host-tree block-tree"
                :data="groups"
                :blockNode="true">
          <!-- 组内数量 -->
          <template #extra="node">
            <span class="node-host-count span-blue">{{ nodes[node.key]?.length || 0 }}</span>
          </template>
        </a-tree>
      </a-scrollbar>
    </div>
    <!-- 主机列表 -->
    <host-table class="host-list"
                v-model:selected-keys="selectedKeysValue"
                :host-list="hostList"
                empty-message="当前分组内无授权主机!" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'hostGroup'
  };
</script>

<script lang="ts" setup>
  import type { HostQueryResponse } from '@/api/asset/host';
  import type { HostGroupQueryResponse } from '@/api/asset/host-group';
  import { computed } from 'vue';
  import HostTable from './host-table.vue';

  const props = defineProps<{
    selectedKeys: Array<number>;
    selectedGroup: number,
    hostList: Array<HostQueryResponse>;
    groups: Array<HostGroupQueryResponse>;
    nodes: Record<string, Array<number>>;
  }>();

  const emits = defineEmits(['update:selectedKeys', 'update:selectedGroup']);

  // 选中数据
  const selectedKeysValue = computed<Array<number>>({
    get() {
      return props.selectedKeys;
    },
    set(e) {
      if (e) {
        emits('update:selectedKeys', e);
      } else {
        emits('update:selectedKeys', []);
      }
    }
  });

  // 选中分组
  const selectedGroup = computed({
    get() {
      return [props.selectedGroup];
    },
    set(e) {
      emits('update:selectedGroup', e[0]);
    }
  });

</script>

<style lang="less" scoped>
  @tree-width: 298px;
  @tree-gap: 24px;

  .group-view-container {
    display: flex;
    justify-content: space-between;
    width: 100%;
    height: 100%;
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
