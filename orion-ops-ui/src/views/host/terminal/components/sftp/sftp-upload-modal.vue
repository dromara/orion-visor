<template>
  <a-modal v-model:visible="visible"
           top="80px"
           title-align="start"
           title="文件上传"
           ok-text="上传"
           :body-style="{ padding: '20px' }"
           :align-center="false"
           :mask-closable="false"
           :unmount-on-close="true"
           :on-before-ok="handlerOk"
           @cancel="handleClose">
    <div class="upload-container">
      <div class="parent-wrapper mb16">
        <span class="parent-label">上传至文件夹:</span>
        <a-input class="parent-input"
                 v-model="parentPath"
                 placeholder="上传目录" />
      </div>
      <a-space>
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
      </a-space>
      <!-- 文件列表 -->
      <a-upload v-if="fileList.length"
                class="file-list-uploader"
                v-model:file-list="fileList"
                :auto-upload="false"
                :show-file-list="true">
        <template #upload-button />
        <template #file-name="{ fileItem }">
          <div class="file-name-wrapper">
            <!-- 文件名称 -->
            <a-tooltip position="left"
                       :mini="true"
                       :auto-fix-position="false"
                       content-class="terminal-tooltip-content"
                       arrow-class="terminal-tooltip-content"
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
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'sftpUploadModal'
  };
</script>

<script lang="ts" setup>
  import type { FileItem } from '@arco-design/web-vue';
  import { ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import useVisible from '@/hooks/visible';
  import { getFileSize } from '@/utils/file';

  const { visible, setVisible } = useVisible();
  const { transferManager } = useTerminalStore();

  const hostId = ref();
  const parentPath = ref('');
  const fileList = ref<FileItem[]>([]);

  // 打开
  const open = (host: number, parent: string) => {
    hostId.value = host;
    parentPath.value = parent;
    setVisible(true);
  };

  defineExpose({ open });

  // 确定
  const handlerOk = () => {
    if (!parentPath.value) {
      Message.error('请输入上传目录');
      return false;
    }
    if (!fileList.value.length) {
      return true;
    }
    // 添加到上传列表
    const files = fileList.value.map(s => s.file as File);
    transferManager.addUpload(hostId.value, parentPath.value, files);
    Message.success('已开始上传, 点击右侧传输列表查看进度');
    // 清空
    handlerClear();
    return true;
  };

  // 关闭
  const handleClose = () => {
    handlerClear();
  };

  // 清空
  const handlerClear = () => {
    fileList.value = [];
  };

</script>

<style lang="less" scoped>
  @file-size-width: 82px;

  .upload-container {
    width: 100%;
  }

  .parent-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .parent-label {
      width: 98px;
    }

    .parent-input {
      width: 386px;
    }
  }

  .file-list-uploader {
    margin-top: 24px;

    :deep(.arco-upload) {
      display: none;
    }

    :deep(.arco-upload-list) {
      padding: 0 12px 0 0;
      max-height: calc(100vh - 386px);
      overflow-x: hidden;
      overflow-y: auto;
    }

    :deep(.arco-upload-list-item-name) {
      margin-right: 0 !important;
    }

    :deep(.arco-upload-list-item-name-text) {
      width: 100%;
    }

    :deep(.arco-upload-list-item:first-of-type) {
      margin-top: 0 !important;
    }

    :deep(.arco-upload-list-item .arco-upload-progress) {
      display: none;
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
