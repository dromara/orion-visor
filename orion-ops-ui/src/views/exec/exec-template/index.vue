<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <exec-template-table ref="table"
                         @open-exec="(e) => execModal.open(e)"
                         @open-add="() => drawer.openAdd()"
                         @open-update="(e) => drawer.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <exec-template-form-drawer ref="drawer"
                               @added="modalAddCallback"
                               @updated="modalUpdateCallback" />
    <!-- 执行模态框 -->
    <exec-template-exec-drawer ref="execModal"
                               @open-host="(e) => hostModal.open(e)" />
    <!-- 主机模态框 -->
    <authorized-host-modal ref="hostModal"
                           @selected="(e) => execModal.setSelectedHost(e)" />
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
  import AuthorizedHostModal from '@/components/asset/host/authorized-host-modal/index.vue';

  const render = ref(false);
  const table = ref();
  const drawer = ref();
  const hostModal = ref();
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
