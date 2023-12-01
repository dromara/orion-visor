<template>
  <div class="view-container">
    <a-tabs class="tabs-container simple-card"
            size="large"
            :destroy-on-hide="true"
            :justify="true"
            :lazy-load="true">
      <!-- 主机分组授权(角色) -->
      <a-tab-pane :key="1"
                  v-permission="['asset:host-group:grant']"
                  title="主机分组授权(角色)">
        <host-group-role-grant />
      </a-tab-pane>
      <!-- 主机分组授权(用户) -->
      <a-tab-pane :key="2"
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
  import { ref, onBeforeMount, onUnmounted } from 'vue';
  import { useCacheStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import { getUserList } from '@/api/user/user';
  import { getRoleList } from '@/api/user/role';
  import HostGroupRoleGrant from './components/host-group-role-grant.vue';
  import HostGroupUserGrant from './components/host-group-user-grant.vue';

  const render = ref(false);
  const cacheStore = useCacheStore();

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
    width: 100%;
    height: 100%;
  }

  :deep(.arco-tabs-tab-title) {
    user-select: none;
  }

  :deep(.arco-tabs-content) {
    position: relative;
  }
</style>
