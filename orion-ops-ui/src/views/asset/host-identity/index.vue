<template>
  <div class="layout-container">
    <!-- 列表-表格 -->
    <host-identity-table v-if="renderTable"
                         ref="table"
                         @openAdd="() => modal.openAdd()"
                         @openUpdate="(e) => modal.openUpdate(e)"
                         @openKeyView="(e) => keyDrawer.openView(e) " />
    <!-- 列表-卡片 -->
    <host-identity-card-list v-else
                             ref="card"
                             @openAdd="() => modal.openAdd()"
                             @openUpdate="(e) => modal.openUpdate(e)"
                             @openKeyView="(e) => keyDrawer.openView(e) " />
    <!-- 添加修改模态框 -->
    <host-identity-form-modal ref="modal"
                              @added="modalAddCallback"
                              @updated="modalAddCallback" />
    <!-- 添加修改模态框 -->
    <host-key-form-drawer ref="keyDrawer" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'assetHostIdentity'
  };
</script>

<script lang="ts" setup>
  import HostIdentityCardList from './components/host-identity-card-list.vue';
  import HostIdentityTable from './components/host-identity-table.vue';
  import HostIdentityFormModal from './components/host-identity-form-modal.vue';
  import HostKeyFormDrawer from '../host-key/components/host-key-form-drawer.vue';
  import { getHostKeyList } from '@/api/asset/host-key';

  import { onUnmounted, ref, computed } from 'vue';
  import { useAppStore, useCacheStore } from '@/store';

  const table = ref();
  const card = ref();
  const modal = ref();
  const keyDrawer = ref();
  const appStore = useAppStore();
  const cacheStore = useCacheStore();

  const renderTable = computed(() => appStore.hostIdentityView === 'table');

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

  // 获取主机秘钥列表
  const fetchHostKeyList = async () => {
    try {
      const { data } = await getHostKeyList();
      cacheStore.set('hostKeys', data);
    } catch (e) {
    }
  };
  fetchHostKeyList();

  // 卸载时清除 tags cache
  onUnmounted(() => {
    const cacheStore = useCacheStore();
    cacheStore.set('hostKeys', []);
  });

</script>

<style lang="less" scoped>

</style>
