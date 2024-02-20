<template>
  <a-modal :visible="true"
           top="80px"
           title-align="start"
           title="文件上传"
           ok-text="上传"
           :align-center="false"
           :mask-closable="false"
           :unmount-on-close="true"
           :on-before-ok="handlerOk">
    <div class="upload-container">
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
      <a-upload class="file-list-uploader"
                v-model:file-list="fileList"
                :auto-upload="false"
                :show-file-list="true">
        <template #upload-button>
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
  import useVisible from '@/hooks/visible';
  import { nextTick, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { permission10toString } from '@/utils/file';
  import SftpSession from '../../handler/sftp-session';

  const { visible, setVisible } = useVisible(true);
  const { sessionManager } = useTerminalStore();

  const fileList = ref([]);

  const handlerOk = () => {
    console.log(fileList.value);
    return false;
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
      max-height: calc(100vh - 328px);
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
