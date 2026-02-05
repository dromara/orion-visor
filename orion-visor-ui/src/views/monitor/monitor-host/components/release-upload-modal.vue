<template>
  <a-modal v-model:visible="visible"
           top="80px"
           width="400px"
           title-align="start"
           title="探针发布包上传"
           ok-text="上传"
           :body-style="{ padding: 0 }"
           :align-center="false"
           :mask-closable="false"
           :unmount-on-close="true"
           :on-before-ok="handlerOk"
           :ok-button-props="{ disabled: !fileList.length }"
           @cancel="handleClose">
    <a-spin class="upload-container" :loading="loading">
      <!-- 选择文件 -->
      <a-upload class="file-list-uploader"
                v-model:file-list="fileList"
                accept=".tar.gz"
                :auto-upload="false"
                :show-file-list="true"
                draggable
                @change="onSelectFile" />
    </a-spin>
  </a-modal>
</template>

<script lang="ts">
  export default {
    name: 'releaseUploadModal'
  };
</script>

<script lang="ts" setup>
  import type { FileItem } from '@arco-design/web-vue';
  import { ref } from 'vue';
  import { Message } from '@arco-design/web-vue';
  import { uploadAgentRelease } from '@/api/asset/host-agent';
  import useVisible from '@/hooks/visible';
  import useLoading from '@/hooks/loading';

  const { visible, setVisible } = useVisible();
  const { loading, setLoading } = useLoading();

  const fileList = ref<FileItem[]>([]);

  // 打开
  const open = () => {
    setVisible(true);
  };

  defineExpose({ open });

  // 选择文件回调
  const onSelectFile = (files: Array<FileItem>) => {
    if (files.length) {
      fileList.value = [files[files.length - 1]];
    } else {
      fileList.value = [];
    }
  };

  // 确定
  const handlerOk = async () => {
    if (!fileList.value.length) {
      Message.error('请选择文件');
      return false;
    }
    setLoading(true);
    try {
      const { data } = await uploadAgentRelease(fileList.value[0].file as File);
      Message.success(`上传成功: 版本 v${data}`);
      // 清空
      handlerClear();
      return true;
    } catch (e) {
      return false;
    } finally {
      setLoading(false);
    }
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
    padding: 16px;
  }

  .file-list-uploader {
    :deep(.arco-upload-list-item:first-of-type) {
      margin-top: 12px !important;
    }

    :deep(.arco-upload-list-item .arco-upload-progress) {
      display: none;
    }
  }

</style>
