<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <exec-host-log-table ref="table"
                  @openAdd="() => modal.openAdd()"
                  @openUpdate="(e) => modal.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <exec-host-log-form-modal ref="modal"
                   @added="modalAddCallback"
                   @updated="modalUpdateCallback" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execHostLog'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import ExecHostLogTable from './components/exec-host-log-table.vue';
  import ExecHostLogFormModal from './components/exec-host-log-form-modal.vue';

  const render = ref(false);
  const table = ref();
  const modal = ref();

  // 添加回调
  const modalAddCallback = () => {
    table.value.addedCallback();
  };

  // 修改回调
  const modalUpdateCallback = () => {
    table.value.updatedCallback();
  };

  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
