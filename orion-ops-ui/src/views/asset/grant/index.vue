<template>
  <div class="view-container">
    <a-tabs v-model:active-key="activeKey"
            class="tabs-container simple-card"
            size="large"
            :justify="true"
            :lazy-load="true">
      <!-- 主机分组授权(角色) -->
      <a-tab-pane :key="GrantKey.HOST_GROUP_ROLE"
                  v-permission="['asset:host-group:grant']"
                  title="主机分组授权(角色)">
        <host-group-role-grant />
      </a-tab-pane>
      <!-- 主机分组授权(用户) -->
      <a-tab-pane :key="GrantKey.HOST_GROUP_USER"
                  v-permission="['asset:host-group:grant']"
                  title="主机分组授权(用户)">
        <host-group-user-grant />
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
  import HostGroupRoleGrant from './components/host-group-role-grant.vue';
  import HostGroupUserGrant from './components/host-group-user-grant.vue';
  import { GrantKey } from './types/const';
  import { useRoute } from 'vue-router';

  const route = useRoute();
  const cacheStore = useCacheStore();

  const activeKey = ref();

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.reset('users', 'roles', 'hosts', 'hostGroups');
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

  :deep(.arco-tabs-tab-title) {
    user-select: none;
  }

  :deep(.arco-tabs-content) {
    position: relative;
  }
</style>
