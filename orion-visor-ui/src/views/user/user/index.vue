<template>
  <div class="layout-container" v-if="render">
    <!-- 表格 -->
    <user-table ref="table"
                @open-add="() => modal.openAdd()"
                @open-update="(e) => modal.openUpdate(e)"
                @open-reset-password="(e) => resetModal.open(e)"
                @open-grant-role="(e) => grantRoleModal.open(e)" />
    <!-- 添加修改模态框 -->
    <user-form-modal ref="modal"
                     @added="() => table.reload()"
                     @updated="() => table.reload()" />
    <!-- 重置密码模态框 -->
    <user-reset-password-form-modal ref="resetModal" />
    <!-- 分配角色模态框 -->
    <user-grant-roles-form-modal ref="grantRoleModal" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'user'
  };
</script>

<script lang="ts" setup>
  import UserTable from './components/user-table.vue';
  import UserFormModal from './components/user-form-modal.vue';
  import UserResetPasswordFormModal from './components/user-reset-password-form-modal.vue';
  import UserGrantRolesFormModal from './components/user-grant-roles-form-modal.vue';
  import { ref, onBeforeMount, onUnmounted } from 'vue';
  import { useCacheStore, useDictStore } from '@/store';
  import { dictKeys } from './types/const';

  const render = ref(false);
  const table = ref();
  const modal = ref();
  const resetModal = ref();
  const grantRoleModal = ref();

  onBeforeMount(async () => {
    // 加载字典值
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

  // 卸载时清除 cache
  onUnmounted(() => {
    const cacheStore = useCacheStore();
    cacheStore.reset('roles');
  });

</script>

<style lang="less" scoped>

</style>
