<template>
  <div class="container">
    <a-steps :current="step"
             :status="status as any"
             direction="vertical">
      <a-step description="创建上传任务">创建任务</a-step>
      <a-step description="将文件上传到临时分区">上传文件</a-step>
      <a-step description="将文件分发到目标服务器">分发文件</a-step>
      <a-step description="上传完成并释放资源">上传完成</a-step>
    </a-steps>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'batchUploadStep'
  };
</script>

<script lang="ts" setup>

  defineProps<{
    step: number;
    status: string;
  }>();

</script>

<style lang="less" scoped>
</style>
