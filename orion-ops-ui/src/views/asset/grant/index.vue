<template>
  <div class="view-container">
    <a-tabs v-model:active-key="activeKey"
            class="tabs-container simple-card"
            size="large"
            :justify="true"
            :lazy-load="true">
      <a-tab-pane v-for="tab in GrantTabs"
                  v-permission="tab.permission"
                  :key="tab.key">
        <template #title>
          <component :is="tab.icon" />
          {{ tab.title }}
        </template>
        <component :is="tab.component" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'assetGrant'
  };
</script>

<script lang="ts" setup>
  import { onBeforeMount, onUnmounted, ref } from 'vue';
  import { useCacheStore } from '@/store';
  import { GrantTabs } from './types/const';
  import { useRoute } from 'vue-router';

  const route = useRoute();
  const cacheStore = useCacheStore();

  const activeKey = ref();

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.reset('users', 'roles', 'hosts', 'hostGroups', 'hostKeys', 'hostIdentities');
  });

  // 跳转到指定页
  onBeforeMount(() => {
    const key = route.query.key;
    if (key) {
      activeKey.value = Number(key);
    }
  });

</script>

<style lang="less" scoped>
  .view-container {
    display: flex;
    width: 100%;
    height: 100%;
    position: relative;
    padding: 16px;
  }

  .tabs-container {
    width: 100%;
    height: 100%;
  }

  :deep(.arco-tabs-pane) {
    width: 100%;
    height: 100%;
    position: relative;
  }

  :deep(.arco-tabs-nav-type-line .arco-tabs-tab) {
    margin: 0 12px;
  }

  :deep(.arco-tabs-tab-title) {
    user-select: none;
    font-size: 15px;
    padding: 0 4px;
  }

  :deep(.arco-tabs-content) {
    position: relative;
  }
</style>
