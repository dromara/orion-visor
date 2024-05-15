<template>
  <a-layout class="full-layout">
    <!-- 页面 -->
    <a-layout-content>
      <!-- 水印 -->
      <a-watermark :grayscale="true"
                   :alpha=".6"
                   :z-index="9999"
                   style="width: 100%; height: 100%;"
                   :content="userStore.username || ''">
        <PageLayout />
      </a-watermark>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
  export default {
    name: 'fullLayout'
  };
</script>

<script lang="ts" setup>
  import { useUserStore } from '@/store';
  import PageLayout from './page-layout.vue';

  const userStore = useUserStore();

</script>

<style lang="less" scoped>
  .full-layout {
    height: 100vh;
    width: 100%;

    .arco-layout-content {
      height: 100%;
      width: 100%;
    }
  }
</style>
