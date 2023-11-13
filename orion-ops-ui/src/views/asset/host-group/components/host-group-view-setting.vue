<template>
  <!-- 左侧菜单 -->
  <a-spin class="simple-card tree-card"
          :loading="treeLoading">
    <!-- 主机分组头部 -->
    <div class="tree-card-header">
      <!-- 标题 -->
      <div class="tree-card-title">
        主机菜单
      </div>
      <!-- 操作 -->
      <div class="tree-card-handler">
        <div v-permission="['asset:host-group:create']"
             class="click-icon-wrapper handler-icon-wrapper"
             title="根节点添加"
             @click="addRootNode">
          <icon-plus />
        </div>
        <div class="click-icon-wrapper handler-icon-wrapper"
             title="刷新"
             @click="refreshTree">
          <icon-refresh />
        </div>
      </div>
    </div>
    <!-- 主机分组树 -->
    <div class="tree-card-main">
      <host-group-tree ref="tree"
                       :loading="treeLoading"
                       @loading="setTreeLoading"
                       @select-key="selectGroup" />
    </div>
  </a-spin>
  <!-- 身体部分 -->
  <a-spin class="simple-card view-body"
          :loading="dataLoading">
    <host-transfer ref="transfer" />
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'host-group-view-setting'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import HostGroupTree from './host-group-tree.vue';
  import HostTransfer from './host-transfer.vue';

  const { loading: treeLoading, setLoading: setTreeLoading } = useLoading();
  const { loading: dataLoading, setLoading: setDataLoading } = useLoading();

  const tree = ref();
  const transfer = ref();

  // 添加根节点
  const addRootNode = () => {
    tree.value.addRootNode();
  };

  // 刷新树
  const refreshTree = () => {
    tree.value.fetchTreeData();
  };

  // 选中分组
  const selectGroup = (key: number) => {
    console.log(key);
  };

</script>

<style lang="less" scoped>
  @tree-width: 26%;

  .tree-card {
    margin-right: 16px;
    width: @tree-width;
    height: 100%;
    position: absolute;

    &-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 8px 0 16px;
      position: relative;
      width: 100%;
      height: 44px;
      border-bottom: 1px var(--color-border-2) solid;
      user-select: none;
    }

    &-title {
      color: rgba(var(--gray-10), .95);
      font-size: 16px;
      font-weight: 600;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }

    &-handler {
      display: flex;

      .handler-icon-wrapper {
        margin-left: 8px;
        color: rgb(var(--primary-6));
        padding: 4px;
        font-size: 16px;
      }
    }

    &-main {
      padding: 8px 8px 8px 16px;
      position: relative;
      width: 100%;
      height: calc(100% - 48px);
      overflow: auto;
    }
  }

  .view-body {
    display: flex;
    height: 100%;
    padding: 16px;
    width: calc(100% - @tree-width - 16px);
    position: absolute;
    left: calc(@tree-width + 16px);
  }

</style>
