<template>
  <div class="panel-container">
    <!-- 上传步骤 -->
    <batch-upload-step class="panel-item step-panel-container"
                       :step="step"
                       :status="stepStatus" />
    <!-- 上传表单 -->
    <batch-upload-form class="panel-item form-panel-container" />
    <!-- 上传文件 -->
    <batch-upload-files class="panel-item files-panel-container" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'batchUploadPanel'
  };
</script>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { UploadStep, UploadStepStatus } from '../types/const';
  import BatchUploadStep from './batch-upload-step.vue';
  import BatchUploadForm from './batch-upload-form.vue';
  import BatchUploadFiles from '@/views/exec/batch-upload/components/batch-upload-files.vue';

  const step = ref(UploadStep.PREPARATION);
  const stepStatus = ref(UploadStepStatus.PROCESS);

</script>

<style lang="less" scoped>
  @step-width: 258px;
  @center-width: 398px;
  @files-width: calc(100% - @step-width - 16px - @center-width - 16px);

  .panel-container {
    height: 100%;
    display: flex;
    position: relative;

    .panel-item {
      height: 100%;
      padding: 16px;
      border-radius: 4px;
      margin-right: 16px;
      background: var(--color-bg-2);
    }

    .step-panel-container {
      width: @step-width;
    }

    .form-panel-container {
      width: @center-width;
    }

    .files-panel-container {
      margin-right: 0;
      width: @files-width;
    }
  }

  :deep(.panel-header) {
    width: 100%;
    height: 28px;
    margin-bottom: 8px;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;

    h3, > span {
      margin: 0;
      overflow: hidden;
      white-space: nowrap;
    }

    h3 {
      color: var(--color-text-1);
    }
  }

</style>
