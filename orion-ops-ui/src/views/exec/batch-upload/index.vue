<template>
  <div class="layout-container upload-container">
    <!-- 上传面板 -->
    <batch-upload-panel />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'batchUpload'
  };
</script>

<script lang="ts" setup>
  import { onMounted } from 'vue';
  import { useDictStore } from '@/store';
  import { dictKeys } from './types/const';
  import BatchUploadPanel from './components/batch-upload-panel.vue';

  // 加载字典值
  onMounted(async () => {
    const dictStore = useDictStore();
    await dictStore.loadKeys(dictKeys);
  });

</script>

<style lang="less" scoped>

  .upload-container {
    width: 100%;
    height: 100%;
    position: relative;
    display: flex;
  }

</style>
