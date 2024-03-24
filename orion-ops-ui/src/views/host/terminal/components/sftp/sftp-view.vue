<template>
  <div class="sftp-container">
    <a-split class="split-view"
             v-model:size="splitSize"
             :min="0.3"
             :disabled="!editorView">
      <!-- 左侧面板表格 -->
      <template #first>
        <a-spin class="sftp-table-container"
                :loading="tableLoading"
                :hide-icon="true">
          <!-- 表头 -->
          <sftp-table-header class="sftp-table-header"
                             v-model:selected-files="selectFiles"
                             :is-close="closed"
                             :close-message="closeMessage"
                             :current-path="currentPath"
                             :session="session"
                             @load-file="loadFiles"
                             @download="downloadFiles" />
          <!-- 表格 -->
          <sftp-table class="sftp-table-wrapper"
                      v-model:selected-files="selectFiles"
                      :session="session"
                      :list="fileList"
                      :loading="tableLoading"
                      @load-file="loadFiles"
                      @edit-file="editFile"
                      @download="downloadFiles" />
        </a-spin>
      </template>
      <template #second v-if="editorView">
        <a-spin class="sftp-editor-container"
                :loading="editorLoading">
          <!-- 表头 -->
          <sftp-editor-header class="sftp-editor-header"
                              :name="editorFileName"
                              :path="editorFilePath"
                              :session="session"
                              @save="editorSave"
                              @close="closeEditor" />
          <!-- 编辑器 -->
          <sftp-editor class="sftp-editor-wrapper"
                       ref="editorRef" />
        </a-spin>
      </template>
    </a-split>
    <!-- 创建文件模态框 -->
    <sftp-create-modal ref="createModal" />
    <!-- 移动文件模态框 -->
    <sftp-move-modal ref="moveModal" />
    <!-- 文件提权模态框 -->
    <sftp-chmod-modal ref="chmodModal" />
    <!-- 文件上传模态框 -->
    <sftp-upload-modal ref="uploadModal" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'sftpView'
  };
</script>

