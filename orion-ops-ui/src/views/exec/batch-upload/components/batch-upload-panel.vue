<template>
  <a-spin class="panel-container full" :loading="loading">
    <!-- 上传步骤 -->
    <batch-upload-step class="panel-item step-panel-container"
                       :status="status" />
    <!-- 上传表单 -->
    <batch-upload-form class="panel-item form-panel-container"
                       :form-model="formModel"
                       :status="status"
                       @upload="doCreateUploadTask"
                       @cancel="doCancelUploadTask"
                       @open-host="openHostModal"
                       @clear="clear" />
    <!-- 上传文件 -->
    <batch-upload-files class="panel-item files-panel-container"
                        ref="filesRef"
                        @end="uploadRequestEnd"
                        @error="uploadRequestError" />
    <!-- 主机模态框 -->
    <authorized-host-modal ref="hostModal"
                           @selected="setSelectedHost" />
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'batchUploadPanel'
  };
</script>

<script lang="ts" setup>
  import type { UploadTaskCreateRequest } from '@/api/exec/upload-task';
  import type { UploadTaskStatusType } from '../types/const';
  import { ref } from 'vue';
  import { UploadTaskStatus } from '../types/const';
  import { cancelUploadTask, createUploadTask, startUploadTask } from '@/api/exec/upload-task';
  import useLoading from '@/hooks/loading';
  import { Message } from '@arco-design/web-vue';
  import BatchUploadStep from './batch-upload-step.vue';
  import BatchUploadForm from './batch-upload-form.vue';
  import BatchUploadFiles from './batch-upload-files.vue';
  import AuthorizedHostModal from '@/components/asset/host/authorized-host-modal/index.vue';

  const defaultForm = (): UploadTaskCreateRequest => {
    return {
      description: '',
      remotePath: '/root/batch',
      hostIdList: [1],
      files: []
    };
  };

  const { loading, setLoading } = useLoading();

  const taskId = ref();
  const formModel = ref<UploadTaskCreateRequest>({ ...defaultForm() });
  const status = ref<UploadTaskStatusType>(UploadTaskStatus.WAITING);
  const filesRef = ref();
  const hostModal = ref<any>();

  // TODO pullstatus 按钮显示就可以去掉了吧
  // host tab
  // status tab

  // 设置选中主机
  const setSelectedHost = (hosts: Array<number>) => {
    formModel.value.hostIdList = hosts;
  };

  // 创建上传任务
  const doCreateUploadTask = async () => {
    // 获取文件
    const files = filesRef.value?.getFiles();
    if (!files || !files.length) {
      Message.error('请先选择需要上传的文件');
      return;
    }
    // 创建任务
    setLoading(true);
    status.value = UploadTaskStatus.WAITING;
    try {
      formModel.value.files = files;
      const { data } = await createUploadTask(formModel.value);
      taskId.value = data.id;
      status.value = UploadTaskStatus.REQUESTING;
      // 上传文件
      await filesRef.value.startUpload(data.token);
    } catch (e) {
      status.value = UploadTaskStatus.FAILED;
    } finally {
      setLoading(false);
    }
  };

  // 取消上传任务
  const doCancelUploadTask = async () => {
    setLoading(true);
    filesRef.value?.close();
    try {
      // 取消上传
      await cancelUploadTask(taskId.value, false);
      status.value = UploadTaskStatus.CANCELED;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 上传请求结束
  const uploadRequestEnd = async () => {
    if (status.value.value !== UploadTaskStatus.REQUESTING.value) {
      // 手动停止或者其他原因
      return;
    }
    // 如果结束后还是请求中则代表请求完毕
    setLoading(true);
    try {
      // 开始上传
      await startUploadTask(taskId.value);
      status.value = UploadTaskStatus.UPLOADING;
    } catch (e) {
      // 设置失败
      await uploadRequestError();
    } finally {
      setLoading(false);
    }
  };

  // 上传请求失败
  const uploadRequestError = async () => {
    setLoading(true);
    try {
      // 开始上传
      await cancelUploadTask(taskId.value, true);
      status.value = UploadTaskStatus.FAILED;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 打开主机模态框
  const openHostModal = () => {
    hostModal.value.open(formModel.value.hostIdList);
  };

  // 清空
  const clear = () => {
    status.value = UploadTaskStatus.WAITING;
    formModel.value = { ...defaultForm() };
    filesRef.value?.close();
  };

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
