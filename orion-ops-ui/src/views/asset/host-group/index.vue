<template>
  <div class="index-container" v-if="render">
    <host-group-view />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'assetHostGroup'
  };
</script>

<script lang="ts" setup>
  import { Message } from '@arco-design/web-vue';

  import { computed, ref, onBeforeMount, onUnmounted } from 'vue';
  import { useAppStore, useCacheStore, useDictStore } from '@/store';
  import HostGroupView from './components/host-group-view.vue';
  import { getHostList } from '@/api/asset/host';

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
  .index-container {
    position: relative;
    width: 100%;
    height: 100%;
    padding: 16px;
  }
</style>
