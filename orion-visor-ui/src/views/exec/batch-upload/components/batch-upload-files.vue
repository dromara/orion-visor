<template>
  <div class="container">
    <!-- 表头 -->
    <div class="panel-header">
      <h3>文件列表</h3>
      <!-- 操作 -->
      <a-button-group size="mini" :disabled="startStatus">
        <a-button @click="clear">清空</a-button>
        <!-- 选择文件 -->
        <a-upload v-model:file-list="files"
                  :auto-upload="false"
                  :show-file-list="false"
                  :multiple="true">
          <template #upload-button>
            <a-button type="primary">选择文件</a-button>
          </template>
        </a-upload>
        <!-- 选择文件夹 -->
        <a-upload v-model:file-list="files"
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
    <div v-if="files.length" class="files-container">
      <a-scrollbar style="overflow-y: auto; height: 100%;">
        <a-upload class="files-wrapper"
                  :class="[ startStatus ? 'uploading-files-wrapper' : 'waiting-files-wrapper' ]"
                  v-model:file-list="files"
                  :auto-upload="false"
                  :show-cancel-button="false"
                  :show-retry-button="false"
                  :show-remove-button="!startStatus"
                  :show-file-list="true">
          <template #upload-button />
          <template #file-name="{ fileItem }">
            <div class="file-name-wrapper">
              <!-- 文件名称 -->
              <span class="file-name text-ellipsis"
                    :title="fileItem.file.webkitRelativePath || fileItem.file.name">
                {{ fileItem.file.webkitRelativePath || fileItem.file.name }}
              </span>
              <!-- 文件大小 -->
              <span class="file-size span-blue">
              {{ getFileSize(fileItem.file.size) }}
            </span>
            </div>
          </template>
        </a-upload>
      </a-scrollbar>
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
  import type { IFileUploader } from '@/components/system/uploader/const';
  import { computed, onUnmounted, ref } from 'vue';
  import { getFileSize } from '@/utils/file';
  import FileUploader from '@/components/system/uploader/file-uploader';

  const emits = defineEmits(['update:fileList', 'end', 'error', 'clearFile']);
  const props = defineProps<{
    fileList: Array<FileItem>;
  }>();

  const startStatus = ref(false);
  const uploader = ref<IFileUploader>();
  const files = computed<Array<FileItem>>({
    get() {
      return props.fileList;
    },
    set(e) {
      if (e) {
        emits('update:fileList', e);
      } else {
        emits('update:fileList', []);
      }
    }
  });

  // 开始上传
  const startUpload = async (token: string) => {
    // 修改状态
    startStatus.value = true;
    props.fileList.forEach(s => s.status = 'uploading');
    // 开始上传
    try {
      uploader.value = new FileUploader(token, props.fileList);
      uploader.value?.setHook(() => {
        emits('end');
      });
      await uploader.value?.start();
    } catch (e) {
      emits('error');
    }
  };

  // 清空
  const clear = () => {
    startStatus.value = false;
    emits('clearFile');
  };

  // 关闭
  const close = () => {
    startStatus.value = false;
    uploader.value?.close();
  };

  defineExpose({ startUpload, close });

  // 卸载时关闭
  onUnmounted(() => {
    uploader.value?.close();
  });

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
      padding: 0 12px 0 0 !important;
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
    }

    :deep(.arco-upload) {
      display: none;
    }

    :deep(.arco-upload-list) {
      padding: 0;
      max-height: 100%;
    }

    :deep(.arco-upload-list-item-error) {
      .arco-upload-list-item-name {
        margin-right: 0 !important;
      }

      .arco-upload-progress {
        display: none;
      }
    }

    :deep(.arco-upload-list-item-name-link) {
      width: 100%;
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
      width: calc(100% - @file-size-width);
      padding: 2px 0;
      color: var(--color-text-1);
    }

    .file-size {
      width: @file-size-width;
      display: inline-flex;
      font-size: 13px;
      justify-content: flex-end;
      align-items: center;
      user-select: none;
    }
  }

  :deep(.arco-scrollbar) {
    position: absolute;
    height: 100%;
    width: 100%;
  }

</style>
