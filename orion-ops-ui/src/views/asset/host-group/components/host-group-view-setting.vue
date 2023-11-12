<template>
  <!-- 左侧菜单 -->
  <div class="simple-card tree-card">
    <!-- 主机分组头部 -->
    <div class="tree-card-header">
      <!-- 标题 -->
      <div class="tree-card-title">
        主机菜单
      </div>
    </div>
    <!-- 主机分组树 -->
    <div class="tree-card-main">
      <host-group-tree ref="tree" />
    </div>
  </div>
  <!-- 身体部分 -->
  <div class="simple-card view-body">
    右侧数据
  </div>
</template>

<script lang="ts">
  export default {
    name: 'host-group-view-setting'
  };
</script>

<script lang="ts" setup>
  import HostGroupTree from './host-group-tree.vue';

</script>

<style lang="less" scoped>
  @tree-width: 26%;
  @tree-width: 50%;

  .tree-card {
    margin-right: 16px;
    width: @tree-width;
    height: 100%;
    position: absolute;

    &-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 16px;
      position: relative;
      width: 100%;
      height: 44px;
      border-bottom: 1px var(--color-border-2) solid;
    }

    &-title {
      color: rgba(var(--gray-10), .95);
      font-size: 16px;
      font-weight: 600;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
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
