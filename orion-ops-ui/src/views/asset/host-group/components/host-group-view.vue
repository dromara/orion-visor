<template>
  <div class="view-container">
    <!-- 左侧菜单 -->
    <div class="simple-card tree-card">
      <!-- 主机分组标题 -->
      <div class="tree-card-header">
        <!-- 标题 -->
        <div class="tree-card-title">
          主机菜单
        </div>
        <div class="tree-card-switch">
          <a-switch type="round"
                    v-model="editMode"
                    checked-text="编辑模式"
                    unchecked-text="授权模式" />
        </div>
      </div>
      <!-- 主机分组树 -->
      <div class="tree-card-main">
        <host-group-tree ref="tree" :editMode="editMode" />
      </div>
    </div>
    <!-- 身体部分 -->
    <div class="view-body">
      <div class="simple-card fixed-header">
        <!-- 头部左侧 -->
        <a-space>
          <a-select placeholder="选择角色" />
          <a-select placeholder="选择用户" />
        </a-space>
        <!-- 头部右侧 -->
        <a-space>
          <!-- 配置 -->
          <a-button type="primary">
            配置
            <template #icon>
              <icon-layers />
            </template>
          </a-button>
          <!-- 授权 -->
          <a-button type="primary">
            授权
            <template #icon>
              <icon-safe />
            </template>
          </a-button>
        </a-space>
      </div>
      <!-- 右侧数据 -->
      <div class="simple-card data-content">
        右侧数据
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'host-group-view'
  };
</script>

<script lang="ts" setup>
  import HostGroupTree from './host-group-tree.vue';
  import { ref } from 'vue';

  const editMode = ref(true);

</script>

<style lang="less" scoped>
  @tree-width: 50%;

  .view-container {
    display: flex;
    width: 100%;
    height: 100%;
    position: relative;
  }

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
    flex-direction: column;
    height: 100%;
    width: calc(100% - @tree-width - 16px);
    position: absolute;
    left: calc(@tree-width + 16px);

    .fixed-header {
      display: flex;
      justify-content: space-between;

      margin-bottom: 16px;
      padding: 8px 16px;
      height: 48px;
    }

    .data-content {
      display: flex;
      padding: 16px;
      width: 100%;
      height: 100%;
    }
  }

</style>
