<template>
  <div v-if="render" class="view-container">
    <a-tabs v-if="render"
            class="tabs-container"
            position="left"
            type="rounded"
            size="large"
            :destroy-on-hide="true"
            :justify="true"
            :lazy-load="true">
      <!-- 角色分配 -->
      <a-tab-pane :key="1" v-permission="['asset:host-group:grant']">
        <host-group-view-role-grant />
        <template #title>
          <icon-safe />
          角色授权
        </template>
      </a-tab-pane>
      <!-- 用户分配 -->
      <a-tab-pane :key="2" v-permission="['asset:host-group:grant']">
        <host-group-view-user-grant />
        <template #title>
          <icon-user />
          用户授权2323
        </template>
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
  import { ref, onBeforeMount, onUnmounted } from 'vue';
  import { useCacheStore } from '@/store';
  import { getHostList } from '@/api/asset/host';
  import { Message } from '@arco-design/web-vue';
  import HostGroupViewRoleGrant from './components/host-group-view-role-grant.vue';
  import HostGroupViewUserGrant from './components/host-group-view-user-grant.vue';
  import { getUserList } from '@/api/user/user';
  import { getRoleList } from '@/api/user/role';
  import usePermission from '@/hooks/permission';

  const render = ref(false);
  const cacheStore = useCacheStore();
  const { hasPermission } = usePermission();

  // 加载主机列表
  const loadHostList = async () => {
    try {
      const { data } = await getHostList();
      // 设置到缓存
      cacheStore.set('hosts', data);
    } catch (e) {
      Message.error('主机列表加载失败');
    }
  };

  // 加载用户列表
  const loadUserList = async () => {
    try {
      const { data } = await getUserList();
      // 设置到缓存
      cacheStore.set('users', data);
    } catch (e) {
      Message.error('用户列表加载失败');
    }
  };

  // 加载角色列表
  const loadRoleList = async () => {
    try {
      const { data } = await getRoleList();
      // 设置到缓存
      cacheStore.set('roles', data);
    } catch (e) {
      Message.error('角色列表加载失败');
    }
  };

  onBeforeMount(async () => {
    if (hasPermission('asset:host-group:query')) {
      // 加载主机列表 tab1
      await loadHostList();
      render.value = true;
    }
    if (hasPermission('asset:host-group:grant')) {
      // 加载角色列表 tab2
      await loadRoleList();
      render.value = true;
      // 加载用户列表 tab3
      await loadUserList();
    }
  });

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.reset('users', 'roles', 'hosts', 'hostGroups');
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
    display: flex;
    width: calc(100% - 32px);
    max-height: calc(100% - 32px);
    position: absolute;
    background: var(--color-bg-2);
    padding: 16px;
  }

  :deep(.arco-tabs-nav-tab) {
    border-right: 1px var(--color-neutral-3) solid;
    padding-right: 16px;
  }

  :deep(.arco-tabs-tab-title) {
    user-select: none;
  }

  :deep(.arco-tabs-content) {
    position: relative;
  }
</style>
