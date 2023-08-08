<template>
  <div class="layout-container">
    <!-- 表格 -->
    <menu-table ref="table"
                @openAdd="(e) => modal.openAdd(e)"
                @openUpdate="(e) => modal.openUpdate(e)" />
    <!-- 添加修改框 -->
    <menu-form-modal ref="modal"
                     @added="(e) => table.addedCallback(e)"
                     @updated="(e) => table.updatedCallback(e)" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'systemMenu'
  };
</script>

<script lang="ts" setup>
  import MenuTable from '@/views/system/menu/components/menu-table.vue';
  import { ref } from 'vue';
  import MenuFormModal from '@/views/system/menu/components/menu-form-modal.vue';

  const table = ref<any>();
  const modal = ref<any>();

</script>
