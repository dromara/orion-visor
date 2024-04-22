<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <exec-template-table ref="table"
                         @open-add="() => drawer.openAdd()"
                         @open-update="(e) => drawer.openUpdate(e)"
                         @open-exec="(e) => execModal.open(e)" />
    <!-- 添加修改模态框 -->
    <exec-template-form-drawer ref="drawer"
                               @added="modalAddCallback"
                               @updated="modalUpdateCallback"
                               @open-host="(e) => openHostModal('drawer', e)" />
    <!-- 执行模态框 -->
    <exec-template-exec-drawer ref="execModal"
                               @open-host="(e) => openHostModal('exec', e)" />
    <!-- 主机模态框 -->
    <authorized-host-modal ref="hostModal"
                           @selected="hostSelected" />
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
  const execModal = ref();
  const hostModal = ref();
  const lastOpenHostRef = ref();

  // 添加回调
  const modalAddCallback = () => {
    table.value.addedCallback();
  };

  // 修改回调
  const modalUpdateCallback = () => {
    table.value.updatedCallback();
  };

  // 打开主机模态框
  const openHostModal = (openRef: string, data: any) => {
    lastOpenHostRef.value = openRef;
    hostModal.value.open(data);
  };

  // 选中主机
  const hostSelected = (data: any) => {
    if (lastOpenHostRef.value === 'drawer') {
      // 设置选中的主机
      drawer.value.setSelectedHost(data);
    } else if (lastOpenHostRef.value === 'exec') {
      // 设置选中的主机
      execModal.value.setSelectedHost(data);
    }
  };

  onBeforeMount(async () => {
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
