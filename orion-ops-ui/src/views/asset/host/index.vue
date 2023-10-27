<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <host-table v-if="renderTable"
                ref="table"
                @openAdd="() => modal.openAdd()"
                @openUpdate="(e) => modal.openUpdate(e)"
                @openUpdateConfig="(e) => config.open(e)" />
    <!-- 列表-卡片 -->
    <host-card-list v-else
                    ref="card"
                    @openAdd="() => modal.openAdd()"
                    @openUpdate="(e) => modal.openUpdate(e)"
                    @openUpdateConfig="(e) => config.open(e)" />
    <!-- 添加修改模态框 -->
    <host-form-modal ref="modal"
                     @added="modalAddCallback"
                     @updated="modalUpdateCallback" />
    <!-- 配置面板 -->
    <host-config-drawer ref="config" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'assetHost'
  };
</script>

<script lang="ts" setup>
  import HostTable from './components/host-table.vue';
  import HostCardList from '@/views/asset/host/components/host-card-list.vue';
  import HostFormModal from './components/host-form-modal.vue';
  import HostConfigDrawer from '@/views/asset/host/components/host-config-drawer.vue';
  import { getTagList } from '@/api/meta/tag';
  import { dictKeys } from './types/const';
  import { Message } from '@arco-design/web-vue';

  import { computed, ref, onBeforeMount, onUnmounted } from 'vue';
  import { useAppStore, useCacheStore, useDictStore } from '@/store';

  const render = ref(false);
  const table = ref();
  const card = ref();
  const modal = ref();
  const config = ref();
  const appStore = useAppStore();
  const cacheStore = useCacheStore();

  const renderTable = computed(() => appStore.hostView === 'table');

  // 添加回调
  const modalAddCallback = () => {
    if (renderTable.value) {
      table.value.addedCallback();
    } else {
      card.value.addedCallback();
    }
  };

  // 修改回调
  const modalUpdateCallback = () => {
    if (renderTable.value) {
      table.value.updatedCallback();
    } else {
      card.value.updatedCallback();
    }
  };

  // 加载 tags
  const loadTags = async () => {
    try {
      const { data } = await getTagList('HOST');
      // 设置到缓存
      cacheStore.set('hostTags', data);
    } catch {
      Message.error('tag加载失败');
    }
  };

  onBeforeMount(async () => {
    // 加载 tags
    await loadTags();
    // 加载字典值
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

  // 卸载时清除 cache
  onUnmounted(() => {
    cacheStore.reset('hostTags', 'hostKeys', 'hostIdentities');
  });

</script>

<style lang="less" scoped>

</style>
