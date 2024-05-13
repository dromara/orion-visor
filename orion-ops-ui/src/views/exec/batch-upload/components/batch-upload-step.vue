<template>
  <div class="container">
    <a-steps :current="status.step"
             :status="status.status as any"
             direction="vertical">
      <!-- 创建任务 -->
      <a-step description="创建上传任务">创建任务</a-step>
      <!-- 上传文件 -->
      <a-step>
        上传文件
        <template #icon v-if="status.step === 2">
          <icon-loading />
        </template>
        <template #description>
          <span>将文件上传到临时分区</span><br>
          <span v-if="status.step === 2" class="span-red desc-tips">
            在此期间请不要关闭页面
          </span>
        </template>
      </a-step>
      <!-- 分发文件 -->
      <a-step>
        分发文件
        <template #icon v-if="status.step === 3">
          <icon-loading />
        </template>
        <template #description>
          <span>将文件分发到目标服务器</span><br>
          <span v-if="status.step === 3" class="span-blue desc-tips">
            在此期间可以关闭页面
          </span>
        </template>
      </a-step>
      <!-- 上传完成 -->
      <a-step description="释放资源">上传完成</a-step>
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

  .desc-tips {
    display: inline-block;
    margin-top: 6px;
    font-weight: 600;
  }
</style>
