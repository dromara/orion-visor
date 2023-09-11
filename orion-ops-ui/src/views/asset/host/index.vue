<template>
  <div class="layout-container">
    <!-- 表格 -->
    <host-table ref="table"
                  @openAdd="() => modal.openAdd()"
                  @openUpdate="(e) => modal.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <host-form-modal ref="modal"
                       @added="() => table.addedCallback()"
                       @updated="() => table.updatedCallback()" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'assetHost'
  };
</script>

<script lang="ts" setup>
  import HostTable from './components/host-table.vue';
  import HostFormModal from './components/host-form-modal.vue';
  import { ref } from 'vue';

  const table = ref();
  const modal = ref();

</script>

<style lang="less" scoped>

</style>
