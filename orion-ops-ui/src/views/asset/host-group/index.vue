<template>
  <div v-if="render" class="view-container">
    <a-tabs v-if="render"
            class="tabs-container"
            :default-active-key="1"
            :destroy-on-hide="true"
            :justify="true"
            :lazy-load="true">
      <!-- 左侧导航 -->
      <a-tab-pane :key="1" v-permission="['asset:host-group:query']">
        <host-group-view-setting />
        <template #title>
          <icon-unordered-list />
          分组配置
        </template>
      </a-tab-pane>
      <!-- 角色分配 -->
      <a-tab-pane :key="2" v-permission="['asset:host-group:grant']">
        <host-group-view-role-grant />
        <template #title>
          <icon-safe />
          角色授权
        </template>
      </a-tab-pane>
      <!-- 用户分配 -->
      <a-tab-pane :key="3" v-permission="['asset:host-group:grant']">
        <host-group-view-user-grant />
        <template #title>
          <icon-user />
          用户授权
        </template>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'assetHostGroup'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount, onUnmounted } from 'vue';
  import { useCacheStore } from '@/store';
  import { getHostList } from '@/api/asset/host';
  import { Message } from '@arco-design/web-vue';
  import HostGroupViewSetting from './components/host-group-view-setting.vue';
  import HostGroupViewRoleGrant from './components/host-group-view-role-grant.vue';
  import HostGroupViewUserGrant from './components/host-group-view-user-grant.vue';

  const render = ref(false);
  const cacheStore = useCacheStore();

  // 加载主机列表
  const loadHostList = async () => {
    try {
      const { data } = await getHostList();
      // 设置到缓存
      cacheStore.set('hosts', data);
    } catch (e) {
      Message.error('tag加载失败');
    }
  };

  onBeforeMount(async () => {
    // 加载主机列表
    await loadHostList();
    render.value = true;
  });

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.reset('user', 'roles', 'hosts');
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
    width: 100%;
    height: 100%;
    position: relative;
    background: #FFF;
  }

  :deep(.arco-tabs-content) {
    padding-top: 0;
  }
</style>
