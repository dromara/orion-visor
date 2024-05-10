<template>
  <div class="container">
    <a-steps :current="status.step"
             :status="status.status as any"
             direction="vertical">
      <a-step description="创建上传任务">创建任务</a-step>
      <a-step>
        上传文件
        <template #description>
          <span>将文件上传到临时分区</span><br>
          <span class="span-red">在此期间请不要关闭页面</span>
        </template>
      </a-step>
      <a-step>
        分发文件
        <template #description>
          <span>将文件分发到目标服务器</span><br>
          <span>可以关闭页面</span>
        </template>
      </a-step>
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
  import type { UploadTaskStatusType } from '../types/const';

  defineProps<{
    status: UploadTaskStatusType;
  }>();

</script>

<style lang="less" scoped>
  .container {
    user-select: none;
  }
</style>
