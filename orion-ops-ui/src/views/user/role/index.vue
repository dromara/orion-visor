<template>
  <div class="layout-container" v-if="render">
    <!-- 表格 -->
    <role-table ref="table"
                @openAdd="() => modal.openAdd()"
                @openUpdate="(e) => modal.openUpdate(e)"
                @openGrant="(e) => grantModal.open(e)" />
    <!-- 添加修改模态框 -->
    <role-form-modal ref="modal"
                     @added="() => table.addedCallback()"
                     @updated="() => table.updatedCallback()" />
    <!-- 分配角色菜单模态框 -->
    <role-menu-grant-modal ref="grantModal" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'role'
  };
</script>

<script lang="ts" setup>
  import RoleTable from './components/role-table.vue';
  import RoleFormModal from './components/role-form-modal.vue';
  import RoleMenuGrantModal from '@/views/user/role/components/role-menu-grant-modal.vue';
  import { ref, onBeforeMount, onUnmounted } from 'vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { dictKeys } from './types/const';

  const render = ref();
  const table = ref();
  const modal = ref();
  const grantModal = ref();

  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

  // 卸载时清除 cache
  onUnmounted(() => {
    const cacheStore = useCacheStore();
    cacheStore.reset('menus');
  });

</script>

<style lang="less" scoped>

</style>
