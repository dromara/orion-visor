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

  const render = ref(false);
  const table = ref();
  const card = ref();
  const modal = ref();
  const config = ref();
  const appStore = useAppStore();
  const cacheStore = useCacheStore();

  // 添加回调
  const modalAddCallback = () => {
    table.value.addedCallback();
  };

  // 修改回调
  const modalUpdateCallback = () => {
    table.value.updatedCallback();
  };

  // 加载 tags
  const loadTags = async () => {
    try {
      // 设置到缓存
      // cacheStore.set('hostTags', data);
    } catch {
      Message.error('tag加载失败');
    }
  };

  onBeforeMount(async () => {
    // 加载角色列表

    // 加载用户列表

    // 加载主机列表
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
