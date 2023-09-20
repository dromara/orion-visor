<template>
  <div class="layout-container">
    <!-- 表格 -->
    <host-key-table ref="table"
                  @openAdd="() => modal.openAdd()"
                  @openUpdate="(e) => modal.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <host-key-form-modal ref="modal"
                       @added="() => table.addedCallback()"
                       @updated="() => table.updatedCallback()" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'asset-host-key'
  };
</script>

<script lang="ts" setup>
  import HostKeyTable from './components/host-key-table.vue';
  import HostKeyFormModal from './components/host-key-form-modal.vue';
  import { ref } from 'vue';

  const table = ref();
  const modal = ref();

</script>

<style lang="less" scoped>

</style>
