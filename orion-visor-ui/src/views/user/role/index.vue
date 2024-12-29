<template>
  <div class="layout-container" v-if="render">
    <!-- 表格 -->
    <role-table ref="table"
                @open-add="() => modal.openAdd()"
                @open-update="(e) => modal.openUpdate(e)"
                @open-grant="(e) => grantModal.open(e)" />
    <!-- 添加修改模态框 -->
    <role-form-modal ref="modal"
                     @added="() => table.reload()"
                     @updated="() => table.reload()" />
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
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import RoleTable from './components/role-table.vue';
  import RoleFormModal from './components/role-form-modal.vue';
  import RoleMenuGrantModal from '@/views/user/role/components/role-menu-grant-modal.vue';

  const render = ref();
  const table = ref();
  const modal = ref();
  const grantModal = ref();

  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
