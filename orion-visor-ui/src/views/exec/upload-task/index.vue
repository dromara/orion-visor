<template>
  <div class="layout-container" v-if="render">
    <!-- 列表-表格 -->
    <upload-task-table ref="table"
                       @open-clear="(e) => clear.open(e)" />
    <!-- 清理模态框 -->
    <upload-task-clear-modal ref="clear"
                             @clear="() => table.reload()" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'uploadTask'
  };
</script>

<script lang="ts" setup>
  import { ref, onBeforeMount } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import UploadTaskTable from './components/upload-task-table.vue';
  import UploadTaskClearModal from './components/upload-task-clear-modal.vue';

  const render = ref(false);
  const table = ref();
  const clear = ref();

  // 加载字典值
  onBeforeMount(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
    render.value = true;
  });

</script>

<style lang="less" scoped>

</style>
