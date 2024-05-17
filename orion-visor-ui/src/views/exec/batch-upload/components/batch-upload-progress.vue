<template>
  <div class="container">
    <!-- 表头 -->
    <div class="panel-header">
      <h3>传输列表</h3>
    </div>
    <div class="wrapper">
      <a-scrollbar style="overflow-y: auto; height: 100%;">
        <!-- 主机 -->
        <div v-for="file in files" class="file-item">
          <!-- 图标 -->
          <div class="file-item-icon span-blue">
            <icon-file />
          </div>
          <!-- 文件路径 -->
          <div class="file-item-path text-ellipsis" :title="file.filePath">
            {{ file.filePath }}
          </div>
          <!-- 状态 -->
          <div class="file-item-status">
            <!-- 文件大小 -->
            <div class="file-item-size span-blue">
              <!-- 当前大小 -->
              <template v-if="file.status === UploadTaskFileStatus.WAITING || file.status === UploadTaskFileStatus.UPLOADING">
                {{ getFileSize(file.current || 0) }}
              </template>
              <!-- 总大小 -->
              <template v-else>
                {{ getFileSize(file.fileSize) }}
              </template>
            </div>
            <!-- 进度 -->
            <a-tooltip position="left"
                       :content="((file.current || 0) / file.fileSize * 100).toFixed(2) + '%'"
                       mini>
              <a-progress type="circle"
                          size="mini"
                          :status="getDictValue(fileStatusKey, file.status, 'status') as any"
                          :percent="(file.current || 0) / file.fileSize" />
            </a-tooltip>
          </div>
        </div>
      </a-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    name: 'batchUploadProgress'
  };
</script>

<script lang="ts" setup>
  import type { UploadTaskFile } from '@/api/exec/upload-task';
  import { fileStatusKey } from '../types/const';
  import { UploadTaskFileStatus } from '@/views/exec/upload-task/types/const';
  import { useDictStore } from '@/store';
  import { getFileSize } from '@/utils/file';

  const emits = defineEmits(['update:selectedHost']);
  const props = defineProps<{
    files: Array<UploadTaskFile>;
  }>();

  const { getDictValue } = useDictStore();

</script>

<style lang="less" scoped>
  @icon-width: 24px;
  @status-width: 102px;

  .wrapper {
    width: 100%;
    height: calc(100% - 36px);
    position: relative;

    .file-item {
      padding: 12px;
      border-radius: 6px;
      margin-bottom: 12px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      position: relative;
      background: var(--color-fill-1);

      &:last-child {
        margin-bottom: 0;
      }

      &:hover {
        background: var(--color-fill-2);
      }

      &-icon {
        width: @icon-width;
        font-size: 18px;
      }

      &-path {
        padding: 2px 0;
        width: calc(100% - @icon-width - @status-width);
        display: inline-block;
        font-size: 14px;
        color: var(--color-text-1);
      }

      &-size {
        font-size: 12px;
        margin-right: 12px;
        user-select: none;
      }

      &-status {
        width: @status-width;
        display: flex;
        justify-content: flex-end;
        align-items: center;
      }
    }
  }

  :deep(.arco-scrollbar) {
    position: absolute;
    height: 100%;
    width: 100%;
  }

</style>
