<template>
  <div class="layout-container">
    <!-- 表格 -->
    <menu-table ref="table"
                @openAdd="(e) => modal.openAdd(e)"
                @openUpdate="(e) => modal.openUpdate(e)" />
    <!-- 添加修改框 -->
    <menu-form-modal ref="modal"
                     @added="() => table.addedCallback()"
                     @updated="() => table.updatedCallback()" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'systemMenu'
  };
</script>

<script lang="ts" setup>
  import MenuTable from '@/views/system/menu/components/menu-table.vue';
  import MenuFormModal from '@/views/system/menu/components/menu-form-modal.vue';

  import { ref } from 'vue';

  const table = ref<any>();
  const modal = ref<any>();

</script>
