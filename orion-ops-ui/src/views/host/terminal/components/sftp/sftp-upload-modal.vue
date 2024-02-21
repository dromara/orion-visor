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
      <div class="mb16">
        上传至文件夹: {{ parentPath }}
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
      <a-upload v-if="fileList.length" class="file-list-uploader"
                v-model:file-list="fileList"
                :auto-upload="false"
                :show-file-list="true">
        <template #upload-button />
        <template #file-name="{ fileItem }">
          <!-- 上传文件夹 -->
          <template v-if="fileItem.file.webkitRelativePath">
            <a-tooltip position="top"
                       :mini="true"
                       :auto-fix-position="false"
                       content-class="terminal-tooltip-content"
                       arrow-class="terminal-tooltip-content"
                       :content="fileItem.file.webkitRelativePath">
              <span>{{ fileItem.file.webkitRelativePath }}</span>
            </a-tooltip>
          </template>
          <!-- 上传文件 -->
          <template v-else>
            <a-tooltip position="top"
                       :mini="true"
                       :auto-fix-position="false"
                       content-class="terminal-tooltip-content"
                       arrow-class="terminal-tooltip-content"
                       :content="fileItem.file.name">
              <span>{{ fileItem.file.name }}</span>
            </a-tooltip>
          </template>
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
  import { ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import useVisible from '@/hooks/visible';
  import { TransferStatus, TransferType } from '../../types/terminal.const';

  const { visible, setVisible } = useVisible();
  const { transferManager } = useTerminalStore();

  const hostId = ref();
  const parentPath = ref('');
  const fileList = ref<any[]>([]);

  // 打开
  const open = (host: number, parent: string) => {
    hostId.value = host;
    parentPath.value = parent;
    setVisible(true);
  };

  defineExpose({ open });

  // 确定
  const handlerOk = () => {
    if (!fileList.value.length) {
      return true;
    }
    // 添加到上传列表
    const files = fileList.value.map(s => {
      return {
        type: TransferType.UPLOAD,
        hostId: hostId.value,
        name: s.file.webkitRelativePath || s.file.name,
        currentSize: 0,
        totalSize: s.file.size,
        status: TransferStatus.WAITING,
        parentPath: parentPath.value,
        file: s.file
      };
    });
    transferManager.addUpload(files);
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
  .upload-container {
    width: 100%;
  }

  .file-list-uploader {
    margin-top: 24px;

    :deep(.arco-upload) {
      display: none;
    }

    :deep(.arco-upload-list) {
      max-height: calc(100vh - 388px);
      overflow-y: auto;
      padding: 0 12px 0 0;
    }

    :deep(.arco-upload-list-item:first-of-type) {
      margin-top: 0 !important;
    }

    :deep(.arco-upload-list-item .arco-upload-progress) {
      display: none;
    }
  }
</style>
