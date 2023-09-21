<template>
  <div class="layout-container">
    <!-- 表格 -->
    <host-key-table ref="table"
                    @openView="(e) => drawer.openView(e)"
                    @openAdd="() => drawer.openAdd()"
                    @openUpdate="(e) => drawer.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <host-key-form-drawer ref="drawer"
                          @added="() => table.addedCallback()"
                          @updated="() => table.updatedCallback()" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'assetHostKey'
  };
</script>

<script lang="ts" setup>
  import HostKeyTable from './components/host-key-table.vue';
  import HostKeyFormDrawer from './components/host-key-form-drawer.vue';
  import { ref } from 'vue';

  const table = ref();
  const drawer = ref();

</script>

<style lang="less" scoped>

</style>