<script lang="ts" setup>
  import type { ISftpSession, SftpFile, TerminalTabItem } from '../../types/terminal.type';
  import { onMounted, onUnmounted, provide, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import { openSftpCreateModalKey, openSftpMoveModalKey, openSftpChmodModalKey, openSftpUploadModalKey } from '../../types/terminal.const';
  import SftpTableHeader from './sftp-table-header.vue';
  import SftpTable from './sftp-table.vue';
  import SftpEditorHeader from './sftp-editor-header.vue';
  import SftpEditor from './sftp-editor.vue';
  import SftpCreateModal from './sftp-create-modal.vue';
  import SftpMoveModal from './sftp-move-modal.vue';
  import SftpChmodModal from './sftp-chmod-modal.vue';
  import SftpUploadModal from './sftp-upload-modal.vue';

  const props = defineProps<{
    tab: TerminalTabItem
  }>();

  const { preference, sessionManager, transferManager } = useTerminalStore();
  const { loading: tableLoading, setLoading: setTableLoading } = useLoading(true);
  const { loading: editorLoading, setLoading: setEditorLoading } = useLoading();

  const session = ref<ISftpSession>();
  const currentPath = ref<string>('');
  const fileList = ref<Array<SftpFile>>([]);
  const selectFiles = ref<Array<string>>([]);
  const splitSize = ref(1);
  const closed = ref(false);
  const closeMessage = ref('');
  const editorView = ref(false);
  const editorRef = ref();
  const editorFileName = ref('');
  const editorFilePath = ref('');
  const createModal = ref();
  const moveModal = ref();
  const chmodModal = ref();
  const uploadModal = ref();

  // 暴露打开创建模态框
  provide(openSftpCreateModalKey, (sessionId: string, path: string, isTouch: boolean) => {
    createModal.value?.open(sessionId, path, isTouch);
  });

  // 暴露打开移动模态框
  provide(openSftpMoveModalKey, (sessionId: string, path: string) => {
    moveModal.value?.open(sessionId, path);
  });

  // 暴露打开提权模态框
  provide(openSftpChmodModalKey, (sessionId: string, path: string, permission: number) => {
    chmodModal.value?.open(sessionId, path, permission);
  });

  // 暴露打开上传模态框
  provide(openSftpUploadModalKey, () => {
    uploadModal.value?.open(props.tab.hostId, currentPath.value);
  });

  // 编辑文件
  const editFile = (name: string, path: string) => {
    setEditorLoading(true);
    setTableLoading(true);
    splitSize.value = 0.6;
    editorView.value = true;
    editorFileName.value = name;
    editorFilePath.value = path;
  };

  // 编辑器保存
  const editorSave = () => {
    setEditorLoading(true);
    const value = editorRef.value?.getValue() || '';
    session.value?.setContent(editorFilePath.value, value);
  };

  // 关闭编辑器
  const closeEditor = () => {
    splitSize.value = 1;
    editorView.value = false;
    editorFileName.value = '';
    editorFilePath.value = '';
  };

  // 下载文件
  const downloadFiles = (paths: Array<string>) => {
    if (!paths.length) {
      return paths;
    }
    Message.success('已开始下载, 点击右侧传输列表查看进度');
    // 映射为文件
    const files = fileList.value.filter(s => paths.includes(s.path))
      .map(s => {
        return { ...s };
      });
    // 添加普通文件到下载队列
    const normalFiles = files.filter(s => !s.isDir);
    transferManager.addDownload(props.tab.hostId as number, currentPath.value, normalFiles);
    // 将文件夹展开普通文件
    const directoryPaths = files.filter(s => s.isDir).map(s => s.path);
    if (directoryPaths.length) {
      session.value?.downloadFlatDirectory(currentPath.value, directoryPaths);
    }
  };

  // 连接成功回调
  const connectCallback = () => {
    loadFiles('~');
  };

  // 加载文件列表
  const loadFiles = (path: string) => {
    setTableLoading(true);
    session.value?.list(path);
  };

  // 检查结果
  const checkResult = (result: string, msg: string): boolean => {
    const success = !!Number.parseInt(result);
    if (!success) {
      Message.error(msg);
    }
    return success;
  };

  // 关闭回调
  const onClose = (forceClose: string, msg: string) => {
    closed.value = true;
    closeMessage.value = msg;
    setTableLoading(false);
    setEditorLoading(false);
  };

  // 接收列表回调
  const resolveList = (result: string, path: string, list: Array<SftpFile>) => {
    setTableLoading(false);
    if (!checkResult(result, '查询失败')) {
      return;
    }
    currentPath.value = path;
    fileList.value = list;
    selectFiles.value = [];
  };

  // 接收文件响应
  const resolveFileAction = (result: string, msg: string) => {
    setTableLoading(false);
    // 检查结果
    if (!checkResult(result, msg)) {
      return;
    }
    Message.success('操作成功');
    // 刷新列表
    loadFiles(currentPath.value);
  };

  // 接收获取文件内容响应
  const resolveSftpGetContent = (path: string, result: string, content: string) => {
    setTableLoading(false);
    setEditorLoading(false);
    // 检查结果
    if (!checkResult(result, '加载失败')) {
      return;
    }
    editorRef.value?.setValue(content);
  };

  // 接收修改文件内容响应
  const resolveSftpSetContent = (result: string, msg: string) => {
    setEditorLoading(false);
    // 检查结果
    if (!checkResult(result, msg)) {
      return;
    }
    Message.success('保存成功');
  };

  // 接收下载文件夹展开文件响应
  const resolveDownloadFlatDirectory = (currentPath: string, list: Array<SftpFile>) => {
    setTableLoading(false);
    transferManager.addDownload(props.tab.hostId as number, currentPath, list);
  };

  // 初始化会话
  onMounted(async () => {
    // 创建终端处理器
    session.value = await sessionManager.openSftp(props.tab, {
      setLoading: setTableLoading,
      connectCallback,
      onClose,
      resolveList,
      resolveSftpMkdir: resolveFileAction,
      resolveSftpTouch: resolveFileAction,
      resolveSftpMove: resolveFileAction,
      resolveSftpRemove: resolveFileAction,
      resolveSftpChmod: resolveFileAction,
      resolveDownloadFlatDirectory,
      resolveSftpGetContent,
      resolveSftpSetContent,
    });
  });

  // 关闭会话
  onUnmounted(() => {
    sessionManager.closeSession(props.tab.key);
  });

</script>

<style lang="less" scoped>
  @sftp-table-header-height: 32px + 8px;

  .sftp-container {
    width: 100%;
    height: calc(100vh - var(--header-height) - var(--panel-nav-height));
    position: relative;

    .split-view {
      width: 100%;
      height: 100%;
    }
  }

  .sftp-table-container, .sftp-editor-container {
    padding: 8px;
    width: 100%;
    height: 100%;

    .sftp-table-header, .sftp-editor-header {
      width: 100%;
      height: @sftp-table-header-height;
      padding-bottom: 8px;
    }

    .sftp-table-wrapper, .sftp-editor-wrapper {
      height: calc(100% - @sftp-table-header-height);
    }
  }

</style>
