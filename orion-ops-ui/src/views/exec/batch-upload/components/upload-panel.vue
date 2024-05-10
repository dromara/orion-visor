<template>
  <a-spin class="panel-container full" :loading="loading">
    <!-- 上传步骤 -->
    <batch-upload-step class="panel-item first-panel-container"
                       :status="status" />
    <!-- 上传表单 -->
    <batch-upload-form v-if="status.formPanel"
                       class="panel-item center-panel-container"
                       :form-model="formModel"
                       :status="status"
                       @upload="doCreateUploadTask"
                       @abort="abortUploadRequest"
                       @open-host="openHostModal"
                       @clear="clearForm" />
    <!-- 上传主机 -->
    <batch-upload-hosts v-else
                        class="panel-item center-panel-container"
                        v-model:selected-host="selectedHost"
                        :task="task"
                        @back="backFormPanel"
                        @cancel="doCancelUploadTask" />
    <!-- 文件列表 -->
    <batch-upload-files v-if="status.formPanel"
                        v-model:file-list="fileList"
                        class="panel-item last-panel-container"
                        ref="filesRef"
                        @end="uploadRequestEnd"
                        @error="uploadRequestError"
                        @clear-file="clearFile" />
    <!-- 传输进度 -->
    <template v-else>
      <template v-for="host in task.hosts">
        <batch-upload-progress v-if="host.id === selectedHost"
                               class="panel-item last-panel-container"
                               :files="host.files" />
      </template>
    </template>
    <!-- 主机模态框 -->
    <authorized-host-modal ref="hostModal"
                           @selected="setSelectedHost" />
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'uploadPanel'
  };
</script>

<script lang="ts" setup>
  import type { FileItem } from '@arco-design/web-vue';
  import type { UploadTaskCreateRequest, UploadTaskQueryResponse } from '@/api/exec/upload-task';
  import type { UploadTaskStatusType } from '../types/const';
  import { ref } from 'vue';
  import { UploadTaskStatus } from '../types/const';
  import { cancelUploadTask, createUploadTask, startUploadTask, getUploadTask } from '@/api/exec/upload-task';
  import useLoading from '@/hooks/loading';
  import { Message } from '@arco-design/web-vue';
  import BatchUploadStep from './batch-upload-step.vue';
  import BatchUploadForm from './batch-upload-form.vue';
  import BatchUploadFiles from './batch-upload-files.vue';
  import BatchUploadHosts from './batch-upload-hosts.vue';
  import BatchUploadProgress from './batch-upload-progress.vue';
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
  const task = ref<UploadTaskQueryResponse>({} as UploadTaskQueryResponse);
  const selectedHost = ref();
  const formModel = ref<UploadTaskCreateRequest>({ ...defaultForm() });
  const fileList = ref<Array<FileItem>>([]);
  const status = ref<UploadTaskStatusType>(UploadTaskStatus.WAITING);
  const filesRef = ref();
  const hostModal = ref<any>();

  // TODO pullstatus

  // 设置选中主机
  const setSelectedHost = (hosts: Array<number>) => {
    formModel.value.hostIdList = hosts;
  };

  // 创建上传任务
  const doCreateUploadTask = async () => {
    // 获取文件
    const files = fileList.value.map(s => {
      return {
        fileId: s.uid,
        filePath: s.file?.webkitRelativePath || s.file?.name,
        fileSize: s.file?.size,
      };
    });
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
    try {
      // 取消上传
      await cancelUploadTask(taskId.value, false);
      status.value = UploadTaskStatus.WAITING;
      Message.success('已取消');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 中断上传请求
  const abortUploadRequest = () => {
    status.value = UploadTaskStatus.WAITING;
    filesRef.value?.close();
  };

  // 上传请求结束
  const uploadRequestEnd = async () => {
    if (status.value.value === UploadTaskStatus.REQUESTING.value) {
      // 如果结束后还是请求中则代表请求完毕
      setLoading(true);
      try {
        // 开始上传
        await startUploadTask(taskId.value);
        // 查询任务
        const { data } = await getUploadTask(taskId.value);
        task.value = data;
        selectedHost.value = data.hosts[0].id;
        status.value = UploadTaskStatus.UPLOADING;
      } catch (e) {
        // 设置失败
        await uploadRequestError();
      } finally {
        setLoading(false);
      }
    } else {
      // 手动停止或者其他原因则修改为取消上传
      await doCancelUploadTask();
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

  // 返回表单页面
  const backFormPanel = () => {
    status.value = UploadTaskStatus.WAITING;
    taskId.value = undefined;
    task.value = undefined as any;
    selectedHost.value = undefined as any;
  };

  // 清空表单
  const clearForm = () => {
    formModel.value = { ...defaultForm() };
  };

  // 清空文件
  const clearFile = () => {
    fileList.value = [];
  };

</script>

<style lang="less" scoped>
  @step-width: 258px;
  @center-width: 398px;
  @last-width: calc(100% - @step-width - 16px - @center-width - 16px);

  .panel-container {
    height: 100%;
    display: flex;
    position: relative;

    .panel-item {
      height: 100%;
      padding: 16px;
      border-radius: 4px;
      margin-right: 16px;
      position: relative;
      background: var(--color-bg-2);
    }

    .first-panel-container {
      width: @step-width;
    }

    .center-panel-container {
      width: @center-width;
    }

    .last-panel-container {
      margin-right: 0;
      width: @last-width;
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
