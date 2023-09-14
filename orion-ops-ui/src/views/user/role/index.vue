<template>
  <div class="layout-container">
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
    name: 'userRole'
  };
</script>

<script lang="ts" setup>
  import RoleTable from './components/role-table.vue';
  import RoleFormModal from './components/role-form-modal.vue';
  import RoleMenuGrantModal from '@/views/user/role/components/role-menu-grant-modal.vue';
  import { onUnmounted, ref } from 'vue';
  import { useCacheStore } from '@/store';

  const table = ref();
  const modal = ref();
  const grantModal = ref();

  // 卸载时清除 menu cache
  onUnmounted(() => {
    const cacheStore = useCacheStore();
    cacheStore.set('menus', []);
  });

</script>

<style lang="less" scoped>

</style>
