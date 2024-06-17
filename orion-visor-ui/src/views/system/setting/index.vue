<template>
  <div class="tabs-container">
    <a-tabs type="rounded"
            size="medium"
            position="left"
            :lazy-load="true"
            :destroy-on-hide="true">
      <!-- 关于 -->
      <a-tab-pane key="about" title="关于">
        <about />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'systemSetting'
  };
</script>

<script lang="ts" setup>
  import About from './components/about.vue';

</script>

<style lang="less" scoped>
  .tabs-container {
    background: var(--color-bg-2);
    margin: 16px;
    padding: 16px;
    display: flex;
    flex-direction: column;
    border-radius: 4px;
  }

  :deep(.arco-tabs-nav-tab-list) {
    width: 88px;
  }

  :deep(.arco-tabs-pane) {
    border-left: 1px var(--color-neutral-3) solid;
  }

  :deep(.arco-tabs-tab) {
    user-select: none;
    white-space: nowrap;
    display: flex;
    justify-content: center;
  }

</style>
