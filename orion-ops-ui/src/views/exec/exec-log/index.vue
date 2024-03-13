<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <exec-log-table />
    <!-- 添加修改模态框 -->
    <!--    <exec-log-form-modal ref="modal"-->
    <!--                   @added="modalAddCallback"-->
    <!--                   @updated="modalUpdateCallback" />-->
  </div>
</template>

<script lang="ts">
  export default {
    name: 'execLog'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import ExecLogTable from './components/exec-log-table.vue';

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
