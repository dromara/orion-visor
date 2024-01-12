<template>
  <div class="terminal-setting-container">
    <div class="terminal-setting-wrapper">
      <!-- 组合容器 -->
      <div class="combined-container">
        <!-- 新建连接 -->
        <div class="combined-handler">
          <!-- 图标 -->
          <div class="combined-handler-icon">
            <icon-plus />
          </div>
          <!-- 内容 -->
          <div class="combined-handler-text">
            新建连接
          </div>
        </div>
        <!-- 主机列表 -->
        <div class="combined-handler">
          <!-- 图标 -->
          <div class="combined-handler-icon">
            <icon-desktop />
          </div>
          <!-- 内容 -->
          <div class="combined-handler-text">
            历史1
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'emptyRecommend'
  };
</script>

<script lang="ts" setup>

  import { useTerminalStore } from '@/store';

  const { tabManager, hosts } = useTerminalStore();

</script>

<style lang="less" scoped>
  @handler-height: 48px;

  .combined-container {
    padding: 8px;
    margin: 64px auto;
    width: 448px;
    height: 448px;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    &:hover {
      overflow: auto;
    }
  }

  .combined-handler {
    width: 100%;
    height: @handler-height;
    border-radius: 4px;
    margin-bottom: 6px;
    background-color: var(--color-fill-2);
    display: flex;
    align-items: center;
    color: var(--color-content-text-1);

    &-icon {
      width: @handler-height;
      height: @handler-height;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 18px;
    }

    &-text {
      height: 100%;
      display: flex;
      align-items: center;
      font-size: 14px;
    }
  }
</style>
