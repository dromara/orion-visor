<template>
  <div class="layout-container">
    <!-- 表格 -->
    <user-table ref="table"
                  @openAdd="() => modal.openAdd()"
                  @openUpdate="(e) => modal.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <user-form-modal ref="modal"
                       @added="() => table.addedCallback()"
                       @updated="() => table.updatedCallback()" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'userUser'
  };
</script>

<script lang="ts" setup>
  import UserTable from './components/user-table.vue';
  import UserFormModal from './components/user-form-modal.vue';
  import { ref } from 'vue';

  const table = ref();
  const modal = ref();

</script>

<style lang="less" scoped>

</style>
