<template>
  <div class="layout-container">
    <!-- 列表-表格 -->
    <host-identity-table v-if="renderTable"
                         ref="table"
                         @open-add="() => modal.openAdd()"
                         @open-update="(e) => modal.openUpdate(e)"
                         @open-key-view="(e) => keyDrawer.openView(e) " />
    <!-- 列表-卡片 -->
    <host-identity-card-list v-else
                             ref="card"
                             @open-add="() => modal.openAdd()"
                             @open-update="(e) => modal.openUpdate(e)"
                             @open-key-view="(e) => keyDrawer.openView(e) " />
    <!-- 添加修改模态框 -->
    <host-identity-form-modal ref="modal"
                              @added="modalAddCallback"
                              @updated="modalUpdateCallback" />
    <!-- 主机秘钥抽屉 -->
    <host-key-form-drawer ref="keyDrawer" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'hostIdentity'
  };
</script>

<script lang="ts" setup>
  import { ref, computed, onUnmounted } from 'vue';
  import { useAppStore, useCacheStore } from '@/store';
  import HostIdentityCardList from './components/host-identity-card-list.vue';
  import HostIdentityTable from './components/host-identity-table.vue';
  import HostIdentityFormModal from './components/host-identity-form-modal.vue';
  import HostKeyFormDrawer from '../host-key/components/host-key-form-drawer.vue';

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

  // 卸载时清除 cache
  onUnmounted(() => {
    const cacheStore = useCacheStore();
    cacheStore.reset('hostKeys');
  });

</script>

<style lang="less" scoped>

</style>
