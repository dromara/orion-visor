<template>
  <div class="container">
    <!-- 表头 -->
    <div class="panel-header">
      <h3>文件列表</h3>
      <!-- 操作 -->
      <a-button-group size="small">
        <a-button @click="clear">清空</a-button>
        <!-- 选择文件 -->
        <a-upload v-model:file-list="fileList"
                  :auto-upload="false"
                  :show-file-list="false"
                  :multiple="true">
          <template #upload-button>
            <a-button type="primary">选择文件</a-button>
          </template>
        </a-upload>
        <!-- 选择文件夹 -->
        <a-upload v-model:file-list="fileList"
                  :auto-upload="false"
                  :show-file-list="false"
                  :directory="true">
          <template #upload-button>
            <a-button type="primary">选择文件夹</a-button>
          </template>
        </a-upload>
      </a-button-group>
    </div>
    <!-- 文件列表 -->
    <div v-if="fileList.length" class="files-container">
      <a-upload class="files-wrapper"
                :class="['waiting-files-wrapper']"
                v-model:file-list="fileList"
                :auto-upload="false"
                :show-cancel-button="false"
                :show-remove-button="true"
                :show-file-list="true">
        <template #upload-button />
        <template #file-name="{ fileItem }">
          <div class="file-name-wrapper">
            <!-- 文件名称 -->
            <a-tooltip position="left"
                       :mini="true"
                       :content="fileItem.file.webkitRelativePath || fileItem.file.name">
              <!-- 文件名称 -->
              <span class="file-name text-ellipsis">
                {{ fileItem.file.webkitRelativePath || fileItem.file.name }}
              </span>
            </a-tooltip>
            <!-- 文件大小 -->
            <span class="file-size span-blue">
              {{ getFileSize(fileItem.file.size) }}
            </span>
          </div>
        </template>
      </a-upload>
    </div>
    <!-- 未选择文件 -->
    <a-result v-else
              class="usn"
              status="404"
              subtitle="请先点击上方按钮选择文件" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'batchUploadFiles'
  };
</script>

<script lang="ts" setup>
  import type { FileItem } from '@arco-design/web-vue';
  import { ref } from 'vue';
  import { getFileSize } from '@/utils/file';

  const fileList = ref<FileItem[]>([]);

  // 清空
  const clear = () => {
  };

</script>

<style lang="less" scoped>
  @file-size-width: 82px;

  .files-container {
    width: 100%;
    height: calc(100% - 36px);
    position: relative;
  }

  :deep(.waiting-files-wrapper) {
    .arco-upload-list {
      padding: 0 6px 0 0 !important;
    }

    .arco-upload-list-item-name {
      margin-right: 0 !important;
    }

    .arco-upload-list-item .arco-upload-progress {
      display: none;
    }
  }

  :deep(.uploading-files-wrapper) {
    .arco-upload-list {
      padding: 0 !important;
    }

    .arco-upload-list-item-name {
      margin-right: 10px !important;
    }
  }

  .files-wrapper {
    :deep(.arco-upload-wrapper) {
      position: absolute;
      height: 100%;
      overflow-y: auto;
    }

    :deep(.arco-upload) {
      display: none;
    }

    :deep(.arco-upload-list) {
      max-height: 100%;
      padding: 0;
      overflow-y: auto;
    }

    :deep(.arco-upload-list-item-name-text) {
      width: 100%;
    }

    :deep(.arco-upload-list-item:first-of-type) {
      margin-top: 0 !important;
    }
  }

  .file-name-wrapper {
    display: flex;
    justify-content: space-between;

    .file-name {
      color: var(--color-text-1);
      display: inline-block;
      width: calc(100% - @file-size-width);
    }

    .file-size {
      font-size: 13px;
      display: inline-block;
      width: @file-size-width;
      text-align: end;
    }
  }

</style>
