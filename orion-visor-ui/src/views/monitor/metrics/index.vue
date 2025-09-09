<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <metrics-table ref="table"
                   @open-add="() => modal.openAdd()"
                   @open-update="(e) => modal.openUpdate(e)" />
    <!-- 添加修改模态框 -->
    <metrics-form-modal ref="modal"
                        @added="reload"
                        @updated="reload" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'metrics'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import MetricsTable from './components/metrics-table.vue';
  import MetricsFormModal from './components/metrics-form-modal.vue';

  const render = ref(false);
  const table = ref();
  const modal = ref();

  // 重新加载
  const reload = () => {
    table.value.reload();
  };

  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
