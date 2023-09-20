<template>
  <div class="layout-container">
    <!-- 表格 -->
    <host-identity-table ref="table"
                         @openAdd="() => drawer.openAdd()"
                         @openUpdate="(e) => drawer.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <host-identity-form-drawer ref="drawer"
                               @added="() => table.addedCallback()"
                               @updated="() => table.updatedCallback()" />

  </div>
</template>

<script lang="ts">
  export default {
    name: 'asset-host-identity'
  };
</script>

<script lang="ts" setup>
  import HostIdentityTable from './components/host-identity-table.vue';
  import HostIdentityFormDrawer from './components/host-identity-form-drawer.vue';

  import { ref } from 'vue';

  const table = ref();
  const drawer = ref();

</script>

<style lang="less" scoped>

</style>
