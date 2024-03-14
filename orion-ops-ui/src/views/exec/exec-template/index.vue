<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <exec-template-table ref="table"
                         @open-exec="e => execModal.open(e)"
                         @openAdd="() => drawer.openAdd()"
                         @openUpdate="(e) => drawer.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <exec-template-form-drawer ref="drawer"
                               @added="modalAddCallback"
                               @updated="modalUpdateCallback" />
    <!-- 执行模态框 -->
    <exec-template-exec-drawer ref="execModal" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execTemplate'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import ExecTemplateTable from './components/exec-template-table.vue';
  import ExecTemplateFormDrawer from './components/exec-template-form-drawer.vue';
  import ExecTemplateExecDrawer from './components/exec-template-exec-drawer.vue';

  const render = ref(false);
  const table = ref();
  const drawer = ref();
  const execModal = ref();

  // 添加回调
  const modalAddCallback = () => {
    table.value.addedCallback();
  };

  // 修改回调
  const modalUpdateCallback = () => {
    table.value.updatedCallback();
  };

  onBeforeMount(async () => {
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
