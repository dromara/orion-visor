<template>
  <div>
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
                             :selected-files="selectFiles"
                             :close-message="closeMessage"
                             :current-path="currentPath"
                             :session="session"
                             @load-file="loadFiles"
                             @set-loading="setTableLoading"
                             @create-file="openCreate"
                             @delete-file="deleteFile"
                             @upload="openUpload"
                             @download="downloadFiles" />
          <!-- 表格 -->
          <sftp-table class="sftp-table-wrapper"
                      v-model:selected-files="selectFiles"
                      :session="session"
                      :list="fileList"
                      :loading="tableLoading"
                      :editor-loading="editorLoading"
                      @load-file="loadFiles"
                      @chmod-file="openChmod"
                      @move-file="openMove"
                      @edit-file="editFile"
                      @delete-file="deleteFile"
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
                              @save="editorSave"
                              @close="closeEditor" />
          <!-- 编辑器 -->
          <sftp-editor class="sftp-editor-wrapper"
                       ref="editorRef" />
        </a-spin>
      </template>
    </a-split>
    <!-- 创建文件模态框 -->
    <sftp-create-modal ref="createModal" :session="session" />
    <!-- 移动文件模态框 -->
    <sftp-move-modal ref="moveModal" :session="session" />
    <!-- 文件提权模态框 -->
    <sftp-chmod-modal ref="chmodModal" :session="session" />
    <!-- 文件上传模态框 -->
    <sftp-upload-modal ref="uploadModal" :session="session" />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'sftpView'
  };
</script>

<script lang="ts" setup>
  import type { ISftpSession, ISftpSessionHandler, SftpFile, TerminalSessionTabItem } from '@/views/terminal/interfaces';
  import { onMounted, onUnmounted, ref } from 'vue';
  import { useTerminalStore } from '@/store';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import { getSftpFileContent, setSftpFileContent } from '@/api/terminal/terminal-sftp';
  import { isString } from '@/utils/is';
  import SftpTableHeader from './sftp-table-header.vue';
  import SftpTable from './sftp-table.vue';
  import SftpEditorHeader from './sftp-editor-header.vue';
  import SftpEditor from './sftp-editor.vue';
  import SftpCreateModal from './sftp-create-modal.vue';
  import SftpMoveModal from './sftp-move-modal.vue';
  import SftpChmodModal from './sftp-chmod-modal.vue';
  import SftpUploadModal from './sftp-upload-modal.vue';

  const props = defineProps<{
    item: TerminalSessionTabItem;
  }>();

  const { sessionManager, transferManager } = useTerminalStore();
  const { loading: tableLoading, setLoading: setTableLoading } = useLoading(true);
  const { loading: editorLoading, setLoading: setEditorLoading } = useLoading();

  const session = ref<ISftpSession>();
  const currentPath = ref<string>('');
  const fileList = ref<Array<SftpFile>>([]);
  const selectFiles = ref<Array<string>>([]);
  const splitSize = ref(1);
  const closeMessage = ref<string>();
  const editorView = ref(false);
  const editorRef = ref();
  const editorFileName = ref('');
  const editorFilePath = ref('');
  const createModal = ref();
  const moveModal = ref();
  const chmodModal = ref();
  const uploadModal = ref();

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
    session.value?.setContent(editorFilePath.value);
  };

  // 关闭编辑器
  const closeEditor = () => {
    splitSize.value = 1;
    editorView.value = false;
    editorFileName.value = '';
    editorFilePath.value = '';
  };

  // 打开创建文件
  const openCreate = (isTouch: boolean) => {
    createModal.value.open(currentPath.value, isTouch);
  };

  // 打开文件移动
  const openMove = (path: string) => {
    moveModal.value.open(path);
  };

  // 打开文件提权
  const openChmod = (path: string, permission: number) => {
    chmodModal.value.open(path, permission);
  };

  // 打开文件上传
  const openUpload = () => {
    uploadModal.value.open(currentPath.value);
  };

  // 删除文件
  const deleteFile = (paths: Array<string>) => {
    if (!paths.length) {
      return;
    }
    // 删除
    selectFiles.value = [];
    session.value?.remove(paths);
  };

  // 下载文件
  const downloadFiles = async (paths: Array<string>, clear: boolean) => {
    if (!paths.length) {
      return;
    }
    // 映射为文件
    const files = fileList.value.filter(s => paths.includes(s.path))
      .map(s => {
        return { ...s };
      });
    if (clear) {
      selectFiles.value = [];
    }
    // 添加普通文件到下载队列
    const normalFiles = files.filter(s => !s.isDir);
    await transferManager.sftp.addDownload(session.value as ISftpSession, currentPath.value, normalFiles);
    // 将文件夹展开普通文件
    const directoryPaths = files.filter(s => s.isDir).map(s => s.path);
    if (directoryPaths.length) {
      session.value?.downloadFlatDirectory(currentPath.value, directoryPaths);
    }
  };

  // 连接成功回调
  const connectCallback = () => {
    loadFiles(currentPath.value || '~');
  };

  // 加载文件列表
  const loadFiles = (path: string) => {
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
  const onClose = (code: number, msg: string) => {
    closeMessage.value = msg;
    setTableLoading(false);
    setEditorLoading(false);
  };

  // 接收列表回调
  const resolveList = (path: string, result: string, msg: string, list: Array<SftpFile>) => {
    setTableLoading(false);
    if (!checkResult(result, msg)) {
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
  const resolveSftpGetContent = (result: string, msg: string, token: string) => {
    setTableLoading(false);
    setEditorLoading(false);
    // 检查结果
    if (!checkResult(result, msg)) {
      return;
    }
    setEditorLoading(true);
    editorRef.value?.setValue('');
    // 读取文件
    getSftpFileContent(token).then(async ({ data }) => {
      if (isString(data)) {
        // 成功为 string
        editorRef.value?.setValue(data || '');
      } else {
        // 失败为 object
        Message.error((data as any).msg || '读取失败');
      }
      setEditorLoading(false);
    }).catch(() => {
      setEditorLoading(false);
    });
  };

  // 接收修改文件内容响应
  const resolveSftpSetContent = (result: string, msg: string, token: string) => {
    setEditorLoading(false);
    // 检查结果
    if (!checkResult(result, msg)) {
      return;
    }
    setEditorLoading(true);
    // 获取文本
    const value = editorRef.value?.getValue() || '';
    // 保存
    setSftpFileContent(token, value).then(() => {
      setEditorLoading(false);
      Message.success('保存成功');
    }).catch(() => {
      setEditorLoading(false);
    });
  };

  // 接收下载文件夹展开文件响应
  const resolveDownloadFlatDirectory = (currentPath: string, result: string, msg: string, list: Array<SftpFile>) => {
    setTableLoading(false);
    // 检查结果
    if (!checkResult(result, msg)) {
      return;
    }
    transferManager.sftp.addDownload(session.value as ISftpSession, currentPath, list);
  };

  // 初始化会话
  onMounted(async () => {
    // 设置 extra
    const cwd = props.item.extra?.cwd;
    if (cwd) {
      currentPath.value = cwd;
    }
    // 创建终端会话
    session.value = sessionManager.createSession<ISftpSession>(props.item);
    const handler: ISftpSessionHandler = {
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
    };
    await sessionManager.openSftp(props.item, handler);
  });

  // 关闭会话
  onUnmounted(() => {
    if (props.item.key) {
      sessionManager.closeSession(props.item.key);
    }
  });

</script>

<style lang="less" scoped>
  @sftp-table-header-height: 32px + 8px;

  .split-view {
    width: 100%;
    height: 100%;
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
